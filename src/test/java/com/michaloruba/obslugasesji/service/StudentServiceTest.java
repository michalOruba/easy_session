package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.*;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        marc = new Student("Marek", "Nowak", "marek@gmail.com", 1, null);
        marc.setId(1);

        Optional<Student> testerStudent = Optional.of(marc);

        when(studentRepository.findById(marc.getId()))
                .thenReturn(testerStudent);
        when(studentRepository.findAll())
                .thenReturn(List.of(marc));
        when(studentRepository.searchForStudent(marc.getId()))
                .thenReturn(List.of(marc));
        when(studentRepository.searchForStudent("arc"))
                .thenReturn(List.of(marc));
    }

    @Test
    public void whenFindById_ThenReturnStudent() {
        int id = 1;
        Student found = studentService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        studentService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<Student> students = studentService.findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<Student> students = studentService.findAll();
        assertThat(students.get(0).getId()).isEqualTo(marc.getId());
        assertThat(students.get(0).getFirstName()).isEqualTo(marc.getFirstName());
        assertThat(students.get(0).getLastName()).isEqualTo(marc.getLastName());
        assertThat(students.get(0).getEmail()).isEqualTo(marc.getEmail());
        assertThat(students.get(0).getSemester()).isEqualTo(marc.getSemester());
        assertThat(students.get(0).getSpecialization()).isEqualTo(marc.getSpecialization());
    }

    @Test
    public void whenSaveStudent_ThenOneInteractionOccurs(){
        Student newStudent = new Student("Luke", "Skywalker", "luke@gmail.com", 1, null);
        studentService.save(newStudent);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void whenDeleteSession_ThenOneInteractionOccurs(){
        int id = 1;
        studentService.deleteById(id);
        verify(studentRepository, times(1)).deleteById(anyInt());
    }

    @Test (expected = NotFoundException.class)
    public void whenDeleteSessionWithWrongId_ThenThrowNotFoundException(){
        int id = -1;
        studentService.deleteById(id);
    }

    @Test
    public void whenFindStudentByName_ThenReturnListOfStudents(){
        List<Student> students = studentService.searchForStudent("arc");
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo(marc.getFirstName());
    }

    @Test
    public void whenFindStudentByWrongName_ThenReturnEmptyListOfStudents(){
        List<Student> students = studentService.searchForStudent("y");
        assertThat(students.size()).isEqualTo(0);
    }

    @Test
    public void whenFindStudentById_ThenReturnListOfStudents(){
        List<Student> students = studentService.searchForStudent(marc.getId());
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getId()).isEqualTo(marc.getId());
    }

    @Test
    public void whenFindStudentByWrongId_ThenReturnListOfStudents(){
        List<Student> students = studentService.searchForStudent(-1);
        assertThat(students.size()).isEqualTo(0);
    }
}
