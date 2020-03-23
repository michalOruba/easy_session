package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.entity.SubjectGrade;
import com.michaloruba.obslugasesji.helper.SubjectGradeTypes;
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
public class SubjectGradeRepositoryTest {

    @Autowired
    private SubjectGradeRepository subjectGradeRepository;
    @Autowired
    private TestEntityManager entityManager;
    private Session session;
    private SubjectGrade subjectGrade;

    @Before
    public void setUp(){

        session = new Session(1);
        session.setStudent(null);
        entityManager.persistAndFlush(session);

        Subject subject = new Subject();
        subject.setECTS(5);
        subject.setHours(60);
        subject.setName("Java");
        subject.setSemester(1);
        subject.setSpecialization(null);
        entityManager.persistAndFlush(subject);

        subjectGrade = new SubjectGrade();
        subjectGrade.setSession(session);
        subjectGrade.setSubject(subject);
        subjectGrade.setGrade(SubjectGradeTypes.TWO);
        entityManager.persistAndFlush(subjectGrade);
    }


    @Test
    public void whenFindBySession_ThenReturnListOfSubjectGrades() {
        List<SubjectGrade> foundSubjectGrades = subjectGradeRepository.findAllBySession(session);
        assertThat(foundSubjectGrades.get(0).getGrade()).isEqualTo(subjectGrade.getGrade());
    }

    @Test
    public void whenFindByWrongSession_ThenReturnEmptyList(){
        Session newSession = new Session(2);
        newSession.setId(5);

        List<SubjectGrade> foundSubjectGrades = subjectGradeRepository.findAllBySession(newSession);
        assertThat(foundSubjectGrades.size()).isEqualTo(0);
    }

}
