package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.StudentRepository;
import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(int id) {
        Optional<Student> result = studentRepository.findById(id);
        Student student = null;

        if (result.isPresent()) {
            student = result.get();
        }
        else {
            throw new NotFoundException("Not found student with id - " + id);
        }

        return student;
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void deleteById(int id) {
        if (!studentRepository.findById(id).isPresent()){
            throw new NotFoundException("Not found student with id - " + id);
        }
        studentRepository.deleteById(id);
    }
}
