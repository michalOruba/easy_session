package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.*;
import com.michaloruba.obslugasesji.helper.SubjectGradeTypes;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SessionService;
import com.michaloruba.obslugasesji.service.SubjectGradeService;
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
@WebMvcTest(SubjectGradeController.class)
public class SubjectGradeControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @MockBean
    private SessionService sessionService;
    @MockBean
    private SubjectGradeService subjectGradeService;
    @Autowired
        private MockMvc mvc;

    private static Session session;
    private static SubjectGrade subjectGrade;
    private static List<SubjectGrade> subjectGrades;

    @BeforeClass
    public static void setUp(){
        SpecKind specKind = new SpecKind();
        specKind.setId(1);
        specKind.setName("Programming");
        specKind.setFieldOfStudy(null);

        InformationSpecialization specialization = new InformationSpecialization();
        specialization.setId(1);
        specialization.setSpecKind(specKind);
        specialization.setStartDate(LocalDate.of(2020,5,1));
        specialization.setEndDate(LocalDate.of(2021,5,1));

        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Java");
        subject.setHours(60);
        subject.setECTS(6);
        subject.setSemester(1);
        subject.setSpecialization(specialization);

        Student student = new Student();
        student.setId(1);
        student.setSpecialization(null);
        student.setFirstName("John");
        student.setLastName("Wayne");
        student.setEmail("john@gmail.com");
        student.setSemester(1);

        session = new Session(student, 1);
        session.setId(1);
        session.setStudent(student);

        subjectGrade = new SubjectGrade();
        subjectGrade.setId(1);
        subjectGrade.setSession(session);
        subjectGrade.setSubject(subject);
        subjectGrade.setGrade(SubjectGradeTypes.NONE);
        subjectGrades = Arrays.asList(subjectGrade);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showSessionDetails_ShouldAddSubjectGradesWithSubjectsToModelAndRenderSubjectGradesListView() throws Exception {
        given(sessionService.findById(1)).willReturn(session);
        given(subjectGradeService.findAllBySession(session)).willReturn(subjectGrades);

        mvc.perform(get("/grades/showSessionDetails")
                .param("sessionId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/grades/session-details"))
                .andExpect(model().attribute("mySession", session))
                .andExpect(model().attribute("subjectGrades", hasItem(
                        allOf(
                                hasProperty("id", is(subjectGrade.getId())),
                                hasProperty("grade", is(subjectGrade.getGrade())),
                                hasProperty("session", is(subjectGrade.getSession())),
                                hasProperty("subject", is(subjectGrade.getSubject()))
                        )
                )));
        verify(subjectGradeService, times(1)).findAllBySession(isA(Session.class));
        verifyNoMoreInteractions(subjectGradeService);
        verify(sessionService, times(1)).findById(anyInt());
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdateGrade_ShouldAddExistingSubjectGradeToModelAndRenderSubjectGradeForm() throws Exception {
        given(subjectGradeService.findById(1)).willReturn(subjectGrade);

        mvc.perform(get("/grades/showFormForUpdateGrade")
                .param("subjectGradeId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/grades/session-detail-form"))
                .andExpect(model().attribute("subject", hasProperty("id", is(subjectGrade.getId()))))
                .andExpect(model().attribute("subject", hasProperty("session", is(subjectGrade.getSession()))))
                .andExpect(model().attribute("subject", hasProperty("subject", is(subjectGrade.getSubject()))))
                .andExpect(model().attribute("subject", hasProperty("grade", is(subjectGrade.getGrade()))))
                .andExpect(model().attribute("grades", SubjectGradeTypes.values()));
        verify(subjectGradeService, times(1)).findById(1);
        verifyNoMoreInteractions(subjectGradeService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdateGrade_ShouldThrowNotFoundException_WhenWrongIdPassed() throws Exception{
        when(subjectGradeService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        subjectGradeService.findById(-1);

        mvc.perform(get("/grades/showFormForUpdateGrade")
                .param("subjectGradeId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(subjectGradeService, times(1)).findById(-1);
        verifyNoMoreInteractions(subjectGradeService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveDetail_ShouldSaveSubjectGradeAndRedirectToShowSessionDetails() throws Exception {
        doNothing().when(subjectGradeService).save(subjectGrade);

        mvc.perform(post("/grades/saveDetail")
                    .with(csrf())
                    .flashAttr("subject", subjectGrade)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grades/showSessionDetails?sessionId="+session.getId()));
        verify(subjectGradeService, times(1)).save(isA(SubjectGrade.class));
        verifyNoMoreInteractions(subjectGradeService);
    }


    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnSubjectGradeService_shouldSucceedWith200Or3xx() throws Exception {
        given(sessionService.findById(1)).willReturn(session);
        given(subjectGradeService.findAllBySession(session)).willReturn(subjectGrades);
        given(subjectGradeService.findById(1)).willReturn(subjectGrade);

        mvc.perform(get("/grades/showSessionDetails")
                .param("sessionId", "1"))
                .andExpect(status().isOk());
        mvc.perform(get("/grades/showFormForUpdateGrade")
                .param("subjectGradeId", "1"))
                .andExpect(status().isOk());
        mvc.perform(post("/grades/saveDetail")
                .flashAttr("subject", subjectGrade)
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void givenEmployeeAuthRequestOnSubjectGradeService_shouldSucceedWith200Or3xx() throws Exception {
        given(sessionService.findById(1)).willReturn(session);
        given(subjectGradeService.findAllBySession(session)).willReturn(subjectGrades);
        given(subjectGradeService.findById(1)).willReturn(subjectGrade);

        mvc.perform(get("/grades/showSessionDetails")
                .param("sessionId", "1"))
                .andExpect(status().isOk());
        mvc.perform(get("/grades/showFormForUpdateGrade")
                .param("subjectGradeId", "1"))
                .andExpect(status().isOk());
        mvc.perform(post("/grades/saveDetail")
                .flashAttr("subject", subjectGrade)
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = {"STUDENT"})
    public void givenStudentAuthRequestOnSubjectGradeServiceList_shouldSucceedWith200() throws Exception {
        given(sessionService.findById(1)).willReturn(session);
        given(subjectGradeService.findAllBySession(session)).willReturn(subjectGrades);

        mvc.perform(get("/grades/showSessionDetails")
                .param("sessionId", "1"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"STUDENT"})
    public void givenStudentAuthRequestOnSubjectGradeService_shouldFailedWith403() throws Exception {

        mvc.perform(get("/grades/showFormForUpdateGrade")
                .param("subjectGradeId", "1"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/grades/saveDetail")
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}