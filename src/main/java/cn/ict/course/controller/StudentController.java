package cn.ict.course.controller;

import cn.ict.course.entity.db.CoursePreselect;
import cn.ict.course.entity.db.CourseSelect;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseSelectStatsVO;
import cn.ict.course.service.CoursePreSelectService;
import cn.ict.course.service.CourseService;
import cn.ict.course.service.StudentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/student")
public class StudentController {

    private final CourseService courseService;

    private final StudentService studentService;

    private final CoursePreSelectService coursePreSelectService;

    @Autowired
    public StudentController(CourseService courseService,
                             StudentService studentService,
                             CoursePreSelectService coursePreSelectService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.coursePreSelectService = coursePreSelectService;
    }

    @GetMapping("/course_stats/students/{username}")
    @ApiOperation(value = "选课学分统计", notes = "获取某学生的选课学分统计信息")
    @ApiImplicitParam(name = "username", value = "学工号", required = true, dataType = "string", paramType = "path")
    public ResponseEntity<List<CourseSelectStatsVO>> getCourseSelectedStats(@PathVariable("username") String username) {
        return ResponseEntity.ok(studentService.getCoursesSelectedStats(username));
    }

    @PostMapping("/course_preselect")
    @ApiOperation(value = "添加预选课")
    @ApiImplicitParam(name = "coursePreselect", value = "课程信息", required = true, dataType = "CoursePreselect")
    public ResponseEntity addCoursePreselect(@RequestBody CoursePreselect coursePreselect) {
        String username = coursePreselect.getUsername();
        String courseCode = coursePreselect.getCourseCode();
        return coursePreSelectService.addCoursePreselect(username, courseCode);
    }

    @PatchMapping("/pre_courses/{courseCode}")
    @ApiOperation(value = "将预选课加入预选课课表/删除预选课课表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseCode", value = "课程编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "addToTable", value = "是否加入预选课课表", required = true, dataType = "boolean")
    })
    public ResponseEntity DeleteCoursePreselected(@PathVariable String courseCode, String username, boolean addToTable) {
        return coursePreSelectService.modifyAddToTable(courseCode, username, addToTable);
    }


    @PostMapping("/course_select")
    @ApiOperation(value = "课程选择")
    @ApiImplicitParam(name = "courseSelect", value = "课程信息", required = true, dataType = "CourseSelect")
    public ResponseEntity addCourseSelect(@RequestBody CourseSelect courseSelect) {
        String username = courseSelect.getUsername();
        String courseCode = courseSelect.getCourseCode();
        return courseService.addCourseSelect(username, courseCode);
    }

    @DeleteMapping("/pre_courses/{courseCode}")
    @ApiOperation(value = "删除预选课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseCode", value = "课程编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String")
    })
    public ResponseEntity DeleteCoursePreselected(@PathVariable String courseCode, String username) {
        return coursePreSelectService.DeleteCoursePreselected(courseCode, username);
    }

    @DeleteMapping("/course/{courseCode}")
    @ApiOperation(value = "学生退课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseCode", value = "课程编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String")
    })
    public ResponseEntity DeleteCourseSelected(@PathVariable String courseCode, String username) {
        return courseService.deleteCourseSelected(courseCode, username);
    }

    @GetMapping("/pre_course/students/{username}")
    @ApiOperation(value = "查看所有预选课")
    @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity DeleteCoursePreselected(@PathVariable String username) {
        return coursePreSelectService.getPreselectedCourses(username);
    }
}
