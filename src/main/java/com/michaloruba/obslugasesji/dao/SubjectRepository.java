package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}
