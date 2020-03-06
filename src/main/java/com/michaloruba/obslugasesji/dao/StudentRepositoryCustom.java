package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Student;

import java.util.List;

public interface StudentRepositoryCustom {
    List<Student> searchForStudent(String name);
    List<Student> searchForStudent(int id);
}
