package cn.ict.course.controller;

import cn.ict.course.service.UserService;
import cn.ict.course.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static cn.ict.course.constants.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //支持数据回滚，避免测试数据污染环境
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserController userController;

    private MockMvc mvc;
    private MockHttpSession mockHttpSession;

    @Before
    public void setupMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void login() throws Exception {
        mvc.perform(post("/xk/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_USERNAME_AND_PWD_RIGHT))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
//        mvc.perform(post("/xk/api/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(TEST_USERNAME_AND_PWD_ERROR))
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
//                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void save() throws Exception {
        mvc.perform(post("/xk/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_USER_INFO))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void detail() {
    }
}