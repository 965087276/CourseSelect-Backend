package cn.ict.course.controller;

import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CourseSelectService;
import cn.ict.course.service.CourseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/teacher")
public class TeacherController {

    private final CourseService courseService;
    private final CourseSelectService courseSelectService;

    @Autowired
    public TeacherController(CourseService courseService,
                             CourseSelectService courseSelectService) {
        this.courseService = courseService;
        this.courseSelectService = courseSelectService;
    }

    @PostMapping(value = "/courses")
    @ApiOperation(value = "教师添加课程", notes = "输入信息添加课程")
    @ApiImplicitParam(name = "courseDTO", value = "课程信息", required = true, dataType = "CourseDTO")
    public ResponseEntity saveCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO);
    }

    @GetMapping(value = "/students/{courseCode}")
    @ApiOperation(value = "获取上该门课的学生信息")
    public ResponseEntity getStudentInfoByCourseCode(@PathVariable String courseCode) {
        return courseSelectService.getStudentInfoByCourseCode(courseCode);
    }

    @GetMapping(value = "courseTable/{teacherId}")
    @ApiOperation(value = "获取教师课表中的课程信息")
    @ApiImplicitParam(name = "teacherId", value = "教师用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity getTeacherCourseTable(@PathVariable String teacherId) {
        return courseService.getTeacherCourseTable(teacherId);
    }
}
