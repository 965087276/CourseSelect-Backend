package cn.ict.course.service.impl;

import cn.ict.course.entity.db.Course;
import cn.ict.course.entity.dto.CourseDTO;
import cn.ict.course.entity.dto.ScheduleDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.repo.CourseRepo;
import cn.ict.course.repo.CourseScheduleRepo;
import cn.ict.course.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CourseServiceImplTest {

    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    CourseScheduleRepo courseScheduleRepo;


    @Test
    @Transactional
    public void addCourse() {
        ScheduleDTO schedule = new ScheduleDTO(2,
                20,
                2,
                3,
                "教1-110"
        );
        List<ScheduleDTO> schedules = new ArrayList<>();
//        schedules.add(schedule);
        CourseDTO course = new CourseDTO("201904",
                "黄",
                "模式识别与机器学习",
                "专业核心课", 60,
                3,
                300,
                "计算机科学与技术学院",
                schedules);
//        ResponseEntity responseEntity = courseService.addCourse(course);
//        assertEquals("OK", responseEntity.getMessage());
//        assertEquals(200, responseEntity.getStatus());
//        assertEquals(1, courseRepo.findByTeacherId("201904").size());
    }

    @Test
    public void getCourseList() {
        ResponseEntity responseEntity = courseService.getCourseList("null",
                "null", "null", -1, -1);
        assertEquals(200, responseEntity.getStatus());
    }

    @Test
    public void deleteCourseByCourseCode() {
        ResponseEntity responseEntity = courseService.deleteCourseByCourseCode("UCASM65DI344216");
        assertEquals(200, responseEntity.getStatus());
        assertEquals("OK", responseEntity.getMessage());
    }
}