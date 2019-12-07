package cn.ict.course.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //支持数据回滚，避免测试数据污染环境
public class CourseServiceImplTest {

    @Test
    public void addCourse() {

    }

    @Test
    public void getCourseList() {
    }

    @Test
    public void deleteCourseByCourseCode() {
    }
}