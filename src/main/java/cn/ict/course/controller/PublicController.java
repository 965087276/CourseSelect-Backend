package cn.ict.course.controller;

import cn.ict.course.entity.dto.UserUpdateDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.entity.vo.EnableTimeVO;
import cn.ict.course.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/pub")
public class PublicController {

    private final UserService userService;
    private final CollegeService collegeService;
    private final CourseService courseService;
    private final CourseSelectService courseSelectService;
    private final ClassroomService classroomService;

    @Autowired
    public PublicController(
            CollegeService collegeService,
            CourseService courseService,
            CourseSelectService courseSelectService,
            ClassroomService classroomService,
            UserService userService
    ) {
        this.collegeService = collegeService;
        this.courseService = courseService;
        this.courseSelectService = courseSelectService;
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @GetMapping(value = "/colleges")
    @ApiOperation(value = "获取所有学院")
    ResponseEntity getCollegeList() {
        List<String> colleges = collegeService.getCollegeList();
        return ResponseEntity.ok(colleges);
    }

    @GetMapping(value = "/courses")
    @ApiOperation(value = "获取课程列表", notes = "已添加的所有课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "college", value = "学院", dataType = "String"),
            @ApiImplicitParam(name = "courseType", value = "课程类型", dataType = "String"),
            @ApiImplicitParam(name = "courseName", value = "课程名", dataType = "String"),
            @ApiImplicitParam(name = "day", value = "周几", dataType = "Integer"),
            @ApiImplicitParam(name = "time", value = "第几节课", dataType = "Integer"),
    })
    ResponseEntity getCourseList(@RequestParam(value = "college", required = false, defaultValue = "none") String college,
                                  @RequestParam(value = "courseType", required = false, defaultValue = "none") String courseType,
                                 @RequestParam(value = "courseName", required = false, defaultValue = "none") String courseName,
                                 @RequestParam(value = "day", required = false, defaultValue = "-1") Integer day,
                                 @RequestParam(value = "time", required = false, defaultValue = "-1") Integer time
                                 ) {
        return courseService.getCourseList(college, courseType, courseName, day, time);
    }

    @GetMapping(value = "/enabletimes")
    @ApiOperation(value = "获取选课开放截止时间", notes = "两个时间")
    ResponseEntity<EnableTimeVO> getEnableTime() {
        return courseSelectService.getEnableTime();
    }

    @GetMapping(value = "/course_select_status")
    @ApiOperation(value = "获取选课开放状态", notes = "是否开放")
    ResponseEntity getCourseSelectStatus() {
        return courseSelectService.getCourseSelectStatus();
    }

    @GetMapping(value = "/classrooms")
    @ApiOperation(value = "获取所有教室")
    ResponseEntity getAllClassrooms() {
        return classroomService.getAllClassrooms();
    }

    @PutMapping("/users")
    @ApiOperation(value = "修改用户信息")
    ResponseEntity updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userUpdateDTO);
    }
}
