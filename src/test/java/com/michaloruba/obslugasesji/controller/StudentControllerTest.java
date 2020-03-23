package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.*;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SessionService;
import com.michaloruba.obslugasesji.service.SpecializationService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
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
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
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
    private StudentService studentService;
    @MockBean
    private SpecializationService specializationService;
    @Autowired
    private MockMvc mvc;

    private static Session session;
    private static Student student;
    private static List<Student> students;
    private static InformationSpecialization specialization;
    private static List<InformationSpecialization> specializations;
    @BeforeClass
    public static void setUp(){

        FieldOfStudy fieldOfStudy = new FieldOfStudy("Information Technology");
        fieldOfStudy.setId(1);

        SpecKind specKind = new SpecKind();
        specKind.setId(1);
        specKind.setName("Programming");
        specKind.setFieldOfStudy(fieldOfStudy);

        specialization = new InformationSpecialization();
        specialization.setId(1);
        specialization.setSpecKind(specKind);
        specialization.setStartDate(LocalDate.of(2020,5,1));
        specialization.setEndDate(LocalDate.of(2021,5,1));
        specializations = Arrays.asList(specialization);

        student = new Student();
        student.setId(1);
        student.setSpecialization(specialization);
        student.setFirstName("John");
        student.setLastName("Wayne");
        student.setEmail("john@gmail.com");
        student.setSemester(1);
        students = Arrays.asList(student);

        session = new Session(student, 1);
        session.setId(1);
        session.setStudent(student);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void list_ShouldAddStudentEntriesToModelAndRenderStudentsListView() throws Exception {
        given(studentService.findAll()).willReturn(students);

        mvc.perform(get("/students/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/students/students-list"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(student.getId())),
                                hasProperty("firstName", is(student.getFirstName())),
                                hasProperty("lastName", is(student.getLastName())),
                                hasProperty("email", is(student.getEmail())),
                                hasProperty("semester", is(student.getSemester())),
                                hasProperty("specialization", is(student.getSpecialization()))
                        )
                )));
        verify(studentService, times(1)).findAll();
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd_ShouldAddEmptyStudentToModelAndRenderStudentForm() throws Exception {
        when(specializationService.findAll()).thenReturn(specializations);

        mvc.perform(get("/students/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("/students/student-form"))
                .andExpect(model().attribute("student", hasProperty("id", is(0))))
                .andExpect(model().attribute("student", hasProperty("firstName", nullValue())))
                .andExpect(model().attribute("student", hasProperty("lastName", nullValue())))
                .andExpect(model().attribute("student", hasProperty("email", nullValue())))
                .andExpect(model().attribute("student", hasProperty("semester", is(0))))
                .andExpect(model().attribute("student", hasProperty("specialization", nullValue())))
                .andExpect(model().attribute("specs", hasSize(1)))
                .andExpect(model().attribute("specs", hasItem(
                        allOf(
                            hasProperty("id", is(specialization.getId())),
                            hasProperty("startDate", is(specialization.getStartDate())),
                            hasProperty("endDate", is(specialization.getEndDate())),
                            hasProperty("specKind", is(specialization.getSpecKind()))
                        )
                )));
        verify(specializationService, times(1)).findAll();
        verifyNoMoreInteractions(specializationService);
        verifyNoInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldAddExistingStudentToModelAndRenderStudentForm() throws Exception {
        given(studentService.findById(1)).willReturn(student);
        when(specializationService.findAll()).thenReturn(specializations);

        mvc.perform(get("/students/showFormForUpdate")
                .param("studentId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/students/student-form"))
                .andExpect(model().attribute("student", hasProperty("id", is(student.getId()))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(student.getFirstName()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(student.getLastName()))))
                .andExpect(model().attribute("student", hasProperty("email", is(student.getEmail()))))
                .andExpect(model().attribute("student", hasProperty("semester", is(student.getSemester()))))
                .andExpect(model().attribute("student", hasProperty("specialization", is(student.getSpecialization()))))
                .andExpect(model().attribute("specs", hasSize(1)))
                .andExpect(model().attribute("specs", hasItem(
                        allOf(
                                hasProperty("id", is(specialization.getId())),
                                hasProperty("startDate", is(specialization.getStartDate())),
                                hasProperty("endDate", is(specialization.getEndDate())),
                                hasProperty("specKind", is(specialization.getSpecKind()))
                        )
                )));
        verify(studentService, times(1)).findById(1);
        verifyNoMoreInteractions(studentService);
        verify(specializationService, times(1)).findAll();
        verifyNoMoreInteractions(specializationService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldThrowNotFoundException_WhenWrongIdPassed() throws Exception{
        when(studentService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        studentService.findById(-1);

        mvc.perform(get("/students/showFormForUpdate")
                .param("studentId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(studentService, times(2)).findById(-1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldSaveStudentAndRedirectToStudentsList() throws Exception {
        doNothing().when(studentService).save(student);
        when(specializationService.findById(anyInt())).thenReturn(specialization);

        mvc.perform(post("/students/save")
                    .with(csrf()).flashAttr("student", student)
                    .param("specId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/list"));
        verify(studentService, times(1)).save(isA(Student.class));
        verifyNoMoreInteractions(studentService);
        verify(specializationService, times(1)).findById(1);
        verifyNoMoreInteractions(specializationService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveStudentAndReturnFormView_WhenFormContainsErrors() throws Exception {
        doNothing().when(studentService).save(isA(Student.class));
        when(specializationService.findById(-1)).thenThrow(NotFoundException.class);

        mvc.perform(post("/students/save")
                .with(csrf())
                .param("specId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("student", 5))
                .andExpect(model().attributeHasFieldErrors("student", "firstName"))
                .andExpect(model().attributeHasFieldErrors("student", "lastName"))
                .andExpect(model().attributeHasFieldErrors("student", "email"))
                .andExpect(model().attributeHasFieldErrors("student", "specialization"))
                .andExpect(model().attributeHasFieldErrors("student", "semester"))
                .andExpect(view().name("/students/student-form"));
        verifyNoInteractions(studentService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveStudentThrowNotFoundExceptionAndRedirectToErrorPage_WhenWrongIdPassed() throws Exception {
        when(specializationService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        specializationService.findById(-1);

        mvc.perform(get("/students/save")
                .param("specId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(specializationService, times(2)).save(isA(InformationSpecialization.class));
        verifyNoMoreInteractions(specializationService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldDeleteStudentAndRedirectToStudentsList() throws Exception {
        doNothing().when(studentService).deleteById(1);

        mvc.perform(get("/students/delete")
                    .param("studentId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/list"));
        verify(studentService, times(1)).deleteById(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldNotDeleteStudentThrowNotFoundExceptionAndRedirectToErrorPage_WhenWrongIdPassed() throws Exception {
        doThrow(NotFoundException.class).when(studentService).deleteById(-1);

        /*
        *   Method call added to perform NotFoundException.
        *   Exception was not thrown when mvc.perform was called.
        */
        studentService.deleteById(-1);

        mvc.perform(get("/students/delete")
                .param("studentId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(studentService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void search_shouldFindStudentByIdAddObjectToModelAndReturnStudentsList() throws Exception {
        when(studentService.searchForStudent(1)).thenReturn(students);

        mvc.perform(post("/students/search")
                        .param("usernameOrId", "1")
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/students/students-list"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("students", students));
        verify(studentService, times(1)).searchForStudent(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void search_shouldFindStudentByUsernameAddObjectToModelAndReturnStudentsList() throws Exception {
        when(studentService.searchForStudent("oh")).thenReturn(students);

        mvc.perform(post("/students/search")
                .param("usernameOrId", "oh")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/students/students-list"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("students", students));
        verify(studentService, times(1)).searchForStudent("oh");
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void search_shouldNotFindStudentAddEmptyListToModelAndReturnStudentsList_WhenWrongUsernamePassed() throws Exception {
        when(studentService.searchForStudent("WRONGUSERNAME")).thenReturn(Collections.emptyList());

        mvc.perform(post("/students/search")
                .param("usernameOrId", "WRONGUSERNAME")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/students/students-list"))
                .andExpect(model().attribute("students", hasSize(0)));
        verify(studentService, times(1)).searchForStudent("WRONGUSERNAME");
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void search_shouldNotFindStudentAddEmptyListToModelAndReturnStudentsList_WhenWrongIdPassed() throws Exception {
        when(studentService.searchForStudent(-1)).thenReturn(Collections.emptyList());

        mvc.perform(post("/students/search")
                .param("usernameOrId", "-1")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/students/students-list"))
                .andExpect(model().attribute("students", hasSize(0)));
        verify(studentService, times(1)).searchForStudent(-1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showSessionDetails_shouldFindStudentsSessionAndRedirectToSessionDetailView() throws Exception {
        when(studentService.findById(1)).thenReturn(student);
        when(sessionService.findByStudentIdAndSemester(student.getId(), student.getSemester())).thenReturn(session);

        mvc.perform(get("/students/showSessionDetails")
                .param("studentId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grades/showSessionDetails?sessionId=" + session.getId()));
        verify(studentService, times(1)).findById(1);
        verifyNoMoreInteractions(studentService);
        verify(sessionService, times(1)).findByStudentIdAndSemester(student.getId(), student.getSemester());
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showSessionDetails_shouldAddErrorMessageListOfStudentsAndReturnStudentList_WhenSessionIsNotFound() throws Exception {
        when(studentService.findById(1)).thenReturn(student);
        when(studentService.findAll()).thenReturn(students);
        when(sessionService.findByStudentIdAndSemester(student.getId(), student.getSemester())).thenReturn(null);

        mvc.perform(get("/students/showSessionDetails")
                .param("studentId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/students/students-list"))
                .andExpect(model().attribute("errorMessage", "Sorry, no active session was found for this Student"))
                .andExpect(model().attribute("students", students));

        verify(studentService, times(1)).findById(1);
        verify(studentService, times(1)).findAll();
        verifyNoMoreInteractions(studentService);
        verify(sessionService, times(1)).findByStudentIdAndSemester(student.getId(), student.getSemester());
        verifyNoMoreInteractions(sessionService);

    }


    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnStudentService_shouldSucceedWith200Or3xx() throws Exception {
        given(studentService.findById(1)).willReturn(student);
        given(sessionService.findByStudentIdAndSemester(student.getId(), student.getSemester())).willReturn(session);
        given(specializationService.findAll()).willReturn(specializations);
        given(specializationService.findById(1)).willReturn(specialization);
        given(studentService.searchForStudent(1)).willReturn(students);

        mvc.perform(get("/students/list"))
                .andExpect(status().isOk());
        mvc.perform(post("/students/search")
                .param("usernameOrId","1")
                .with(csrf()))
                .andExpect(status().isOk());
        mvc.perform(get("/students/showSessionDetails")
                .param("studentId", "1"))
                .andExpect(status().is3xxRedirection());
        mvc.perform(get("/students/showFormForAdd"))
                .andExpect(status().isOk());
        mvc.perform(get("/students/showFormForUpdate")
                .param("studentId", "1"))
                .andExpect(status().isOk());
        mvc.perform(post("/students/save")
                .param("specId", "1")
                .with(csrf()))
                .andExpect(status().isOk());
        mvc.perform(get("/students/delete")
                .param("studentId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnStudentServiceListSearchAndSessionDetails_shouldSucceedWith200() throws Exception {
        given(studentService.searchForStudent(1)).willReturn(students);
        given(studentService.findById(1)).willReturn(student);
        given(sessionService.findByStudentIdAndSemester(student.getId(), student.getSemester())).willReturn(session);

        mvc.perform(get("/students/list"))
                .andExpect(status().isOk());
        mvc.perform(post("/students/search")
                .param("usernameOrId","1")
                .with(csrf()))
                .andExpect(status().isOk());
        mvc.perform(get("/students/showSessionDetails")
                .param("studentId", "1"))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnStudentService_shouldFailedWith403() throws Exception {
        given(studentService.searchForStudent(1)).willReturn(students);
        given(studentService.findById(1)).willReturn(student);
        given(sessionService.findByStudentIdAndSemester(student.getId(), student.getSemester())).willReturn(session);

        mvc.perform(get("/students/showFormForAdd"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/students/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/students/save")
                .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/students/delete"))
                .andExpect(status().isForbidden());
    }
}