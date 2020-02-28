package com.michaloruba.obslugasesji.controller;


import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import com.michaloruba.obslugasesji.entity.InformationTechnology;
import com.michaloruba.obslugasesji.service.FieldOfStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fields")
public class FieldOfStudyController {

    private FieldOfStudyService fieldOfStudyService;

    @Autowired
    public FieldOfStudyController(FieldOfStudyService fieldOfStudyService) {
        this.fieldOfStudyService = fieldOfStudyService;
    }

    @GetMapping("/list")
    public String getFieldsOfStudy(Model model){
        List<FieldOfStudy> fields =  fieldOfStudyService.findAll();
        model.addAttribute("fields", fields);

        return "fields/field-list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        FieldOfStudy fieldOfStudy = new InformationTechnology();

        model.addAttribute("fieldOfStudy", fieldOfStudy);

        return "/fields/field-form";
    }

    @PostMapping("/save")
    public String saveField(@ModelAttribute("fieldOfStudy") InformationTechnology fieldOfStudy){
        fieldOfStudyService.save(fieldOfStudy);

        return "redirect:/fields/list";
    }

    @GetMapping("/showUpdateForm")
    public String updateField(@RequestParam("fieldId") int fieldId, Model model){
        FieldOfStudy fieldOfStudy = fieldOfStudyService.findById(fieldId);

        model.addAttribute("fieldOfStudy", fieldOfStudy);

        return "/fields/field-form";
    }


    @GetMapping("/delete")
    public String deleteField(@RequestParam("fieldId") int fieldId){

        fieldOfStudyService.deleteById(fieldId);

        return "redirect:/fields/list";
    }

}
