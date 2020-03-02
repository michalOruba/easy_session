package com.michaloruba.obslugasesji.rest;

import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private StudentService studentService;
    private SpecializationService specializationService;

    @Autowired
    public StudentRestController(StudentService studentService, SpecializationService specializationService) {
        this.studentService = studentService;
        this.specializationService = specializationService;
    }

    @GetMapping("/students")
    public List<Student> findAll(){
        return studentService.findAll();
    }

    @GetMapping("/students/{studentId}")
    public Student findById(@PathVariable("studentId") int studentId){
        return studentService.findById(studentId);
    }

    @PostMapping("/students")
    public Student saveStudent(@RequestBody Student student){
        student.setId(0);
        studentService.save(student);
        return student;
    }

    @PutMapping("/students")
    public Student updateStudent(@RequestBody Student student){
        studentService.save(student);

        return student;
    }

    @DeleteMapping("/students/{studentId}")
    public String deleteStudent(@PathVariable("studentId") int studentId){

        studentService.deleteById(studentId);

        return "Deleted student with id - " + studentId;
    }
}















