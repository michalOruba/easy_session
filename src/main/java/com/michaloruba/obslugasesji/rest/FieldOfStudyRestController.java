package com.michaloruba.obslugasesji.rest;

import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import com.michaloruba.obslugasesji.service.FieldOfStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FieldOfStudyRestController {
    private FieldOfStudyService fieldOfStudyService;

    @Autowired
    public FieldOfStudyRestController(FieldOfStudyService fieldOfStudyService) {
        this.fieldOfStudyService = fieldOfStudyService;
    }

    @GetMapping("/fields")
    public List<FieldOfStudy> getFields(){
        return fieldOfStudyService.findAll();
    }

    @GetMapping("/fields/{fieldId}")
    public FieldOfStudy getFieldById(@PathVariable ("fieldId") int id){
        return fieldOfStudyService.findById(id);
    }

    @PostMapping("/fields")
    public FieldOfStudy addField(@RequestBody FieldOfStudy fieldOfStudy){
        fieldOfStudy.setId(0);
        fieldOfStudyService.save(fieldOfStudy);
        return  fieldOfStudy;
    }

    @PutMapping("/fields")
    public FieldOfStudy updateField(@RequestBody FieldOfStudy fieldOfStudy){
        fieldOfStudyService.save(fieldOfStudy);
        return fieldOfStudy;
    }

    @DeleteMapping("/fields/{fieldId}")
    public String deleteField(@PathVariable("fieldId") int id){
        fieldOfStudyService.deleteById(id);
        return "Deleted customer id - " + id;
    }
}
