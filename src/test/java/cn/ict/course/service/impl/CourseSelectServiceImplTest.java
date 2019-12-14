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

    private final String testUsername = testUser.getUsername();
    private final String testCourseCode = "UCASO5KBI075388";
    private final String startTime = "2019-12-14T07:40:05.053Z";
    private final String endTime = "2019-12-14T07:40:05.052Z";

    @Before
    public void setUp() throws Exception {
        log.info("Test Start");
        ResponseEntity responseEntity = userService.save(testUser);
        assertEquals(STATUS_OK, responseEntity.getStatus());
        courseSelectService.deleteCourseSelected(testCourseCode, testUsername);
    }

    @After
    public void tearDown() throws Exception {
        ResponseEntity deleteResponse = userService.deleteUserByUsername(testUser.getUsername());
        assertEquals(STATUS_OK, deleteResponse.getStatus());
        log.info("Test End");
    }

    @Test
    public void addCourseSelect() {
        ResponseEntity response = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        log.info(response.getMessage().toString());
        assertEquals(STATUS_OK, response.getStatus());

        ResponseEntity response2 = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals(STATUS_ERROR, response2.getStatus());

        ResponseEntity responseCurriculum = courseSelectService.getCurriculum(testUsername);
        assertEquals("OK", responseCurriculum.getMessage().toString());
        log.info(responseCurriculum.getBody().toString());
        assertTrue(responseCurriculum.getBody().toString().contains(testCourseCode));
        assertEquals(STATUS_OK, responseCurriculum.getStatus());
    }

    @Test
    public void deleteCourseSelected() {
        ResponseEntity response = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals("OK", response.getMessage().toString());
        assertEquals(STATUS_OK, response.getStatus());
        ResponseEntity responseDelete = courseSelectService.deleteCourseSelected(testCourseCode, testUsername);
        assertEquals("OK", responseDelete.getMessage().toString());
        assertEquals(STATUS_OK, responseDelete.getStatus());
    }

    @Test
    public void updateEnableTime() {

    }

    @Test
    public void getEnableTime() {
        ResponseEntity response = courseSelectService.getEnableTime();
        assertEquals("OK", response.getMessage().toString());
        log.info(response.getBody().toString());
        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void getCourseSelectStatus() {
        ResponseEntity response =courseSelectService.getCourseSelectStatus();
        assertEquals("OK", response.getMessage().toString());
        log.info(response.getBody().toString());
        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void getCurriculum() {
        ResponseEntity response = courseSelectService.getCurriculum(testUsername);
        assertEquals("OK", response.getMessage().toString());
        log.info(response.getBody().toString());
        assertEquals(STATUS_OK, response.getStatus());

        ResponseEntity responseAdd = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        log.info(responseAdd.getMessage().toString());
        assertEquals(STATUS_OK, responseAdd.getStatus());

        ResponseEntity responseAdd2 = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        log.info(responseAdd2.getMessage().toString());
        assertEquals(STATUS_ERROR, responseAdd2.getStatus());

        ResponseEntity responseCurriculum = courseSelectService.getCurriculum(testUsername);
        assertEquals("OK", responseCurriculum.getMessage().toString());
        log.info(responseCurriculum.getBody().toString());
        assertEquals(STATUS_OK, responseCurriculum.getStatus());
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