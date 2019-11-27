package cn.ict.course.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Jianyong Feng
 **/
public interface AdminService {
    void saveByExcel(MultipartFile file) throws IOException;
}
