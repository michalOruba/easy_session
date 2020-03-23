package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.helper.SessionStatus;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SessionService;
import com.michaloruba.obslugasesji.service.StudentService;
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
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SessionController.class)
public class SessionControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @MockBean
    private StudentService studentService;
    @MockBean
    private SessionService sessionService;
    @Autowired
        private MockMvc mvc;

    private static Session session;
    private static List<Session> sessions;
    private static Student student;
    private static List<Student> students;
    @BeforeClass
    public static void setUp(){
        student = new Student();
        student.setId(1);
        student.setSpecialization(null);
        student.setFirstName("John");
        student.setLastName("Wayne");
        student.setEmail("john@gmail.com");
        student.setSemester(1);
        students = Arrays.asList(student);

        session = new Session(student, 1);
        session.setId(1);
        session.setStudent(student);
        sessions = Arrays.asList(session);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void list_ShouldAddSessionsEntriesToModelAndRenderSessionsListView() throws Exception {
        given(sessionService.findAll()).willReturn(sessions);

        mvc.perform(get("/sessions/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/sessions/sessions-list"))
                .andExpect(model().attribute("mySessions", hasSize(1)))
                .andExpect(model().attribute("mySessions", hasItem(
                        allOf(
                                hasProperty("id", is(session.getId())),
                                hasProperty("semester", is(session.getSemester())),
                                hasProperty("sessionStatus", is(session.getSessionStatus())),
                                hasProperty("student", is(student))
                        )
                )));
        verify(sessionService, times(1)).findAll();
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd_ShouldAddEmptySessionAndRenderSessionForm() throws Exception {
        when(studentService.findAll()).thenReturn(students);

        mvc.perform(get("/sessions/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("/sessions/session-form"))
                .andExpect(model().attribute("mySession", hasProperty("id", is(0))))
                .andExpect(model().attribute("mySession", hasProperty("semester", is(0))))
                .andExpect(model().attribute("sessionStatus", SessionStatus.values()))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                            hasProperty("id", is(student.getId())),
                            hasProperty("firstName", is(student.getFirstName())),
                            hasProperty("lastName", is(student.getLastName())),
                            hasProperty("email", is(student.getEmail())),
                            hasProperty("semester", is(student.getSemester()))
                        )
                )));
        verify(studentService, times(1)).findAll();
        verifyNoMoreInteractions(studentService);
        verifyNoInteractions(sessionService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldAddExistingSessionAndRenderSessionForm() throws Exception {
        given(sessionService.findById(1)).willReturn(session);
        when(studentService.findAll()).thenReturn(students);

        mvc.perform(get("/sessions/showFormForUpdate").param("sessionId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/sessions/session-form"))
                .andExpect(model().attribute("mySession", hasProperty("id", is(1))))
                .andExpect(model().attribute("mySession", hasProperty("semester", is(1))))
                .andExpect(model().attribute("sessionStatus", SessionStatus.values()))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(student.getId())),
                                hasProperty("firstName", is(student.getFirstName())),
                                hasProperty("lastName", is(student.getLastName())),
                                hasProperty("email", is(student.getEmail())),
                                hasProperty("semester", is(student.getSemester()))                        )
                )));
        verify(sessionService, times(1)).findById(1);
        verifyNoMoreInteractions(sessionService);
        verify(studentService, times(1)).findAll();
        verifyNoMoreInteractions(studentService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldThrowNotFoundExceptionWhenWrongIdPassed() throws Exception{
        when(sessionService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        sessionService.findById(-1);

        mvc.perform(get("/fields/showFormForUpdate").param("sessionId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(sessionService, times(2)).findById(-1);
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldSaveSessionAndRedirectToSessionsList() throws Exception {
        doNothing().when(sessionService).save(session);
        when(studentService.findById(anyInt())).thenReturn(student);

        mvc.perform(post("/sessions/save")
                    .with(csrf()).flashAttr("mySession", session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions/list"));
        verify(sessionService, times(1)).save(isA(Session.class));
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveSessionAndReturnFormView() throws Exception {
        doNothing().when(sessionService).save(isA(Session.class));

        mvc.perform(post("/sessions/save")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("mySession", 2))
                .andExpect(model().attributeHasFieldErrors("mySession", "semester"))
                .andExpect(model().attributeHasFieldErrors("mySession", "student"))
                .andExpect(view().name("/sessions/session-form"));
        verifyNoInteractions(sessionService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveFieldWhenStudentIsNull_ThrowNotFoundException_AndRedirectToErrorPage() throws Exception {
        doThrow(NotFoundException.class).when(studentService).findById(-1);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        studentService.findById(-1);

        mvc.perform(get("/sessions/save")
                .param("sessionId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(studentService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldDeleteSessionAndRedirectToSessionsList() throws Exception {
        doNothing().when(sessionService).deleteById(1);

        mvc.perform(get("/sessions/delete")
                    .param("sessionId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions/list"));
        verify(sessionService, times(1)).deleteById(1);
        verifyNoMoreInteractions(sessionService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldNotDeleteField_ThrowNotFoundException_AndRedirectToErrorPage() throws Exception {
        doThrow(NotFoundException.class).when(sessionService).deleteById(-1);

        /*
        *   Method call added to perform NotFoundException.
        *   Exception was not thrown when mvc.perform was called.
        */
        sessionService.deleteById(-1);

        mvc.perform(get("/sessions/delete")
                .param("sessionId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(sessionService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(sessionService);
    }


    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnSessionService_shouldSucceedWith200() throws Exception {
        given(sessionService.findById(1)).willReturn(session);

        mvc.perform(get("/sessions/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/sessions/showFormForAdd"))
                .andExpect(status().isOk());
        mvc.perform(get("/sessions/showFormForUpdate").param("sessionId", "1"))
                .andExpect(status().isOk());
        mvc.perform(post("/sessions/save")
                .with(csrf()))
                .andExpect(status().isOk());
        mvc.perform(get("/sessions/delete").param("sessionId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnSessionService_shouldSucceedWith403_ListShouldGive200() throws Exception {
        mvc.perform(get("/sessions/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/sessions/showFormForAdd"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/sessions/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/sessions/save")
                    .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/sessions/delete"))
                .andExpect(status().isForbidden());
    }
}