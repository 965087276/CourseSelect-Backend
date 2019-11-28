package cn.ict.course.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.course.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jianyong Feng
 **/
@Service
public class AdminServiceImpl implements AdminService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByExcel(MultipartFile file) throws IOException {
        InputStream inputStream =  file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);



    }
}
