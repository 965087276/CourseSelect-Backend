package cn.ict.course.controller;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseVO;
import cn.ict.course.service.CollegeService;
import cn.ict.course.service.CourseService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    public PublicController(CollegeService collegeService, CourseService courseService) {
        this.collegeService = collegeService;
        this.courseService = courseService;
    }

    @GetMapping(value = "/colleges")
    @ApiOperation(value = "获取所有学院")
    ResponseEntity getCollegeList() {
        List<String> colleges = collegeService.getCollegeList();
        return ResponseEntity.ok(colleges);
    }

    @GetMapping(value = "/courses")
    @ApiOperation(value = "获取课程列表", notes = "已添加的所有课程")
    ResponseEntity getCourseList() {
        List<CourseVO> courses = courseService.getCourseList();
        return ResponseEntity.ok(courses);
    }
}
