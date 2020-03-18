package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.helper.SessionStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TestEntityManager entityManager;
    private Student marc;
    private Session session;

    @Before
    public void setUp(){

        marc = new Student("Marek", "Nowak", "marek@gmail.com", 1, null);
        entityManager.persistAndFlush(marc);

        session = new Session(null, 1);
        session.setSessionStatus(SessionStatus.NOT_PASSED);
        session.setStudent(marc);
        entityManager.persistAndFlush(session);
    }

    @Test
    public void whenFindByStudentAndSemester_ThenReturnSession() {

        Session foundSession = sessionRepository.findByStudentIdAndSemester(marc.getId(), session.getSemester());
        assertThat(foundSession.getId()).isEqualTo(session.getId());
    }

    @Test
    public void whenFindByWrongStudent_ThenReturnNull(){
        Session foundSession = sessionRepository.findByStudentIdAndSemester(-1, session.getSemester());
        assertThat(foundSession).isNull();
    }

    @Test
    public void whenFindByWrongSemester_ThenReturnNull(){
        Session foundSession = sessionRepository.findByStudentIdAndSemester(marc.getId(), -1);
        assertThat(foundSession).isNull();
    }
}
