package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.SpecKind;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SpecKindService;
import com.michaloruba.obslugasesji.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/specs")
public class SpecializationController {
    private SpecializationService specializationService;
    private SpecKindService specKindService;

    @Autowired
    public SpecializationController(SpecializationService specializationService, SpecKindService specKindService) {
        this.specializationService = specializationService;
        this.specKindService = specKindService;
    }

    @GetMapping("/list")
    public String showListOfSpecs(Model model){
        model.addAttribute("specializations", specializationService.findAll());
        return "specializations/specs-list";
    }

    @GetMapping("/showFormForSelectSpec")
    public String showFormForSelectSpec(Model model){
        model.addAttribute("specKinds", specKindService.findAll());
        return "specializations/specs-select-kind";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(@RequestParam("specKindId") int kindId, Model model){
        try {
            SpecKind specKind = specKindService.findById(kindId);
            InformationSpecialization specialization = new InformationSpecialization();
            specialization.setSpecKind(specKind);
            model.addAttribute("specialization", specialization);
        } catch (NotFoundException e){
            List<SpecKind> specKinds = specKindService.findAll();
            model.addAttribute("message", "invalid specialization kind (Can not be null)");
            model.addAttribute("specKinds", specKinds);
            return "specializations/specs-select-kind";
        }
        return "specializations/spec-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("specId") int specId, Model model){
        try {
            model.addAttribute("specialization", specializationService.findById(specId));
        } catch (NotFoundException e ){
            return "error-404";
        }
        return "specializations/spec-form";
    }

    @PostMapping("/save")
    public String saveSpec(@Valid @ModelAttribute("specialization") InformationSpecialization specialization, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "specializations/spec-form";
        }
        specializationService.save(specialization);
        return "redirect:/specs/list";
    }

    @GetMapping("/delete")
    public String deleteSpec(@ModelAttribute("specId") int id){
        specializationService.deleteById(id);
        return "redirect:/specs/list";
    }
}