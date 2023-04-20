package platform.messagingservice.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import platform.messagingservice.api.utils.UserTestBuilder;
import sat.recruitment.api.SatRecruitmentApplication;
import sat.recruitment.api.controller.SatRecruitmentController;
import sat.recruitment.api.exceptions.IllegalArgumentException;
import sat.recruitment.api.model.User;
import sat.recruitment.api.service.UserService;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = SatRecruitmentApplication.class)
@WebMvcTest(SatRecruitmentController.class)
class SatRecruitmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SatRecruitmentController userController;

    @MockBean
    UserService userService;

    @Test
    void createUserOk() throws Exception {
        User user = new UserTestBuilder().build();
        createTest(201, user);
    }

    @Test
    void createUserWithoutName() throws Exception {
        User user = new UserTestBuilder()
                .withOutName()
                .build();
        createTest(400, user);
    }

    @Test
    void createUserWithoutEmail() throws Exception {
        User user = new UserTestBuilder()
                .withOutEmail()
                .build();
        createTest(400, user);
    }

    @Test
    void createUserWithoutAddress() throws Exception {
        User user = new UserTestBuilder()
                .withOutAddress()
                .build();
        createTest(400, user);
    }

    @Test
    void createUserWithoutPhone() throws Exception {
        User user = new UserTestBuilder()
                .withOutPhone()
                .build();
        createTest(400, user);
    }

    @Test
    void createUserDuplicated() throws Exception {
        User user = new UserTestBuilder()
                .withOutPhone()
                .build();
        doThrow(new IllegalArgumentException("User is duplicated")).when(userService).validate(user);
        createTest(400, user);
    }

    private void createTest(int status, User user) throws Exception {
        MvcResult mvcResult = createApiCall(user);
        assertEquals(status, mvcResult.getResponse().getStatus());
    }

    private MvcResult createApiCall(User user) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        return mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/api/v1/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

}

