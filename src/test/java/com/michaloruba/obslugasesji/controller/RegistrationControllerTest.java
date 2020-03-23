package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.service.UserService;
import com.michaloruba.obslugasesji.user.CrmUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {
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
    public void showRegistrationForm_ShouldAddEmptyCrmUserToModelAndRenderRegisterForm() throws Exception {
        mvc.perform(get("/register/showRegistrationForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"))
                .andExpect(model().attribute("crmUser", hasProperty("userName", nullValue())));
    }

    @Test
    public void processRegistrationForm_ShouldSaveUserAndRedirectToLoginPage() throws Exception {
        doNothing().when(userService).save(isA(CrmUser.class));
        when(userService.findByUserName("test")).thenReturn(null);

        mvc.perform(post("/register/processRegistrationForm")
                    .with(csrf())
                    .param("userName", "test")
                    .param("password", "test")
                    .param("matchingPassword", "test")
                    .param("firstName", "test")
                    .param("lastName", "test")
                    .param("email", "test@gmail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("fancy-login"));
        verify(userService, times(1)).save(isA(CrmUser.class));
        verify(userService, times(1)).findByUserName("test");
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void processRegistrationForm_ShouldRedirectToRegistration_WhenUserAlreadyExists() throws Exception {
        doNothing().when(userService).save(isA(CrmUser.class));
        when(userService.findByUserName("test")).thenReturn(new User());

        mvc.perform(post("/register/processRegistrationForm")
                    .with(csrf())
                    .param("userName", "test")
                    .param("password", "test")
                    .param("matchingPassword", "test")
                    .param("firstName", "test")
                    .param("lastName", "test")
                    .param("email", "test@gmail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"));
        verify(userService, times(1)).findByUserName("test");
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void processRegistrationFormWithErrors_ShouldRedirectToRegistration() throws Exception {
        mvc.perform(post("/register/processRegistrationForm")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("crmUser", 7))
                .andExpect(model().attributeHasFieldErrors("crmUser", "userName"))
                .andExpect(model().attributeHasFieldErrors("crmUser", "matchingPassword"))
                .andExpect(model().attributeHasFieldErrors("crmUser", "password"))
                .andExpect(model().attributeHasFieldErrors("crmUser", "firstName"))
                .andExpect(model().attributeHasFieldErrors("crmUser", "lastName"))
                .andExpect(model().attributeHasFieldErrors("crmUser", "email"))
                .andExpect(view().name("registration-form"));
        verifyNoInteractions(userService);
    }

    @Test
    public void requestedOnRegistration_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/register/showRegistrationForm"))
                .andExpect(status().isOk());
        mvc.perform(post("/register/processRegistrationForm").with(csrf()))
                .andExpect(status().isOk());
    }
}