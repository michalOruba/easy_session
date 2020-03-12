package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Student;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;
    private SpecializationService specializationService;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());



    @Autowired
    public StudentController(StudentService studentService, SpecializationService specializationService) {
        this.studentService = studentService;
        this.specializationService = specializationService;
    }


    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @GetMapping("/list")
    public String showListOfStudents(Model model){
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
    public String saveStudent(@Valid @ModelAttribute Student student, BindingResult bindingResult, Model model, @RequestParam("specId") int specId){
        try {
            specializationService.findById(specId);
        } catch (NotFoundException e){
            logger.warn(e.getMessage());
            bindingResult.rejectValue("specialization", "error.student", "invalid specialization (can not be null)");
        }

        student.setSpecialization(specializationService.findById(specId));

        if (bindingResult.hasErrors()){
            model.addAttribute("specs", specializationService.findAll());

            return "/students/student-form";
        }
        else {
            studentService.save(student);
            return "redirect:/students/list";
        }
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

    @PostMapping("/search")
    public String showResultList(@RequestParam("usernameOrId") String usernameOrId,Model model) {

        if (!checkIfInteger(usernameOrId)){
            model.addAttribute("students", studentService.searchForStudent(usernameOrId));
        }
        else {
            model.addAttribute("students", studentService.searchForStudent(Integer.parseInt(usernameOrId)));
        }

        model.addAttribute("input", usernameOrId);

        return "/students/student-list";
    }

    private boolean checkIfInteger(String input){
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
