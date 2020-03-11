package com.michaloruba.obslugasesji.controller;


import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import com.michaloruba.obslugasesji.entity.InformationTechnology;
import com.michaloruba.obslugasesji.service.FieldOfStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/fields")
public class FieldOfStudyController {

    private FieldOfStudyService fieldOfStudyService;

    @Autowired
    public FieldOfStudyController(FieldOfStudyService fieldOfStudyService) {
        this.fieldOfStudyService = fieldOfStudyService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
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
    public String saveField(@Valid @ModelAttribute("fieldOfStudy") InformationTechnology fieldOfStudy, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "/fields/field-form";
        }

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
