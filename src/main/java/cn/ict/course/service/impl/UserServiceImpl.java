package cn.ict.course.service.impl;

import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.vo.login.LoginVO;
import cn.ict.course.repo.UserRepo;
import cn.ict.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jianyong Feng
 **/
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        User user = userRepo.findByUsername(loginDTO.getUsername());
        int loginCode;
        if (user == null) {
            loginCode = -1;
        } else {
            loginCode = user.getPassword().equals(loginDTO.getPassword()) ? 1 : 0;
        }
        return LoginVO.builder().code(loginCode).build();
    }
}
