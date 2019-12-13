package cn.ict.course.controller;

import cn.ict.course.entity.bo.UserUpdateInfo;
import cn.ict.course.entity.db.SelectionControl;
import cn.ict.course.entity.db.User;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CourseSelectService;
import cn.ict.course.service.CourseService;
import cn.ict.course.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/admin")
@Slf4j
public class AdminController {
    private final CourseService courseService;
    private final CourseSelectService courseSelectService;
    private final UserService userService;

    @Autowired
    public AdminController(
            CourseService courseService,
            CourseSelectService courseSelectService,
            UserService userService
    ) {
        this.courseService = courseService;
        this.courseSelectService = courseSelectService;
        this.userService = userService;
    }

    @PostMapping("/enabletimes_edit")
    @ApiOperation(value = "管理员编辑选课时间")
    @ApiImplicitParam(name = "selectionControl", value = "选课结束时间", required = true, dataType = "SelectionControl")
    public ResponseEntity editEnableTime(@RequestBody SelectionControl selectionControl) {
        Date startTime = selectionControl.getStartTime();
        Date endTime = selectionControl.getEndTime();
        return courseSelectService.updateEnableTime(startTime, endTime);
    }

    @DeleteMapping("/course/{courseCode}")
    @ApiOperation(value = "删除课程", notes = "使用课程编码删除课程")
    public ResponseEntity deleteCourseByCourseCode(@PathVariable String courseCode) {
        return courseService.deleteCourseByCourseCode(courseCode);
    }

    @DeleteMapping("/users/{username}")
    @ApiOperation(value = "删除用户", notes = "使用用户名删除用户")
    @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity deleteUserByUsername(@PathVariable String username) {
        return userService.deleteUserByUsername(username);
    }


    @PostMapping(value = "/users/{username}")
    @ApiOperation(value =  "更新用户信息", notes = "使用用户名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "userInfo", value = "用户信息", required = true, dataType = "UserUpdateInfo")
    }
    )
    public ResponseEntity updateUserInfoByUsername(@PathVariable String username, @RequestBody UserUpdateInfo userInfo) {
        return userService.updateUserInfo(username, userInfo);
    }

    @GetMapping(value = "/teachers")
    @ApiOperation(value = "获取所有用户", notes = "除了管理员之外的所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", dataType = "String"),
            @ApiImplicitParam(name = "college", value = "学院", dataType = "String"),
            @ApiImplicitParam(name = "role", value = "用户角色", required = true, dataType = "String"),
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "pageSize", value = "当前页条目数", required = true, dataType = "Long")
    })
    public ResponseEntity getAllUsersExceptAdmin(
            String username,
            String realName,
            String college,
            String role,
            int curPage,
            int pageSize
    ) {
        return userService.getAllUsersExceptAdmin(
                username,
                realName,
                college,
                role,
                curPage,
                pageSize
        );
    }

    @PostMapping("/users")
    @ApiOperation(value = "用户添加")
    @ApiImplicitParam(name = "user", value = "用户登录信息", required = true, dataType = "User")
    public ResponseEntity save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/usersExcel/{role}")
    @ApiOperation(value = "Excel批量导入用户", notes = "role为教师或学生")
    @ApiImplicitParam(name = "role", value = "用户角色", required = true, dataType = "String", paramType = "path")
    public ResponseEntity addUsersByExcel(
            @PathVariable String role,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return userService.addUsersByExcel(role, file);
    }

    @PostMapping("/coursesExcel")
    @ApiOperation(value = "管理员批量导入课程", notes = "使用excel")
    public ResponseEntity addCoursesByExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return courseService.addCoursesByExcel(file);
    }

}
