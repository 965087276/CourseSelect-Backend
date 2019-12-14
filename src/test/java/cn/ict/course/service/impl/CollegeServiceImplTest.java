package cn.ict.course.service.impl;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CollegeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class CollegeServiceImplTest {

    @Autowired
    CollegeService collegeService;

    private int STATUS_OK = 200;
    private int STATUS_ERROR = 500;

    @Test
    public void getCollegeList() {
        List<String> colleges = collegeService.getCollegeList();
        log.info(colleges.toString());
        assertNotNull(colleges);
    }
}