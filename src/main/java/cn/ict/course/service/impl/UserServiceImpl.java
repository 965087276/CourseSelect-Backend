package cn.ict.course.service.impl;

import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.vo.login.LoginVO;
import cn.ict.course.repo.UserRepo;
import cn.ict.course.service.UserService;
import org.graalvm.compiler.replacements.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jianyong Feng
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        User user = userRepo.findByUserCode(loginDTO.getUserCode());
        int loginCode;
        if (user == null) {
            loginCode = -1;
        } else {
            loginCode = user.getPassword().equals(loginDTO.getPassword()) ? 1 : 0;
        }
        return LoginVO.builder().code(loginCode).build();
    }
}
