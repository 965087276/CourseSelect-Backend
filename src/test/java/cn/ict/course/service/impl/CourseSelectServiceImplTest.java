package cn.ict.course.service.impl;

import cn.ict.course.constants.CourseConflictConst;
import cn.ict.course.entity.bo.GradesInfoBO;
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

    private final String MESSAGE_OK = "OK";
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
    public void setUp() {
        log.info("Test Start");
        ResponseEntity responseEntity = userService.save(testUser);
        assertEquals(STATUS_OK, responseEntity.getStatus());
        courseSelectService.deleteCourseSelected(testCourseCode, testUsername);
    }

    @After
    public void tearDown() {
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
        assertEquals(MESSAGE_OK, responseCurriculum.getMessage().toString());
        log.info(responseCurriculum.getBody().toString());
        assertTrue(responseCurriculum.getBody().toString().contains(testCourseCode));
        assertEquals(STATUS_OK, responseCurriculum.getStatus());
    }

    @Test
    public void deleteCourseSelected() {
        ResponseEntity response = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals(MESSAGE_OK, response.getMessage().toString());
        assertEquals(STATUS_OK, response.getStatus());
        ResponseEntity responseDelete = courseSelectService.deleteCourseSelected(testCourseCode, testUsername);
        assertEquals(MESSAGE_OK, responseDelete.getMessage().toString());
        assertEquals(STATUS_OK, responseDelete.getStatus());
    }

    @Test
    public void updateEnableTime() {

    }

    @Test
    public void getEnableTime() {
        ResponseEntity response = courseSelectService.getEnableTime();
        assertEquals(MESSAGE_OK, response.getMessage().toString());
        log.info(response.getBody().toString());
        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void getCourseSelectStatus() {
        ResponseEntity response =courseSelectService.getCourseSelectStatus();
        assertEquals(MESSAGE_OK, response.getMessage().toString());
        log.info(response.getBody().toString());
        assertEquals(STATUS_OK, response.getStatus());
    }

    @Test
    public void getCurriculum() {

        ResponseEntity responseAdd = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        log.info(responseAdd.getMessage().toString());
        assertEquals(STATUS_OK, responseAdd.getStatus());

        ResponseEntity responseAdd2 = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        log.info(responseAdd2.getMessage().toString());
        assertEquals(STATUS_ERROR, responseAdd2.getStatus());

        ResponseEntity responseCurriculum = courseSelectService.getCurriculum(testUsername);
        assertEquals(MESSAGE_OK, responseCurriculum.getMessage().toString());
        log.info(responseCurriculum.getBody().toString());
        assertEquals(STATUS_OK, responseCurriculum.getStatus());
        assertTrue(responseCurriculum.getBody().toString().contains(testCourseCode));
    }

    @Test
    public void getSelectedCourseCodesByUsername() {
        ResponseEntity responseAdd = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals(MESSAGE_OK, responseAdd.getMessage().toString());
        log.info(responseAdd.getMessage().toString());
        assertEquals(STATUS_OK, responseAdd.getStatus());

        ResponseEntity responseGetCourseCode = courseSelectService.getSelectedCourseCodesByUsername(testUsername);
        assertEquals(STATUS_OK, responseGetCourseCode.getStatus());
        assertTrue(responseGetCourseCode.getBody().toString().contains(testCourseCode));
        assertEquals(MESSAGE_OK, responseGetCourseCode.getMessage().toString());
        log.info(responseGetCourseCode.getBody().toString());
    }

    @Test
    public void getSelectedCourses() {
        ResponseEntity responseAdd = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals(MESSAGE_OK, responseAdd.getMessage().toString());
        log.info(responseAdd.getMessage().toString());
        assertEquals(STATUS_OK, responseAdd.getStatus());

        ResponseEntity response = courseSelectService.getSelectedCourses(testUsername);
        assertEquals(STATUS_OK, response.getStatus());
        assertTrue(response.getBody().toString().contains(testCourseCode));
        assertEquals(MESSAGE_OK, response.getMessage().toString());
        log.info(response.getBody().toString());
    }

    @Test
    public void getStudentInfoByCourseCode() {
        ResponseEntity responseAdd = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals(MESSAGE_OK, responseAdd.getMessage().toString());
        log.info(responseAdd.getMessage().toString());
        assertEquals(STATUS_OK, responseAdd.getStatus());

        ResponseEntity response = courseSelectService.getStudentInfoByCourseCode(testCourseCode);
        assertEquals(STATUS_OK, response.getStatus());
        assertTrue(response.getBody().toString().contains(testUsername));
        assertEquals(MESSAGE_OK, response.getMessage().toString());
        log.info(response.getBody().toString());
    }

    @Test
    public void getStudentGradesByUsername() {
        ResponseEntity responseAdd = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals(MESSAGE_OK, responseAdd.getMessage().toString());
        log.info(responseAdd.getMessage().toString());
        assertEquals(STATUS_OK, responseAdd.getStatus());

        ResponseEntity response = courseSelectService.getStudentGradesByUsername(testUsername);
        assertEquals(STATUS_ERROR, response.getStatus());
    }

    @Test
    public void updateStudentGrades() {

        ResponseEntity responseAdd = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        assertEquals(MESSAGE_OK, responseAdd.getMessage().toString());
        log.info(responseAdd.getMessage().toString());
        assertEquals(STATUS_OK, responseAdd.getStatus());

        ResponseEntity responseAdd2 = courseSelectService.addCourseSelect(testUsername, testCourseCode);
        log.info(responseAdd2.getMessage().toString());
        assertEquals(STATUS_ERROR, responseAdd2.getStatus());

        GradesInfoBO gradesInfo = new GradesInfoBO();
        gradesInfo.setCourseCode(testCourseCode);
        gradesInfo.setStudentId(testUsername);
        gradesInfo.setGrade(90.0);

        ResponseEntity response2 = courseSelectService.updateStudentGrades(gradesInfo);
        log.info("response2: " + response2.getMessage().toString());
        assertEquals(STATUS_OK, response2.getStatus());

    }

    @Test
    public void updateStudentGradesByFile() {

    }

    @Test
    public void getConflictedCourseCode() {
//        String conflicted = courseSelectService.getConflictedCourseCode(testCourseCode, testUsername);
//        assertEquals(CourseConflictConst.NO_COURSE_SCHEDULE_CONFLICT);
    }
}