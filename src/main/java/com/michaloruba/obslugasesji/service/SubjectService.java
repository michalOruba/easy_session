package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAll();
    Subject findById(int id);
    void save(Subject subject);
    void deleteById(int id);
    List<Subject> findAllBySemesterAndSpecialization(int semester, InformationSpecialization specialization);

}
