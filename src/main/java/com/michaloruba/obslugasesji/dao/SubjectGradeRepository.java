package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.SubjectGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectGradeRepository extends JpaRepository<SubjectGrade, Integer> {
    List<SubjectGrade> findAllBySession(Session session);
}
