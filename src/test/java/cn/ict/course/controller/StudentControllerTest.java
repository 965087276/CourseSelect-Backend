package cn.ict.course.controller;

import cn.ict.course.entity.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private MockHttpSession session;

    @Before
    public void setUpMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession();
    }

    @Test
    public void getCourseSelectedStats() {
//        String

    }

    @Test
    public void addCoursePreselect() {

    }

    @Test
    public void addCourseSelect() {
    }

    @Test
    public void deleteCoursePreselected() {
    }

    @Test
    public void deleteCourseSelected() {
    }

    @Test
    public void deleteCoursePreselected1() {
    }
}