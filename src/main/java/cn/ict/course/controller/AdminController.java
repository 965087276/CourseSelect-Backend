package cn.ict.course.controller;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CourseService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/admin")
@Slf4j
public class AdminController {
    private final CourseService courseService;

    @Autowired
    public AdminController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/enabletimes_edit")
    @ApiOperation(value = "管理员编辑选课时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "选课开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "选课结束时间", required = true, dataType = "String")
    })
    public ResponseEntity DeleteCoursePreselected(String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = format.parse(start);
            Date endTime = format.parse(end);
            log.info(startTime.toString());
            log.info(endTime.toString());
            return courseService.updateEnableTime(startTime, endTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "输入日期不合法");
        }
    }
}
