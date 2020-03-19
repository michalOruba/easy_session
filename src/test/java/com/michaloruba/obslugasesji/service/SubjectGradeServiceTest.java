package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SubjectGradeRepository;
import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.SubjectGrade;
import com.michaloruba.obslugasesji.helper.SubjectGradeTypes;
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
public class SubjectGradeServiceTest {
    @TestConfiguration
    static class SubjectGradeServiceImplTestContextConfiguration {
        @Autowired
        private SubjectGradeRepository subjectGradeRepository;

        @Bean
        public SubjectGradeService subjectGradeService() {
            return new SubjectGradeServiceImpl(subjectGradeRepository);
        }
    }

    @Autowired
    private SubjectGradeService subjectGradeService;
    @MockBean
    private SubjectGradeRepository subjectGradeRepository;

    private Session session;

    @Before
    public void setUp() {
        session = new Session(1);
        session.setStudent(null);
        session.setId(3);

        SubjectGrade subjectGrade = new SubjectGrade();
        subjectGrade.setId(1);
        subjectGrade.setSubject(null);
        subjectGrade.setSession(session);
        subjectGrade.setGrade(SubjectGradeTypes.TWO);
        Optional<SubjectGrade> testerSubjectGrade = Optional.of(subjectGrade);

        when(subjectGradeRepository.findById(subjectGrade.getId()))
                .thenReturn(testerSubjectGrade);
        when(subjectGradeRepository.findAll())
                .thenReturn(List.of(subjectGrade));
        when(subjectGradeRepository.findAllBySession(session))
                .thenReturn(List.of(subjectGrade));
    }

    @Test
    public void whenFindById_ThenReturnSubjectGrade() {
        int id = 1;
        SubjectGrade found = subjectGradeService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        subjectGradeService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<SubjectGrade> roles = subjectGradeService.findAll();
        assertThat(roles.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<SubjectGrade> fields = subjectGradeService.findAll();
        assertThat(fields.get(0).getGrade()).isEqualTo(SubjectGradeTypes.TWO);
        assertThat(fields.get(0).getId()).isEqualTo(1);
        assertThat(fields.get(0).getSession().getId()).isEqualTo(session.getId());
        assertThat(fields.get(0).getSubject()).isNull();
    }

    @Test
    public void whenSaveSubjectGrade_ThenOneInteractionOccurs(){
        SubjectGrade newSubjectGrade = new SubjectGrade();
        subjectGradeService.save(newSubjectGrade);
        verify(subjectGradeRepository, times(1)).save(any(SubjectGrade.class));
    }

    @Test
    public void whenDeleteSubjectGrade_ThenOneInteractionOccurs(){
        int id = 1;
        subjectGradeService.deleteById(id);
        verify(subjectGradeRepository, times(1)).deleteById(anyInt());
    }

    @Test (expected = NotFoundException.class)
    public void whenDeleteRoleWithWrongId_ThenThrowNotFoundException(){
        int id = -1;
        subjectGradeService.deleteById(id);
    }

    @Test
    public void whenFindBySession_ThenReturnListOfSubjectGrades() {
        List<SubjectGrade> foundSubjectGrades = subjectGradeRepository.findAllBySession(session);
        assertThat(foundSubjectGrades.get(0).getGrade()).isEqualTo(SubjectGradeTypes.TWO);
    }
    @Test
    public void whenFindByWrongSession_ThenReturnEmptyListOfSubjectGrades() {
        Session newSession = new Session(3);
        newSession.setId(66);
        List<SubjectGrade> foundSubjectGrades = subjectGradeRepository.findAllBySession(newSession);
        assertThat(foundSubjectGrades.size()).isEqualTo(0);
    }
}
