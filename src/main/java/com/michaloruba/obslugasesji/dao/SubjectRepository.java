package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findAllBySemesterAndSpecialization(int semester, InformationSpecialization specialization);

    @Query("SELECT s FROM Subject s WHERE s.name like %?1%")
    Page<Subject> findByName(String name, Pageable pageable);
}
