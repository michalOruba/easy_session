package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.SpecKind;
import com.michaloruba.obslugasesji.service.SpecKindService;
import com.michaloruba.obslugasesji.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String viewSpecs(Model model){
        List<InformationSpecialization> specializations = specializationService.findAll();

        model.addAttribute("specializations", specializations);

        return "specializations/specs-list";
    }






    @GetMapping("/showFormForSelectSpec")
    public String showFormForSelectSpec(Model model){
        List<SpecKind> specKinds = specKindService.findAll();

        model.addAttribute("specKinds", specKinds);

        return "specializations/specs-select-kind";
    }




    @GetMapping("/showFormForAdd")
    public String showFormForAdd(@RequestParam("specKindId") int kindId, Model model){

        SpecKind specKind = specKindService.findById(kindId);
        InformationSpecialization specialization = new InformationSpecialization();
        specialization.setSpecKind(specKind);



        model.addAttribute("specialization", specialization);

        return "specializations/spec-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("specId") int specId, Model model){
        InformationSpecialization specialization = specializationService.findById(specId);

        model.addAttribute("specialization", specialization);

        return "specializations/spec-form";
    }



    @PostMapping("/saveSpec")
    public String saveSpec(@ModelAttribute("specialization") InformationSpecialization specialization){


        specializationService.save(specialization);

        return "redirect:/specs/list";
    }

    @GetMapping("/deleteSpec")
    public String deleteSpec(@ModelAttribute("specId") int id){
        specializationService.deleteById(id);

        return "redirect:/specs/list";
    }
}
