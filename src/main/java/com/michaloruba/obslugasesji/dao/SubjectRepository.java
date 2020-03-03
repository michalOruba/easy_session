package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findAllBySemesterAndSpecialization(int semester, InformationSpecialization specialization);
}
