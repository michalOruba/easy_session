package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;

//@NoRepositoryBean
//public interface FieldOfStudyRepository<T extends FieldOfStudy> extends JpaRepository<T, Integer> {
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Integer> {
}
