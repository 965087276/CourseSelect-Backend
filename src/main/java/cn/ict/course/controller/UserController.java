package cn.ict.course.controller;

import cn.ict.course.entity.bo.UserUpdateInfo;
import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.LoginVO;
import cn.ict.course.entity.vo.UserDetailVO;
import cn.ict.course.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "学生、老师和管理员登录")
    @ApiImplicitParam(name = "loginDTO", value = "用户登录信息", required = true, dataType = "LoginDTO")
    public ResponseEntity<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return ResponseEntity.ok(loginVO);
    }

    @PostMapping("/users")
    @ApiOperation(value = "用户添加", notes = "测试添加用户")
    @ApiImplicitParam(name = "user", value = "用户登录信息", required = true, dataType = "User")
    public ResponseEntity save(@RequestBody User user) {
        return userService.save(user);
    }

//    @RequiresRoles(value={"admin", "student", "teacher"},logical = Logical.OR)
    @GetMapping("/users/{username}")
    @ApiOperation(value = "获取登录用户信息", notes = "根据url的用户名来获取用户详细信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "Long", paramType = "path")
    public ResponseEntity detail(@PathVariable String username) {
        UserDetailVO detail = userService.detail(username);
        return ResponseEntity.ok(detail);
    }

}
