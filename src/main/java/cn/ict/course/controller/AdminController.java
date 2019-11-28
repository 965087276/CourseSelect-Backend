package cn.ict.course.controller;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.AdminService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Jianyong Feng
 **/
@RestController
@RequestMapping(value = "/xk/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/upload")
    @RequiresRoles(value = "admin")
    @ApiOperation(value = "上传Excel添加课程", notes = "Excel需要符合课程规范")
    @ApiImplicitParam(name = "file", value = "上传的excel", required = true, dataType = "MultipartFile")
    public ResponseEntity excelUpload(@RequestParam("file") MultipartFile file) throws IOException {
        adminService.saveByExcel(file);
        return ResponseEntity.ok();
    }

}
