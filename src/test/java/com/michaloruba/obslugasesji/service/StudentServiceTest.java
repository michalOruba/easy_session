package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SessionRepository;
import com.michaloruba.obslugasesji.dao.StudentRepository;
import com.michaloruba.obslugasesji.dao.SubjectGradeRepository;
import com.michaloruba.obslugasesji.dao.SubjectRepository;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// TODO...


@RunWith(SpringRunner.class)
public class StudentServiceTest {
    @TestConfiguration
    static class StudentServiceImplTestContextConfiguration {
        @Autowired
        SubjectGradeRepository subjectGradeRepository;

        @Bean
        public SubjectGradeService subjectGradeService() {
            return new SubjectGradeServiceImpl(subjectGradeRepository);
        }

        @Autowired
        SubjectRepository subjectRepository;

        @Bean
        public SubjectService subjectService() {
            return new SubjectServiceImpl(subjectRepository);
        }

        @Autowired
        SessionRepository sessionRepository;

        @Bean
        public SessionService sessionService() {
            return new SessionServiceImpl(sessionRepository, subjectGradeService(), subjectService());
        }

        @Autowired
        StudentRepository studentRepository;

        @Bean
        public StudentService studentService() {
            return new StudentServiceImpl(studentRepository, sessionService());
        }
    }

    @Autowired
    private SessionService sessionService;
    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private SubjectGradeRepository subjectGradeRepository;

    @MockBean
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentService studentService;
    @MockBean
    private StudentRepository studentRepository;

    private Student marc;

    @Before
    public void setUp() {
        InformationSpecialization specialization = new InformationSpecialization();
        specialization.setSpecKind(null);
        specialization.setStartDate(LocalDate.of(2020,5,1));
        specialization.setEndDate(LocalDate.of(2021,5,1));
        specialization.setId(1);


        marc = new Student("Marek", "Nowak", "marek@gmail.com", 1, specialization);
        marc.setId(1);

        Session session = new Session(marc, 1);
        session.setId(1);
        Optional<Session> testerSession = Optional.of(session);

        when(sessionRepository.findById(session.getId()))
                .thenReturn(testerSession);
        when(sessionRepository.findAll())
                .thenReturn(List.of(session));
        when(sessionRepository.findByStudentIdAndSemester(1, 1))
                .thenReturn(session);
    }

    @Test
    public void whenFindById_ThenReturnSession() {
        int id = 1;
        Session found = sessionService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        sessionService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<Session> roles = sessionService.findAll();
        assertThat(roles.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<Session> sessions = sessionService.findAll();
        assertThat(sessions.get(0).getSemester()).isEqualTo(1);
        assertThat(sessions.get(0).getStudent().getFirstName()).isEqualTo("Marek");
    }

    @Test
    public void whenSaveSession_ThenOneInteractionOccurs(){
        Session newSession = new Session(marc,2);
        sessionService.save(newSession);
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    public void whenDeleteSession_ThenOneInteractionOccurs(){
        int id = 1;
        sessionService.deleteById(id);
        verify(sessionRepository, times(1)).deleteById(anyInt());
    }

    @Test (expected = NotFoundException.class)
    public void whenDeleteSessionWithWrongId_ThenThrowNotFoundException(){
        int id = -1;
        sessionService.deleteById(id);
    }

    @Test
    public void whenFindByStudentAndSemester_ThenReturnSession() {

        Session foundSession = sessionService.findByStudentIdAndSemester(1,1);
        assertThat(foundSession.getId()).isEqualTo(1);
    }

    @Test
    public void whenFindByWrongSemester_ThenReturnNull(){
        Session foundSession = sessionService.findByStudentIdAndSemester(1, -1);
        assertThat(foundSession).isNull();
    }

    @Test
    public void whenFindByWrongStudent_ThenReturnNull(){
        Session foundSession = sessionService.findByStudentIdAndSemester(-1, 1);
        assertThat(foundSession).isNull();
    }
}
