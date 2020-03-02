package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.SpecKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecKindRepository extends JpaRepository<SpecKind, Integer> {
    List<SpecKind> findByFieldOfStudy_Id(int id);
}
