package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.StudentRepository;
import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private SessionService sessionService;


    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, SessionService sessionService) {
        this.studentRepository = studentRepository;
        this.sessionService = sessionService;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(int id) {
        Optional<Student> result = studentRepository.findById(id);
        Student student;

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

        if (student.getId() == 0) {
            studentRepository.save(student);
            Session session = new Session(student ,student.getSemester());
            sessionService.save(session);
        }
        else studentRepository.save(student);
    }

    @Override
    public void deleteById(int id) {
        if (!studentRepository.findById(id).isPresent()){
            throw new NotFoundException("Not found student with id - " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> searchForStudent(String name) {
        return studentRepository.searchForStudent(name);
    }

    @Override
    public List<Student> searchForStudent(int id) {
        return studentRepository.searchForStudent(id);
    }


}
