package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TestEntityManager entityManager;
    private Subject subject;

    @Before
    public void setUp(){
        InformationSpecialization specialization = new InformationSpecialization();
        specialization.setSpecKind(null);
        specialization.setStartDate(LocalDate.of(2020,5,1));
        specialization.setEndDate(LocalDate.of(2021,5,1));
        entityManager.persistAndFlush(specialization);

        subject = new Subject();
        subject.setECTS(5);
        subject.setHours(60);
        subject.setName("Java");
        subject.setSemester(1);
        subject.setSpecialization(specialization);
        entityManager.persistAndFlush(subject);
    }

    @Test
    public void whenFindAllBySemesterAndSpecialization_ThenReturnListOfSubjects() {
        List<Subject> foundSubjects = subjectRepository.findAllBySemesterAndSpecialization(subject.getSemester(), subject.getSpecialization());
        assertThat(foundSubjects.get(0).getName()).isEqualTo(subject.getName());
    }

    @Test
    public void whenFindAllByWrongSemesterAndSpecialization_ThenReturnEmptyList() {
        List<Subject> foundSubjects = subjectRepository.findAllBySemesterAndSpecialization(-1, subject.getSpecialization());
        assertThat(foundSubjects.size()).isEqualTo(0);
    }

    @Test
    public void whenFindAllBySemesterAndWrongSpecialization_ThenReturnEmptyList() {
        InformationSpecialization newSpecialization = new InformationSpecialization();
        newSpecialization.setId(100);
        newSpecialization.setSpecKind(null);
        newSpecialization.setStartDate(LocalDate.of(2020,2,15));
        newSpecialization.setEndDate(LocalDate.of(2021,2,15));

        List<Subject> foundSubjects = subjectRepository.findAllBySemesterAndSpecialization(subject.getSemester(), newSpecialization);
        assertThat(foundSubjects.size()).isEqualTo(0);
    }

    @Test
    public void whenFindByName_ThenReturnPageOfSubjects(){
        Page<Subject> subjects = subjectRepository.findByName("_", PageRequest.of(0, 5));
        assertThat(subjects.getTotalPages()).isEqualTo(1);
    }


    @Test
    public void whenFindByName_ThenReturnPageWithExistingSubject(){
        Page<Subject> subjects = subjectRepository.findByName(subject.getName(), PageRequest.of(0, 5));
        assertThat(subjects.getContent().get(0).getName()).isEqualTo(subject.getName());
    }

}
