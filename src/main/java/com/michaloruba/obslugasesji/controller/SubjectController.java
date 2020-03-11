package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
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
    public String saveSubject(@Valid @ModelAttribute Subject subject, BindingResult bindingResult, Model model){
        System.out.println(bindingResult.toString());
        try {
            if(subject.getSpecialization() == null) throw new NotFoundException("Specialization not found");
            specializationService.findById(subject.getSpecialization().getId());
        } catch (NotFoundException e){
            System.out.println(e.getMessage());
            bindingResult.rejectValue("specialization", "error.subject", "invalid specialization (can not be null)");
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("specs", specializationService.findAll());
            return "/subjects/subject-form";
        }
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
