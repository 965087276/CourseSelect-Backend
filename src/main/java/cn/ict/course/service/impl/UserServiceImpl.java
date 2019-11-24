package cn.ict.course.service.impl;

import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.vo.LoginVO;
import cn.ict.course.entity.vo.UserDetailVO;
import cn.ict.course.enums.ResultEnum;
import cn.ict.course.repo.UserRepo;
import cn.ict.course.service.UserService;
import com.github.dozermapper.core.Mapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author Jianyong Feng
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final Mapper mapper;

    public UserServiceImpl(Mapper mapper, UserRepo userRepo) {
        this.mapper = mapper;
        this.userRepo = userRepo;
    }


    @Override
    public LoginVO login(LoginDTO loginDTO) {

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        LoginVO loginVO = new LoginVO();


        // to-do 添加用户名密码空异常
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            loginVO.setCode(405);
            loginVO.setToken("error");
            return loginVO;
        }

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        Serializable sessionId = subject.getSession().getId();

        try {
            subject.login(token);
            loginVO.setCode(ResultEnum.SUCCESS.getCode());
            loginVO.setMessage(ResultEnum.SUCCESS.getMessage());
            loginVO.setToken(sessionId);
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
    public void save(User user) {
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";

        String password = user.getPassword();
        String encodedPassword = new SimpleHash(algorithmName,password,salt,times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        userRepo.save(user);
    }

    @Override
    public UserDetailVO detail(Long id) {
        User user = userRepo.findById(id).orElse(new User());
        return mapper.map(user, UserDetailVO.class);
    }

}
