package cn.ict.course.controller;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CollegeService;
import io.swagger.annotations.ApiOperation;
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
public class CollegeController {

    private final CollegeService collegeService;

    @Autowired
    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @GetMapping(value = "/colleges")
    @ApiOperation(value = "获取所有学院")
    ResponseEntity getCollegeList() {
        List<String> colleges = collegeService.findAllColleges();
        return ResponseEntity.ok(colleges);
    }
}
