package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.Session;

import java.util.List;

public interface SessionService {
    List<Session> findAll();
    Session findById(int id);
    void save(Session session);
    void deleteById(int id);
    Session findByStudentIdAndSemester(int studentId, int semester);
}
