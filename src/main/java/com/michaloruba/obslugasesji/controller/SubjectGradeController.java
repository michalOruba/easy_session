package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.SubjectGrade;
import com.michaloruba.obslugasesji.service.SessionService;
import com.michaloruba.obslugasesji.service.SubjectGradeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/grades")
public class SubjectGradeController {

    private SessionService sessionService;
    private SubjectGradeService subjectGradeService;

    public SubjectGradeController(SessionService sessionService, SubjectGradeService subjectGradeService) {
        this.sessionService = sessionService;
        this.subjectGradeService = subjectGradeService;
    }

    @GetMapping("/showSessionDetails")
    public String showSessionDetails(@RequestParam("sessionId") int sessionId , Model model){
        model.addAttribute("mySession", sessionService.findById(sessionId));
        model.addAttribute("subjectGrades", subjectGradeService.findAllBySession(sessionService.findById(sessionId)));

        return "/grades/session-details";
    }

    @GetMapping("/showFormForUpdateGrade")
    public String showFormForUpdateGrade(@RequestParam("subjectId") int subjectId, Model model){
        model.addAttribute("subject", subjectGradeService.findById(subjectId));
        model.addAttribute("grades", com.michaloruba.obslugasesji.helper.SubjectGrade.values());
        return "/grades/session-detail-form";
    }

    @PostMapping("/saveDetail")
    public String saveDetail(@ModelAttribute SubjectGrade subjectGrade, RedirectAttributes attributes){
        subjectGradeService.save(subjectGrade);

        attributes.addAttribute("sessionId", subjectGrade.getSession().getId());
        return "redirect:/grades/showSessionDetails";
    }
}
