package cn.ict.course.service;

import cn.ict.course.entity.bo.UserUpdateInfo;
import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.LoginVO;
import cn.ict.course.entity.vo.UserDetailVO;

/**
 * @author Jianyong Feng
 **/
public interface UserService {

    LoginVO login(LoginDTO loginDTO);

    void save(User user);

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
    ResponseEntity getAllTeachers(
            String username,
            String realName,
            String college,
            int curPage,
            int pageSize
    );
}
