package com.michaloruba.obslugasesji.rest;

import com.michaloruba.obslugasesji.entity.SubjectGrade;
import com.michaloruba.obslugasesji.service.SubjectGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SubjectGradeListRestController {
    private SubjectGradeService subjectGradeService;

    @Autowired
    public SubjectGradeListRestController(SubjectGradeService subjectGradeService) {
        this.subjectGradeService = subjectGradeService;
    }

    @GetMapping("/grades")
    public List<SubjectGrade> findAll(){
        return subjectGradeService.findAll();
    }

    @GetMapping("/grades/{gradeId}")
    public SubjectGrade findById(@PathVariable("gradeId") int gradeId){
        return subjectGradeService.findById(gradeId);
    }

    @PostMapping("/grades")
    public SubjectGrade createGrade(@RequestBody SubjectGrade subjectGrade){
        subjectGrade.setId(0);
        subjectGradeService.save(subjectGrade);
        return subjectGrade;
    }

    @PutMapping("/grades")
    public SubjectGrade updateGrade(@RequestBody SubjectGrade subjectGrade){
        subjectGradeService.save(subjectGrade);
        return subjectGrade;
    }

    @DeleteMapping("/grades/{gradeId}")
    public String deleteGrade(@PathVariable("gradeId") int gradeId){
        subjectGradeService.deleteById(gradeId);
        return "Deleted grade with id - " + gradeId;
    }
}
