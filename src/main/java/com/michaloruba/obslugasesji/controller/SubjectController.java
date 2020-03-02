package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private SubjectService subjectService;
    private SpecializationService specializationService;

    @Autowired
    public SubjectController(SubjectService subjectService, SpecializationService specializationService) {
        this.subjectService = subjectService;
        this.specializationService = specializationService;
    }

    @GetMapping("/list")
    public String findAllSubjects(Model model){
        model.addAttribute("subjects", subjectService.findAll());

        return "/subjects/subjects-list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        model.addAttribute("subject", new Subject());
        model.addAttribute("specs", specializationService.findAll());

        return "/subjects/subject-form";
    }

    @PostMapping("/save")
    public String saveSubject(@ModelAttribute Subject subject){
        subjectService.save(subject);

        return "redirect:/subjects/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("subId") int subId, Model model){

        model.addAttribute("subject", subjectService.findById(subId));
        model.addAttribute("specs", specializationService.findAll());

        return "/subjects/subject-form";
    }

    @GetMapping("/delete")
    public String deleteSubject(@RequestParam("subId") int subId){
        subjectService.deleteById(subId);
        return "redirect:/subjects/list";
    }


}
