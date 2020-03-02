package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;
    private SpecializationService specializationService;


    @Autowired
    public StudentController(StudentService studentService, SpecializationService specializationService) {
        this.studentService = studentService;
        this.specializationService = specializationService;
    }



    @GetMapping("/list")
    public String showStudentList(Model model){
        model.addAttribute("students", studentService.findAll());
        return "/students/student-list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        Student student = new Student();

        model.addAttribute("student", student);
        model.addAttribute("specs", specializationService.findAll());


        return "/students/student-form";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, @RequestParam("specId") int specId){
        student.setSpecialization(specializationService.findById(specId));
        studentService.save(student);

        return "redirect:/students/list";
    }

    @GetMapping("/showFormForUpdate")
    public String updateStudent(@RequestParam("studentId") int studentId ,Model model){
        model.addAttribute("student", studentService.findById(studentId));
        model.addAttribute("specs", specializationService.findAll());

        return "/students/student-form";
    }

    @GetMapping("/delete")
    public String deleteStudent(@RequestParam("studentId") int id){
        studentService.deleteById(id);

        return "redirect:/students/list";
    }
}
