package cn.ict.course.controller;

import cn.ict.course.entity.db.SelectionControl;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CourseSelectService;
import cn.ict.course.service.CourseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public AdminController(CourseService courseService, CourseSelectService courseSelectService) {
        this.courseService = courseService;
        this.courseSelectService = courseSelectService;
    }

    @PostMapping("/enabletimes_edit")
    @ApiOperation(value = "管理员编辑选课时间")
    @ApiImplicitParam(name = "selectionControl", value = "选课结束时间", required = true, dataType = "SelectionControl")
    public ResponseEntity DeleteCoursePreselected(@RequestBody SelectionControl selectionControl) {
        Date startTime = selectionControl.getStartTime();
        Date endTime = selectionControl.getEndTime();
        return courseSelectService.updateEnableTime(startTime, endTime);
    }
}
