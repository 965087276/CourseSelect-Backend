package cn.ict.course.service.impl;

import cn.ict.course.entity.bo.UserUpdateInfo;
import cn.ict.course.entity.db.User;
import cn.ict.course.entity.dto.LoginDTO;
import cn.ict.course.entity.dto.UserUpdateDTO;
import cn.ict.course.entity.http.ResponseEntity;
import cn.ict.course.entity.vo.LoginVO;
import cn.ict.course.entity.vo.UserDetailVO;
import cn.ict.course.service.CourseService;
import cn.ict.course.service.UserService;
import com.alibaba.fastjson.JSONObject;
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
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    private int STATUS_OK = 200;
    private int STATUS_ERROR = 500;

    private User testUser = JSONObject.parseObject("{\n" +
            "  \"college\": \"计算机科学与技术学院\",\n" +
            "  \"email\": \"test@test.com\",\n" +
            "  \"phoneNumber\": \"testPhoneNumber\",\n" +
            "  \"realName\": \"testUserRealName\",\n" +
            "  \"role\": \"student\",\n" +
            "  \"username\": \"testUsername\"\n" +
            "}", User.class);

    @Test
    public void login() {
        String username = "201902";
        String password = "123456";
        LoginDTO loginDTO = new LoginDTO(username, password);
        LoginVO loginVO = userService.login(loginDTO);
        assertEquals(STATUS_OK, loginVO.getCode().intValue());
    }

    @Test
    public void save() {
        ResponseEntity responseEntity = userService.save(testUser);
        assertEquals(STATUS_OK, responseEntity.getStatus());
        String username = "testUsername";
        String password = "ername";
        LoginDTO loginDTO = new LoginDTO(username, password);
        LoginVO loginVO = userService.login(loginDTO);
        assertEquals(STATUS_OK, loginVO.getCode().intValue());
    }

    @Test
    public void detail() {
        String username = "201902";
        UserDetailVO userDetailVO = userService.detail(username);
        assertEquals("201902", userDetailVO.getUsername());
    }

    @Test
    public void deleteUserByUsername() {
        ResponseEntity saveResponse = userService.save(testUser);
        assertEquals(STATUS_OK, saveResponse.getStatus());
        ResponseEntity deleteResponse = userService.deleteUserByUsername(testUser.getUsername());
        assertEquals(STATUS_OK, deleteResponse.getStatus());
        UserDetailVO userDetailVO = userService.detail(testUser.getUsername());
        assertNull(userDetailVO);
    }

    @Test
    public void updateUserInfo() {
        ResponseEntity saveResponse = userService.save(testUser);
        assertEquals(STATUS_OK, saveResponse.getStatus());
        UserUpdateInfo updateInfo = new UserUpdateInfo();
        updateInfo.setCollege("计算机科学与技术学院");
        updateInfo.setEmail("test2@test.com");
        updateInfo.setPhoneNumber("testPhone");
        ResponseEntity updateResponse = userService.updateUserInfo(testUser.getUsername(), updateInfo);
        assertEquals(STATUS_OK, updateResponse.getStatus());
    }

    @Test
    public void getAllUsersExceptAdmin() {
        ResponseEntity responseEntity = userService.getAllUsersExceptAdmin(
                "201902",
                "",
                "",
                "student",
                1,
                5
        );
        assertEquals(STATUS_OK, responseEntity.getStatus());
    }

    @Test
    public void repeatByUsername() {
        ResponseEntity saveResponse = userService.save(testUser);
        assertEquals(STATUS_OK, saveResponse.getStatus());
        boolean isRepeat = userService.repeatByUsername(testUser.getUsername());
        assertTrue(isRepeat);
        ResponseEntity saveResponse2 = userService.save(testUser);
        assertEquals(STATUS_ERROR, saveResponse2.getStatus());

    }

    @Test
    public void updateUser() {
        String newPassword = "newPassword";
        String oldPassword = "ername";
        ResponseEntity saveResponse = userService.save(testUser);
        assertEquals(STATUS_OK, saveResponse.getStatus());
        UserUpdateDTO update = new UserUpdateDTO();
        update.setCollege("计算机科学与技术学院");
        update.setEmail("test2@test.com");
        update.setPhoneNumber("testPhone");
        update.setOldPassword("wrong old password");
        update.setUsername(testUser.getUsername());
        update.setRealName(testUser.getRealName());
        update.setPassword(newPassword);
        ResponseEntity updateResponse = userService.updateUser(update);
        assertEquals(STATUS_ERROR, updateResponse.getStatus());

        update.setOldPassword(oldPassword);
        ResponseEntity updateResponse2 = userService.updateUser(update);
        log.info(update.getUsername());
        log.info(update.getPassword());
        log.info(update.getOldPassword());
        assertEquals("OK", updateResponse2.getMessage());
        assertEquals(STATUS_OK, updateResponse2.getStatus());

        LoginDTO loginDTO = new LoginDTO(update.getUsername(), newPassword);
        LoginVO loginVO = userService.login(loginDTO);
        assertEquals(STATUS_OK, loginVO.getCode().intValue());
    }
}