package cn.ict.course.service;

import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.vo.login.LoginVO;

/**
 * @author Jianyong Feng
 **/
public interface UserService {

    LoginVO login(LoginDTO loginDTO);
}
