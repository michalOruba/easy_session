package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;

import java.util.List;

public interface SpecializationService {
    List<InformationSpecialization> findAll();
    InformationSpecialization findById(int id);
    void save(InformationSpecialization specialization);
    void deleteById(int id);
}
