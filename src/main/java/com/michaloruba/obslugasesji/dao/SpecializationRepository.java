package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepository extends JpaRepository<InformationSpecialization, Integer> {
}
