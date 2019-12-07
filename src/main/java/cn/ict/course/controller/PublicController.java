package cn.ict.course.controller;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.entity.vo.EnableTimeVO;
import cn.ict.course.service.CollegeService;
import cn.ict.course.service.CourseSelectService;
import cn.ict.course.service.CourseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/pub")
public class PublicController {

    private final CollegeService collegeService;
    private final CourseService courseService;
    private final CourseSelectService courseSelectService;

    @Autowired
    public PublicController(CollegeService collegeService,
                            CourseService courseService,
                            CourseSelectService courseSelectService) {
        this.collegeService = collegeService;
        this.courseService = courseService;
        this.courseSelectService = courseSelectService;
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
        List<CourseVO> courses = courseService.getCourseList(college, courseType, courseName, day, time);
        return ResponseEntity.ok(courses);
    }

    @GetMapping(value = "/enabletimes")
    @ApiOperation(value = "获取选课开放截止时间", notes = "两个时间")
    ResponseEntity<EnableTimeVO> getEnableTime() {
        return courseSelectService.getEnableTime();
    }
}
