package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.FieldOfStudy;

import java.util.List;

public interface FieldOfStudyService {
    List<FieldOfStudy> findAll();
    FieldOfStudy findById(int id);
    void save(FieldOfStudy fieldOfStudy);
    void deleteById(int id);
}
