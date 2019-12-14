package cn.ict.course.service.impl;

import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CoursePreselectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CoursePreselectServiceImplTest {

    @Autowired
    CoursePreselectService coursePreselectService;

    private int STATUS_OK = 200;
    private int STATUS_ERROR = 500;

    private String testCourseCode = "UCAS6VY1U096628";
    private String testUsername = "201902";

    @Test
    @Transactional
    public void addCoursePreselect() {
        ResponseEntity response = coursePreselectService.addCoursePreselect(testUsername, testCourseCode);
        log.info(response.getMessage().toString());
        assertEquals(STATUS_OK, response.getStatus());
        ResponseEntity response2 = coursePreselectService.addCoursePreselect(testUsername, testCourseCode);
        coursePreselectService.deleteCoursePreselected(testCourseCode, testUsername);
        assertEquals(STATUS_ERROR, response2.getStatus());
    }

    @Test
    public void deleteCoursePreselected() {
        ResponseEntity response = coursePreselectService.addCoursePreselect(testUsername, testCourseCode);
        assertEquals(STATUS_OK, response.getStatus());
        ResponseEntity deleteResponse = coursePreselectService.deleteCoursePreselected(testCourseCode, testUsername);
        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void getPreselectedCourses() {
        ResponseEntity response = coursePreselectService.getPreselectedCourses(testUsername);
        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void repeatByUsernameAndCourseCode() {
        boolean repeat = coursePreselectService.repeatByUsernameAndCourseCode(testUsername, testCourseCode);
        assertFalse(repeat);
        ResponseEntity response = coursePreselectService.addCoursePreselect(testUsername, testCourseCode);
        assertEquals(STATUS_OK, response.getStatus());
        boolean repeat2 = coursePreselectService.repeatByUsernameAndCourseCode(testUsername, testCourseCode);
        assertTrue(repeat2);
    }

    @Test
    public void modifyAddToTable() {
    }

    @Test
    @Transactional
    public void getPreselectedCourseCodeByUsername() {
        ResponseEntity response = coursePreselectService.getPreselectedCourseCodeByUsername(testUsername);
        assertEquals(STATUS_OK, response.getStatus());
        ResponseEntity responseAdd = coursePreselectService.addCoursePreselect(testUsername, testCourseCode);
        assertEquals(STATUS_OK, responseAdd.getStatus());
    }

    @After
    public void afterTest() {
        ResponseEntity deleteResponse = coursePreselectService.deleteCoursePreselected(testCourseCode, testUsername);
    }
}