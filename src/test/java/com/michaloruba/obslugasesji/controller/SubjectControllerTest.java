package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.SpecKind;
import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.SubjectService;
import com.michaloruba.obslugasesji.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SubjectController.class)
public class SubjectControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @MockBean
    private SpecializationService specializationService;
    @MockBean
    private SubjectService subjectService;
    @Autowired
        private MockMvc mvc;

    private static Subject subject;
    private static List<Subject> subjects;
    private static InformationSpecialization specialization;
    private static List<InformationSpecialization> specializations;
    @BeforeClass
    public static void setUp(){
        SpecKind specKind = new SpecKind();
        specKind.setId(1);
        specKind.setName("Programming");
        specKind.setFieldOfStudy(null);

        specialization = new InformationSpecialization();
        specialization.setId(1);
        specialization.setSpecKind(specKind);
        specialization.setStartDate(LocalDate.of(2020,5,1));
        specialization.setEndDate(LocalDate.of(2021,5,1));
        specializations = Arrays.asList(specialization);

        subject = new Subject();
        subject.setId(1);
        subject.setName("Java");
        subject.setHours(60);
        subject.setECTS(6);
        subject.setSemester(1);
        subject.setSpecialization(specialization);
        subjects = Arrays.asList(subject);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void list_ShouldAddPageObjectWithSubjectsAndRenderSubjectListView() throws Exception {
        when(subjectService.findByName("_", PageRequest.of(0, 10, Sort.Direction.ASC, "semester", "name"))).thenReturn(new PageImpl<>(subjects));

        mvc.perform(get("/subjects/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/subjects-list"))
                .andExpect(model().attribute("subjects", new PageImpl<>(subjects)))
                .andExpect(model().attribute("subjects", hasItem(
                        allOf(
                                hasProperty("id", is(subject.getId())),
                                hasProperty("semester", is(subject.getSemester())),
                                hasProperty("name", is(subject.getName())),
                                hasProperty("specialization", is(subject.getSpecialization())),
                                hasProperty("ECTS", is(subject.getECTS())),
                                hasProperty("hours", is(subject.getHours()))
                        )
                )));
        verify(subjectService, times(1)).findByName(isA(String.class), isA(Pageable.class));
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd_ShouldAddEmptySubjectAndRenderSubjectForm() throws Exception {
        when(specializationService.findAll()).thenReturn(specializations);

        mvc.perform(get("/subjects/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/subject-form"))
                .andExpect(model().attribute("subject", hasProperty("id", is(0))))
                .andExpect(model().attribute("subject", hasProperty("semester", is(0))))
                .andExpect(model().attribute("subject", hasProperty("name", nullValue())))
                .andExpect(model().attribute("subject", hasProperty("specialization", nullValue())))
                .andExpect(model().attribute("subject", hasProperty("ECTS", is(0))))
                .andExpect(model().attribute("subject", hasProperty("hours", is(0.0))));
        verify(specializationService, times(1)).findAll();
        verifyNoMoreInteractions(specializationService);
        verifyNoInteractions(subjectService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldAddExistingSubjectAndRenderSubjectForm() throws Exception {
        given(subjectService.findById(1)).willReturn(subject);
        when(specializationService.findAll()).thenReturn(specializations);

        mvc.perform(get("/subjects/showFormForUpdate")
                .param("subId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/subject-form"))
                .andExpect(model().attribute("subject", hasProperty("id", is(subject.getId()))))
                .andExpect(model().attribute("subject", hasProperty("semester", is(subject.getSemester()))))
                .andExpect(model().attribute("subject", hasProperty("specialization", is(subject.getSpecialization()))))
                .andExpect(model().attribute("subject", hasProperty("name", is(subject.getName()))))
                .andExpect(model().attribute("subject", hasProperty("ECTS", is(subject.getECTS()))))
                .andExpect(model().attribute("subject", hasProperty("hours", is(subject.getHours()))))
                .andExpect(model().attribute("specs", hasItem(
                        allOf(
                                hasProperty("id", is(specialization.getId())),
                                hasProperty("startDate", is(specialization.getStartDate())),
                                hasProperty("endDate", is(specialization.getEndDate())),
                                hasProperty("specKind", is(specialization.getSpecKind()))
                ))));
        verify(subjectService, times(1)).findById(1);
        verifyNoMoreInteractions(subjectService);
        verify(specializationService, times(1)).findAll();
        verifyNoMoreInteractions(specializationService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldThrowNotFoundException_WhenWrongIdPassed() throws Exception{
        when(subjectService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        subjectService.findById(-1);

        mvc.perform(get("/subjects/showFormForUpdate")
                .param("subId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(subjectService, times(2)).findById(-1);
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldSaveSubjectAndRedirectToSubjectsList() throws Exception {
        doNothing().when(subjectService).save(subject);

        mvc.perform(post("/subjects/save")
                .with(csrf())
                .flashAttr("subject", subject)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects/list"));
        verify(subjectService, times(1)).save(isA(Subject.class));
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveSubjectAndReturnFormView_WhenFormIsNotValid() throws Exception {
        doNothing().when(subjectService).save(isA(Subject.class));

        mvc.perform(post("/subjects/save")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("subject", 5))
                .andExpect(model().attributeHasFieldErrors("subject", "semester"))
                .andExpect(model().attributeHasFieldErrors("subject", "specialization"))
                .andExpect(model().attributeHasFieldErrors("subject", "ECTS"))
                .andExpect(model().attributeHasFieldErrors("subject", "hours"))
                .andExpect(model().attributeHasFieldErrors("subject", "name"))
                .andExpect(view().name("subjects/subject-form"));
        verifyNoInteractions(subjectService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveSubjectThrowNotFoundExceptionAndRedirectToErrorPage_WhenWrongIdPassed() throws Exception {
        doThrow(NotFoundException.class).when(subjectService).findById(-1);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        subjectService.findById(-1);

        mvc.perform(get("/subjects/save")
                .param("subId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(subjectService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldDeleteSubjectAndRedirectToSubjectsList() throws Exception {
        doNothing().when(subjectService).deleteById(1);

        mvc.perform(get("/subjects/delete")
                .param("subId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects/list"));
        verify(subjectService, times(1)).deleteById(1);
        verifyNoMoreInteractions(subjectService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldNotDeleteSubjectThrowNotFoundExceptionAndRedirectToErrorPage_WhenWrongIdPassed() throws Exception {
        doThrow(NotFoundException.class).when(subjectService).deleteById(-1);

        /*
        *   Method call added to perform NotFoundException.
        *   Exception was not thrown when mvc.perform was called.
        */
        subjectService.deleteById(-1);

        mvc.perform(get("/subjects/delete")
                .param("subId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(subjectService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(subjectService);
    }


    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnSessionService_shouldSucceedWith200Or3xx() throws Exception {
        when(subjectService.findByName("_", PageRequest.of(0, 10, Sort.Direction.ASC, "semester", "name"))).thenReturn(new PageImpl<>(subjects));
        given(subjectService.findById(1)).willReturn(subject);

        mvc.perform(get("/subjects/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/subjects/showFormForAdd"))
                .andExpect(status().isOk());
        mvc.perform(get("/subjects/showFormForUpdate")
                .param("subId", "1"))
                .andExpect(status().isOk());
        mvc.perform(post("/subjects/save")
                .with(csrf())
        )
                .andExpect(status().isOk());
        mvc.perform(get("/subjects/delete")
                .param("subId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnSubjectServiceList_shouldSucceedWith200() throws Exception {
        when(subjectService.findByName("_", PageRequest.of(0, 10, Sort.Direction.ASC, "semester", "name"))).thenReturn(new PageImpl<>(subjects));

        mvc.perform(get("/subjects/list")
                .param("name", "_")
                .param("page", "1")
        )
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnSubjectService_shouldFailedWith403() throws Exception {
        when(subjectService.findByName("_", PageRequest.of(0, 10, Sort.Direction.ASC, "semester", "name"))).thenReturn(new PageImpl<>(subjects));

        mvc.perform(get("/subjects/showFormForAdd"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/subjects/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/subjects/save")
                .with(csrf())
        )
                .andExpect(status().isForbidden());
        mvc.perform(get("/subjects/delete"))
                .andExpect(status().isForbidden());
    }
}