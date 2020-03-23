package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.Student;
import java.util.List;

public interface StudentService {
    List<Student> findAll();
    Student findById(int id);
    void save(Student student);
    void deleteById(int id);
    List<Student> searchForStudent(String name);
    List<Student> searchForStudent(int id);
}
