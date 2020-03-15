package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByStudentIdAndSemester(int studentId, int semester);
}
