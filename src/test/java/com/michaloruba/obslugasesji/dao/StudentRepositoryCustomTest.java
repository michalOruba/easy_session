package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StudentRepositoryCustomTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestEntityManager entityManager;
    private Student marc;

    @Before
    public void setUp(){

        marc = new Student("Marek", "Nowak", "marek@gmail.com", 1, null);
        entityManager.persistAndFlush(marc);
    }

    @Test
    public void whenFindStudentByName_ThenReturnListOfStudents() {
        List<Student> students = studentRepository.searchForStudent("are");
        assertThat(students.get(0).getFirstName()).isEqualTo("Marek");
    }

    @Test
    public void whenFindStudentById_ThenReturnListOfStudents() {
        List<Student> students = studentRepository.searchForStudent(marc.getId());
        assertThat(students.get(0).getId()).isEqualTo(marc.getId());
    }

    @Test
    public void whenFindStudentNotFoundByName_ThenReturnEmptyList() {
        List<Student> students = studentRepository.searchForStudent("y");
        assertThat(students.size()).isEqualTo(0);
    }

    @Test
    public void whenFindStudentNotFoundById_ThenReturnEmptyList() {
        List<Student> students = studentRepository.searchForStudent(-1);
        assertThat(students.size()).isEqualTo(0);
    }

}
