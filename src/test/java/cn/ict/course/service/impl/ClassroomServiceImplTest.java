package cn.ict.course.service.impl;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.ClassroomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class ClassroomServiceImplTest {

    @Autowired
    ClassroomService classroomService;

    private int STATUS_OK = 200;
    private int STATUS_ERROR = 500;

    @Test
    public void getAllClassrooms() {
        ResponseEntity response = classroomService.getAllClassrooms();
        assertEquals(STATUS_OK, response.getStatus());
    }
}