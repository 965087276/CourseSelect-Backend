package cn.ict.course.controller;

import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.login.LoginVO;
import cn.ict.course.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "学生、老师和管理员登录")
    @ApiImplicitParam(name = "loginDTO", value = "用户登录信息", required = true, dataType = "LoginDTO")
    @PostMapping("/login")
    public ResponseEntity<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return ResponseEntity.ok(loginVO);
    }

}
