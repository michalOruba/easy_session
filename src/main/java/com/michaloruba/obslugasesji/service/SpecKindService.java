package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.SpecKind;

import java.util.List;

public interface SpecKindService {
    List<SpecKind> findAll();

    SpecKind findById(int kindId);
    List<SpecKind>  findByFieldOfStudy_Id(int id);
}
