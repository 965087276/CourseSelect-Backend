package cn.ict.course.service.impl;

import cn.ict.course.entity.db.CourseSelect;
import cn.ict.course.entity.db.User;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.service.CourseSelectService;
import cn.ict.course.service.UserService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
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
public class CourseSelectServiceImplTest {

    @Autowired
    CourseSelectService courseSelectService;

    @Autowired
    UserService userService;

    private final int STATUS_OK = 200;
    private final int STATUS_ERROR = 500;

    private User testUser = JSONObject.parseObject("{\n" +
            "  \"college\": \"计算机科学与技术学院\",\n" +
            "  \"email\": \"test@test.com\",\n" +
            "  \"phoneNumber\": \"test_phone\",\n" +
            "  \"realName\": \"test_user\",\n" +
            "  \"role\": \"student\",\n" +
            "  \"password\": \"123456\",\n" +
            "  \"username\": \"test_user123456\"\n" +
            "}", User.class);

    @Before
    public void setUp() throws Exception {
        ResponseEntity responseEntity = userService.save(testUser);
        assertEquals(STATUS_OK, responseEntity.getStatus());
    }

    @After
    public void tearDown() throws Exception {
        ResponseEntity deleteResponse = userService.deleteUserByUsername(testUser.getUsername());
        assertEquals(STATUS_OK, deleteResponse.getStatus());
    }

    @Test
    public void addCourseSelect() {
    }

    @Test
    public void deleteCourseSelected() {
    }

    @Test
    public void updateEnableTime() {
    }

    @Test
    public void getEnableTime() {
    }

    @Test
    public void getCourseSelectStatus() {
    }

    @Test
    public void getCurriculum() {
    }

    @Test
    public void getSelectedCourseCodesByUsername() {
    }

    @Test
    public void getSelectedCourses() {
    }

    @Test
    public void getStudentInfoByCourseCode() {
    }

    @Test
    public void getStudentGradesByUsername() {
    }

    @Test
    public void updateStudentGrades() {
    }

    @Test
    public void updateStudentGradesByFile() {
    }

    @Test
    public void getConflictedCourseCode() {
    }
}