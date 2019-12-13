package cn.ict.course.service;

import cn.ict.course.entity.bo.UserUpdateInfo;
import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.dto.UserUpdateDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.LoginVO;
import cn.ict.course.entity.vo.UserDetailVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Jianyong Feng
 **/
public interface UserService {

    LoginVO login(LoginDTO loginDTO);

    ResponseEntity save(User user);

    UserDetailVO detail(String username);

    /**
     * 删除用户
     * @param username 用户名
     * @return 删除结果
     */
    ResponseEntity deleteUserByUsername(String username);

    /**
     * 更新用户信息
     * @param username 用户名
     * @param info 需要修改的用户信息
     * @return 更新结果
     */
    ResponseEntity updateUserInfo(String username, UserUpdateInfo info);

    /**
     * 获取所有教师
     * @param username 用户名
     * @param realName 真实姓名
     * @param college 学院
     * @param curPage 当前页
     * @param pageSize 当前页条目数
     * @return 教师
     */
    ResponseEntity getAllUsersExceptAdmin(
            String username,
            String realName,
            String college,
            String role,
            int curPage,
            int pageSize
    );

    /**
     * 判断用户名是否重复
     * @param username 用户名
     * @return 验证结果
     */
    boolean repeatByUsername(String username);

    /**
     * Excel批量导入用户
     * @param role 用户角色
     * @param file 用户文件
     * @return 导入是否成功
     */
    ResponseEntity addUsersByExcel(String role, MultipartFile file) throws IOException;

    /**
     * 修改用户信息
     * @param userUpdate 更新的用户信息
     * @return 更新结果
     */
    ResponseEntity updateUser(UserUpdateDTO userUpdate);
}
