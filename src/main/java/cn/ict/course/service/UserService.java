package cn.ict.course.service;

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
}
