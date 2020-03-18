package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "fields")
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Integer> {
}
