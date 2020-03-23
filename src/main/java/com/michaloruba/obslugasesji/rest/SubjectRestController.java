package com.michaloruba.obslugasesji.rest;

import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubjectRestController {
    private SubjectService subjectService;

    @Autowired
    public SubjectRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public List<Subject> findAll(){
        return subjectService.findAll();
    }

    @GetMapping("/subjects/{subId}")
    public Subject findById(@PathVariable("subId") int subId){
        return subjectService.findById(subId);
    }

    @PostMapping("/subjects")
    public Subject addSubject(@RequestBody Subject subject){
        subject.setId(0);
        subjectService.save(subject);
        return subject;
    }

    @PutMapping("/subjects")
    public Subject updateSubject(@RequestBody Subject subject){
        subjectService.save(subject);
        return subject;
    }

    @DeleteMapping("/subjects/{subId}")
    public String deleteSubject(@PathVariable("subId") int subId){
        subjectService.deleteById(subId);
        return "Deleted subject with id - " + subId;
    }
}
