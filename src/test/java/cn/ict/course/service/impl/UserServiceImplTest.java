package cn.ict.course.service.impl;

import cn.ict.course.service.CourseService;
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
public class UserServiceImplTest {

    @Autowired
    CourseService courseService;

    @Test
    public void login() {
//        courseService
    }

    @Test
    public void save() {
    }

    @Test
    public void detail() {
    }

    @Test
    public void deleteUserByUsername() {
    }

    @Test
    public void updateUserInfo() {
    }

    @Test
    public void getAllUsersExceptAdmin() {
    }

    @Test
    public void repeatByUsername() {
    }

    @Test
    public void updateUser() {
    }
}