package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SessionRepository;
import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.entity.SubjectGrade;
import com.michaloruba.obslugasesji.helper.SubjectGradeTypes;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
    private SessionRepository sessionRepository;
    private SubjectGradeService subjectGradeService;
    private SubjectService subjectService;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository, SubjectGradeService subjectGradeService, SubjectService subjectService) {
        this.sessionRepository = sessionRepository;
        this.subjectGradeService = subjectGradeService;
        this.subjectService = subjectService;
    }

    @Override
    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public Session findById(int id) {
        Optional<Session> result = sessionRepository.findById(id);
        Session session;
        if (result.isPresent()){
            session = result.get();
        }
        else {
            throw new NotFoundException("Not found session with id - " + id);
        }
        return session;
    }

    @Override
    public void save(Session session) {
        if (session.getId() == 0) {
            sessionRepository.save(session);
            List<Subject> subjects = subjectService.findAllBySemesterAndSpecialization(
                    session.getSemester(), session.getStudent().getSpecialization()
            );

            subjects.
                    forEach(subject -> {
                        SubjectGrade subjectGrade = new SubjectGrade(SubjectGradeTypes.NONE, subject, session);
                        subjectGradeService.save(subjectGrade);
                    });
        }
        else sessionRepository.save(session);
    }

    @Override
    public void deleteById(int id) {
        if (sessionRepository.findById(id).isPresent()){
            sessionRepository.deleteById(id);
        }
        else {
            throw new NotFoundException("Not found session with id - " + id);
        }
    }

    @Override
    public Session findByStudentIdAndSemester(int studentId, int semester) {
        return sessionRepository.findByStudentIdAndSemester(studentId, semester);
    }
}
