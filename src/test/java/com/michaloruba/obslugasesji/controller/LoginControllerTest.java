package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private MockMvc mvc;

    @Test
    public void givenRequestOnLoginControllerShowMyLoginPage_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/showMyLoginPage"))
                .andExpect(status().isOk());
    }
    @Test
    public void givenRequestOnLoginControllerGetAccessDenied_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/access-denied"))
                .andExpect(status().isOk());
    }
    @Test
    public void givenRequestOnLoginControllerPostAccessDenied_shouldSucceedWith200() throws Exception {
        mvc.perform(post("/access-denied")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}