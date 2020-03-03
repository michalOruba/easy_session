package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.SubjectGrade;

import java.util.List;

public interface SubjectGradeService {
    List<SubjectGrade> findAll();
    SubjectGrade findById(int id);
    void save(SubjectGrade subjectGrade);
    void deleteById(int id);
    List<SubjectGrade> findAllBySession(Session session);
}
