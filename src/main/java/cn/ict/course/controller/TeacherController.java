package cn.ict.course.controller;

import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CourseService;
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
@RequestMapping(value = "/xk/api/teacher")
public class TeacherController {

    private final CourseService courseService;

    @Autowired
    public TeacherController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping(value = "/courses")
    @ApiOperation(value = "教师添加课程", notes = "输入信息添加课程")
    @ApiImplicitParam(name = "courseDTO", value = "课程信息", required = true, dataType = "CourseDTO")
    public ResponseEntity saveCourse(@RequestBody CourseDTO courseDTO) {
        courseService.addCourse(courseDTO);
        return ResponseEntity.ok();
    }
}
