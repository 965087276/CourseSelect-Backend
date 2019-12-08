package cn.ict.course.controller;

import cn.ict.course.entity.db.CoursePreselect;
import cn.ict.course.entity.db.CourseSelect;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.CourseSelectStatsVO;
import cn.ict.course.entity.vo.CurriculumVO;
import cn.ict.course.service.CoursePreselectService;
import cn.ict.course.service.CourseSelectService;
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


    private final StudentService studentService;

    private final CoursePreselectService coursePreSelectService;

    private final CourseSelectService courseSelectService;

    @Autowired
    public StudentController(StudentService studentService,
                             CoursePreselectService coursePreSelectService,
                             CourseSelectService courseSelectService) {
        this.studentService = studentService;
        this.coursePreSelectService = coursePreSelectService;
        this.courseSelectService = courseSelectService;
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
    public ResponseEntity modifyAddToTable(@PathVariable String courseCode, String username, boolean addToTable) {
        return coursePreSelectService.modifyAddToTable(courseCode, username, addToTable);
    }


    @PostMapping("/course_select")
    @ApiOperation(value = "课程选择")
    @ApiImplicitParam(name = "courseSelect", value = "课程信息", required = true, dataType = "CourseSelect")
    public ResponseEntity addCourseSelect(@RequestBody CourseSelect courseSelect) {
        String username = courseSelect.getUsername();
        String courseCode = courseSelect.getCourseCode();
        return courseSelectService.addCourseSelect(username, courseCode);
    }

    @DeleteMapping("/pre_courses/{courseCode}")
    @ApiOperation(value = "删除预选课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseCode", value = "课程编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String")
    })
    public ResponseEntity deleteCoursePreselected(@PathVariable String courseCode, String username) {
        return coursePreSelectService.deleteCoursePreselected(courseCode, username);
    }

    @DeleteMapping("/course/{courseCode}")
    @ApiOperation(value = "学生退课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseCode", value = "课程编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String")
    })
    public ResponseEntity deleteCourseSelected(@PathVariable String courseCode, String username) {
        return courseSelectService.deleteCourseSelected(courseCode, username);
    }

    @GetMapping("/course/students/{username}")
    @ApiOperation(value = "获取已选课程信息", notes = "使用学生用户名查询")
    public ResponseEntity getSelectedCourses(@PathVariable String username) {
        return courseSelectService.getSelectedCourses(username);
    }

    @GetMapping("/pre_course/students/{username}")
    @ApiOperation(value = "查看所有预选课")
    @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity getPreselectedCourses(@PathVariable String username) {
        return coursePreSelectService.getPreselectedCourses(username);
    }

    @GetMapping("/pre_course_code/students/{username}")
    @ApiOperation(value = "获取所有预选课程的课程编码", notes = "该学生用户名对应的所有预选课课程编码")
    @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity<List<String>> getPreselectedCourseCodeByUsername(@PathVariable String username) {
        return coursePreSelectService.getPreselectedCourseCodeByUsername(username);
    }

    @GetMapping("/course_code/students/{username}")
    @ApiOperation(value = "获取所有已选课程的课程编码", notes = "该学生用户名对应的所有已选课程的课程编码")
    @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity<List<String>> getSelectedCourseCodesByUsername(@PathVariable String username) {
        return courseSelectService.getSelectedCourseCodesByUsername(username);
    }

    @GetMapping("/courses_table/students/{username}")
    @ApiOperation(value = "获取学生课表信息")
    @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity<List<CurriculumVO>> getCurriculum(@PathVariable String username) {
        return courseSelectService.getCurriculum(username);
    }
    @GetMapping("/course_grade/students/{username}")
    @ApiOperation(value = "获取学生成绩", notes = "使用学生用户名查询")
    @ApiImplicitParam(name = "username", value = "学生用户名", required = true, dataType = "String", paramType = "path")
    public ResponseEntity getStudentGradesByUsername(@PathVariable String username) {
        return courseSelectService.getStudentGradesByUsername(username);
    }
}
