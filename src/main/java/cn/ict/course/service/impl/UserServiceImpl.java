package cn.ict.course.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.course.constants.PasswordConst;
import cn.ict.course.entity.bo.UserUpdateInfo;
import cn.ict.course.entity.db.QUser;
import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.dto.UserDTO;
import cn.ict.course.entity.dto.UserUpdateDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.LoginVO;
import cn.ict.course.entity.vo.TeacherInfoVO;
import cn.ict.course.entity.vo.UserDetailVO;
import cn.ict.course.enums.ResultEnum;
import cn.ict.course.repo.CoursePreselectRepo;
import cn.ict.course.repo.CourseSelectRepo;
import cn.ict.course.repo.UserRepo;
import cn.ict.course.service.UserService;
import cn.ict.course.utils.ListMapUtil;
import cn.ict.course.utils.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.Mapper;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jianyong Feng
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final CourseSelectRepo courseSelectRepo;
    private final CoursePreselectRepo coursePreselectRepo;

    private final Mapper mapper;

    public UserServiceImpl(
            Mapper mapper,
            UserRepo userRepo,
            CourseSelectRepo courseSelectRepo,
            CoursePreselectRepo coursePreselectRepo
            ) {
        this.mapper = mapper;
        this.userRepo = userRepo;
        this.courseSelectRepo = courseSelectRepo;
        this.coursePreselectRepo = coursePreselectRepo;
    }


    @Override
    public LoginVO login(LoginDTO loginDTO) {

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        LoginVO loginVO = new LoginVO();


        // to-do 添加用户名密码空异常
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            loginVO.setCode(405);
            loginVO.setAuthToken("error");
            return loginVO;
        }

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        Serializable sessionId = subject.getSession().getId();

        try {
            subject.login(token);
            loginVO.setCode(ResultEnum.SUCCESS.getCode());
            loginVO.setMessage(ResultEnum.SUCCESS.getMessage());
            loginVO.setAuthToken(sessionId);
        } catch (IncorrectCredentialsException e) {
            loginVO.setCode(ResultEnum.USERNAME_PASSWORD_NOT_MATCH.getCode());
            loginVO.setMessage(ResultEnum.USERNAME_PASSWORD_NOT_MATCH.getMessage());
        } catch (LockedAccountException e) {
            loginVO.setCode(ResultEnum.USER_LOCKED.getCode());
            loginVO.setMessage(ResultEnum.USER_LOCKED.getMessage());
        } catch (AuthenticationException e) {
            loginVO.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            loginVO.setMessage(ResultEnum.USER_NOT_EXIST.getMessage());
        } catch (Exception e) {
            loginVO.setCode(ResultEnum.UNKNOWN_EXCEPTION.getCode());
            loginVO.setMessage(ResultEnum.UNKNOWN_EXCEPTION.getMessage());
        }
        return loginVO;
    }

    /**
     * to-do：
     * 判断用户数据合法性
     *
     * @param user 需要保存的用户信息
     */
    @Override
    @Transactional
    public ResponseEntity save(User user) {

        String username = user.getUsername();

        if (repeatByUsername(username)) {
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "用户名重复");
        }

        String password = user.getPassword();
        if (password == null) {
            password = username.substring(username.length() - 6);
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName,password,salt,times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return ResponseEntity.ok();
    }

    @Override
    public UserDetailVO detail(String username) {
        User user = userRepo.findByUsername(username);
        return mapper.map(user, UserDetailVO.class);
    }

    /**
     * 删除用户
     *
     * 删除用户表中的用户
     * 删除course_select表中的记录
     * 删除course_preselect表中的记录
     *
     * @param username 用户名
     * @return 删除结果
     */
    @Override
    @Transactional
    public ResponseEntity deleteUserByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "该用户不存在"
            );
        }
        userRepo.deleteByUsername(username);
        courseSelectRepo.deleteAllByUsername(username);
        coursePreselectRepo.deleteAllByUsername(username);
        return ResponseEntity.ok();
    }

    /**
     * 更新用户信息
     *
     * @param info 需要修改的用户信息
     * @return 更新结果
     */
    @Override
    @Transactional
    public ResponseEntity updateUserInfo(String username, UserUpdateInfo info) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "该用户不存在"
            );
        }
        String email = info.getEmail();
        String phoneNumber = info.getPhoneNumber();
        String college = info.getCollege();

        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setCollege(college);
        userRepo.save(user);

        return ResponseEntity.ok();
    }

    /**
     * 获取所有教师
     *
     * @param username 用户名
     * @param realName 真实姓名
     * @param college  学院
     * @param curPage  当前页
     * @param pageSize 当前页条目数
     * @return 教师
     */
    @Override
    public ResponseEntity<Page<TeacherInfoVO>> getAllUsersExceptAdmin(String username, String realName, String college, String role, int curPage, int pageSize) {
        if (StrUtil.isBlank(role) || !(role.equals("student") || role.equals("teacher"))) {
            return ResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "用户角色输入错误"
            );
        }
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        QUser user = QUser.user;

        Predicate predicate = user.isNotNull().or(user.isNull());

        predicate = ExpressionUtils.and(predicate, user.role.eq(role));
        predicate = StrUtil.isBlank(username) ? predicate : ExpressionUtils.and(predicate, user.username.eq(username));
        predicate = StrUtil.isBlank(realName) ? predicate : ExpressionUtils.and(predicate, user.realName.eq(realName));
        predicate = StrUtil.isBlank(college) ? predicate : ExpressionUtils.and(predicate, user.college.eq(college));

        Page<TeacherInfoVO> teacherInfo = userRepo.findAll(predicate, pageable)
                .map(u -> mapper.map(u, TeacherInfoVO.class));

        return ResponseEntity.ok(teacherInfo);
    }

    /**
     * 判断用户名是否重复
     *
     * @param username 用户名
     * @return 验证结果
     */
    @Override
    public boolean repeatByUsername(String username) {
        return userRepo.findByUsername(username) != null;
    }

    /**
     * Excel批量导入用户
     *
     * @param role 用户角色
     * @param file 用户文件
     * @return 导入是否成功
     */
    @Override
    public ResponseEntity addUsersByExcel(String role, MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Map<String, Object>> readAll = reader.readAll();
        Map<String, String> map;
        if (role.equals("student")) {
            map = ListMapUtil.getStudentUserMap();
        } else if (role.equals("teacher")) {
            map = ListMapUtil.getTeacherUserMap();
        } else {
            return ResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "导入错误"
            );
        }
        List<Map<String, Object>> readTransformed = readAll.stream()
                .map(list -> ListMapUtil.mapTransform(list, map))
                .collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> usersInfo = readTransformed.stream()
                .map(list -> objectMapper.convertValue(list, UserDTO.class))
                .collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        for (UserDTO userInfo:usersInfo) {
            User user = mapper.map(userInfo, User.class);
            String username = userInfo.getUsername();
            if (repeatByUsername(username)) {
                return ResponseEntity.error(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        username + "用户名重复"
                );
            }
            String password = PasswordUtil.passwordGenerateByUsername(username);
            String salt = PasswordUtil.saltGenerate();
            password = PasswordUtil.passwordEncode(
                    password,
                    salt,
                    PasswordConst.PASSWORD_EGYPT_NAME,
                    PasswordConst.PASSWORD_EGYPT_TIMES
            );
            user.setPassword(password);
            user.setSalt(salt);
            user.setRole(role);
            users.add(user);
        }
        userRepo.saveAll(users);
        return ResponseEntity.ok();
    }

    /**
     * 修改用户信息
     *
     * @param userUpdate 更新的用户信息
     * @return 更新结果
     */
    @Override
    @Transactional
    public ResponseEntity updateUser(UserUpdateDTO userUpdate) {
        String username = userUpdate.getUsername();
        User user = userRepo.findByUsername(username);
        user.setCollege(userUpdate.getCollege());
        user.setRealName(userUpdate.getRealName());
        user.setEmail(userUpdate.getEmail());
        user.setPhoneNumber(userUpdate.getPhoneNumber());
        String oldPassword = userUpdate.getOldPassword();
        String oldPasswordEncoded = PasswordUtil.passwordEncode(oldPassword, user.getSalt());
        if(!oldPasswordEncoded.equals(user.getPassword())) {
            return ResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "历史密码输入错误"
            );
        }
        String newSalt = PasswordUtil.saltGenerate();
        String passwordEncoded = PasswordUtil.passwordEncode(userUpdate.getPassword(), newSalt);
        user.setSalt(newSalt);
        user.setPassword(passwordEncoded);
        userRepo.save(user);
        return ResponseEntity.ok();
    }


}
