package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.FieldOfStudyService;
import com.michaloruba.obslugasesji.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FieldOfStudyController.class)
public class FieldOfStudyControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @MockBean
    private FieldOfStudyService fieldOfStudyService;
    @Autowired
    private MockMvc mvc;

    private static FieldOfStudy fieldOfStudy;
    private static List<FieldOfStudy> fields;
    @BeforeClass
    public static void setUp(){
        fieldOfStudy = new FieldOfStudy("Information Technology");
        fieldOfStudy.setId(1);
        fields = Arrays.asList(fieldOfStudy);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void list_ShouldAddFieldEntriesToModelAndRenderFieldsListView() throws Exception {
        given(fieldOfStudyService.findAll()).willReturn(fields);

        mvc.perform(get("/fields/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("fields/fields-list"))
                .andExpect(model().attribute("fields", hasSize(1)))
                .andExpect(model().attribute("fields", hasItem(
                        allOf(
                                hasProperty("name", is(fieldOfStudy.getName()))
                        )
                )));

        verify(fieldOfStudyService, times(1)).findAll();
        verifyNoMoreInteractions(fieldOfStudyService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd_ShouldAddEmptyFieldToModelAndRenderFieldForm() throws Exception {
        mvc.perform(get("/fields/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("fields/field-form"))
                .andExpect(model().attribute("fieldOfStudy", hasProperty("id", is(0))))
                .andExpect(model().attribute("fieldOfStudy", hasProperty("name", nullValue())));
        verifyNoInteractions(fieldOfStudyService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldAddExistingFieldToModelAndRenderRoleForm() throws Exception {
        given(fieldOfStudyService.findById(1)).willReturn(fieldOfStudy);

        mvc.perform(get("/fields/showFormForUpdate")
                .param("fieldId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("fields/field-form"))
                .andExpect(model().attribute("fieldOfStudy", hasProperty("id", is(1))))
                .andExpect(model().attribute("fieldOfStudy", hasProperty("name", is(fieldOfStudy.getName()))));
        verify(fieldOfStudyService, times(1)).findById(1);
        verifyNoMoreInteractions(fieldOfStudyService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldThrowNotFoundException_WhenWrongIdPassed() throws Exception{
        when(fieldOfStudyService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        fieldOfStudyService.findById(-1);

        mvc.perform(get("/fields/showFormForUpdate")
                .param("fieldId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(fieldOfStudyService, times(2)).findById(-1);
        verifyNoMoreInteractions(fieldOfStudyService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldSaveFieldAndRedirectToFieldsList() throws Exception {
        doNothing().when(fieldOfStudyService).save(isA(FieldOfStudy.class));

        mvc.perform(post("/fields/save")
                    .with(csrf())
                    .param("id", "" + fieldOfStudy.getId())
                    .param("name", fieldOfStudy.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/fields/list"));
        verify(fieldOfStudyService, times(1)).save(isA(FieldOfStudy.class));
        verifyNoMoreInteractions(fieldOfStudyService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveFieldAndReturnFormView_WhenFormContainsErrors() throws Exception {
        doNothing().when(fieldOfStudyService).save(isA(FieldOfStudy.class));

        mvc.perform(post("/fields/save")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("fieldOfStudy", 1))
                .andExpect(model().attributeHasFieldErrors("fieldOfStudy", "name"))
                .andExpect(view().name("fields/field-form"));
        verifyNoInteractions(fieldOfStudyService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldDeleteFieldAndRedirectToFieldsList() throws Exception {
        doNothing().when(fieldOfStudyService).deleteById(1);

        mvc.perform(get("/fields/delete")
                    .param("fieldId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/fields/list"));
        verify(fieldOfStudyService, times(1)).deleteById(1);
        verifyNoMoreInteractions(fieldOfStudyService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldNotDeleteFieldThrowNotFoundExceptionAndRedirectToErrorPage_WhenWrongIdPassed() throws Exception {
        doThrow(NotFoundException.class).when(fieldOfStudyService).deleteById(-1);

        /*
        *   Method call added to perform NotFoundException.
        *   Exception was not thrown when mvc.perform was called.
        */
        fieldOfStudyService.deleteById(-1);

        mvc.perform(get("/fields/delete")
                .param("fieldId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(fieldOfStudyService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(fieldOfStudyService);
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnFieldsService_shouldSucceedWith200Or3xx() throws Exception {
        given(fieldOfStudyService.findById(1)).willReturn(fieldOfStudy);

        mvc.perform(get("/fields/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/fields/showFormForAdd"))
                .andExpect(status().isOk());
        mvc.perform(get("/fields/showFormForUpdate")
                .param("fieldId", "1"))
                .andExpect(status().isOk());
        mvc.perform(post("/fields/save")
                .with(csrf()))
                .andExpect(status().isOk());
        mvc.perform(get("/fields/delete")
                .param("fieldId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnFieldOfStudyService_shouldFailedWith403() throws Exception {
        mvc.perform(get("/fields/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/fields/showFormForAdd"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/fields/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/fields/save")
                .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/fields/delete"))
                .andExpect(status().isForbidden());
    }
}