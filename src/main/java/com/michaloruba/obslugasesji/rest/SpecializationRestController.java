package com.michaloruba.obslugasesji.rest;

import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SpecializationRestController {

    private SpecializationService specializationService;

    @Autowired
    public SpecializationRestController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping("/specs")
    public List<InformationSpecialization> findAll(){
        return specializationService.findAll();
    }

    @GetMapping("/specs/{specId}")
    public InformationSpecialization findById(@PathVariable("specId") int specId){
        return specializationService.findById(specId);
    }

    @PostMapping("/specs")
    public InformationSpecialization addSpec(@RequestBody InformationSpecialization specialization){
        specialization.setId(0);
        specializationService.save(specialization);
        return specialization;
    }

    @PutMapping("/specs")
    public InformationSpecialization updateSpec(@RequestBody InformationSpecialization specialization){
        specializationService.save(specialization);
        return specialization;
    }

    @DeleteMapping("/specs/{specId}")
    public String deleteSpec(@PathVariable("specId") int specId){
        InformationSpecialization specialization = specializationService.findById(specId);

        if (specialization == null){
            throw new NotFoundException("Not found specialization with id - " + specId);
        }

        specializationService.deleteById(specId);

        return "Deleted specialization with id - " + specId;
    }


}
