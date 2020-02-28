package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.SpecKind;
import com.michaloruba.obslugasesji.service.FieldOfStudyService;
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
    private FieldOfStudyService fieldOfStudyService;
    private SpecKindService specKindService;


    @Autowired
    public SpecializationController(SpecializationService specializationService, FieldOfStudyService fieldOfStudyService, SpecKindService specKindService) {
        this.specializationService = specializationService;
        this.fieldOfStudyService = fieldOfStudyService;
        this.specKindService = specKindService;
    }

    @GetMapping("/list")
    public String viewSpecs(Model model){
        List<InformationSpecialization> specializations = specializationService.findAll();

        model.addAttribute("specializations", specializations);

        return "specializations/specs-list";
    }




    @GetMapping("/showFormForSelectField")
    public String showFormForSelectField(Model model){
        List<FieldOfStudy> fieldOfStudies = fieldOfStudyService.findAll();

        model.addAttribute("fields", fieldOfStudies);

        return "specializations/specs-select-field";
    }




    @GetMapping("/showFormForSelectSpec")
    public String showFormForSelectSpec(@RequestParam("fieldId") int fieldId, Model model){
        List<SpecKind> specKinds = specKindService.findByFieldOfStudy_Id(fieldId);

        model.addAttribute("specKinds", specKinds);

        return "specializations/specs-select-kind";
    }




    @GetMapping("/showFormForAdd")
    public String showFormForAdd(@RequestParam("specKindId") int kindId, Model model){

        SpecKind specKind = specKindService.findById(kindId);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> showFormForAdd: specKind -> Field of study " + specKind.getFieldOfStudy());
        InformationSpecialization specialization = new InformationSpecialization();
        specialization.setSpecKind(specKind);
        specialization.getSpecKind().setFieldOfStudy(specKind.getFieldOfStudy());


        model.addAttribute("specialization", specialization);

        return "specializations/spec-form";
    }

    @PostMapping("/saveSpec")
    public String saveSpec(@ModelAttribute("specialization") InformationSpecialization specialization){

        System.out.println(">>>>>>>>>>>>>>>>>>>>>> SPEC KIND NAME " + specialization.getSpecKind().getName());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> SPEC KIND FIELD NAME " + specialization.getSpecKind().getFieldOfStudy().getName());

        specializationService.save(specialization);

        return "redirect:/specs/list";
    }

    @GetMapping("/deleteSpec")
    public String deleteSpec(@ModelAttribute("specId") int id){
        specializationService.deleteById(id);

        return "redirect:/specs/list";
    }
}
