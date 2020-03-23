package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SubjectRepository;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class SubjectServiceTest {
    @TestConfiguration
    static class SubjectServiceImplTestContextConfiguration {
        @Autowired
        private SubjectRepository subjectRepository;

        @Bean
        public SubjectService subjectService() {
            return new SubjectServiceImpl(subjectRepository);
        }
    }

    @Autowired
    private SubjectService subjectService;
    @MockBean
    private SubjectRepository subjectRepository;
    private Subject subject;

    @Before
    public void setUp() {
        subject = new Subject();
        subject.setECTS(5);
        subject.setHours(60);
        subject.setName("Java");
        subject.setSemester(1);
        subject.setSpecialization(null);
        subject.setId(1);
        Optional<Subject> testerSubject = Optional.of(subject);

        when(subjectRepository.findById(subject.getId()))
                .thenReturn(testerSubject);
        when(subjectRepository.findAll())
                .thenReturn(List.of(subject));
        when(subjectRepository.findAllBySemesterAndSpecialization(subject.getSemester(), subject.getSpecialization()))
                .thenReturn(List.of(subject));
        when(subjectRepository.findByName(subject.getName(), PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<>(List.of(subject)));
    }

    @Test
    public void whenFindById_ThenReturnSubject() {
        int id = 1;
        Subject found = subjectService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        subjectService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<Subject> roles = subjectService.findAll();
        assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<Subject> fields = subjectService.findAll();
        assertThat(fields.get(0).getName()).isEqualTo(subject.getName());
        assertThat(fields.get(0).getSemester()).isEqualTo(subject.getSemester());
        assertThat(fields.get(0).getECTS()).isEqualTo(subject.getECTS());
        assertThat(fields.get(0).getHours()).isEqualTo(subject.getHours());
        assertThat(fields.get(0).getId()).isEqualTo(subject.getId());
        assertThat(fields.get(0).getSpecialization()).isEqualTo(subject.getSpecialization());
    }

    @Test
    public void whenSaveSubject_ThenOneInteractionOccurs(){
        Subject newSubject = new Subject();
        subjectService.save(newSubject);
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    public void whenDeleteSubject_ThenOneInteractionOccurs(){
        int id = 1;
        subjectService.deleteById(id);
        verify(subjectRepository, times(1)).deleteById(anyInt());
    }

    @Test (expected = NotFoundException.class)
    public void whenDeleteRoleWithWrongId_ThenThrowNotFoundException(){
        int id = -1;
        subjectService.deleteById(id);
    }


    @Test
    public void whenFindAllBySemesterAndSpecialization_ThenReturnSubject() {
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
        Page<Subject> subjects = subjectRepository.findByName(subject.getName(), PageRequest.of(0, 5));
        assertThat(subjects.getTotalPages()).isEqualTo(1);
    }


    @Test
    public void whenFindByName_ThenReturnPageWithExistingSubject(){
        Page<Subject> subjects = subjectRepository.findByName(subject.getName(), PageRequest.of(0, 5));
        assertThat(subjects.getContent().get(0).getName()).isEqualTo(subject.getName());
    }



}
