package cn.ict.course.controller;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseSelectStatsVO;
import cn.ict.course.service.CourseService;
import cn.ict.course.service.StudentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/student")
public class StudentController {

    private final CourseService courseService;

    private final StudentService studentService;

    @Autowired
    public StudentController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("/course_stats/students/{username}")
    @ApiOperation(value = "选课学分统计", notes = "获取某学生的选课学分统计信息")
    @ApiImplicitParam(name = "username", value = "学工号", required = true, dataType = "string", paramType = "path")
    public ResponseEntity<List<CourseSelectStatsVO>> getCourseSelectedStats(@PathVariable("username") String username) {
        return ResponseEntity.ok(studentService.getCoursesSelectedStats(username));
    }

}
