package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.SubjectGrade;
import com.michaloruba.obslugasesji.helper.SubjectGradeTypes;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SessionService;
import com.michaloruba.obslugasesji.service.SubjectGradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
@RequestMapping("/grades")
public class SubjectGradeController {
    private SessionService sessionService;
    private SubjectGradeService subjectGradeService;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    public SubjectGradeController(SessionService sessionService, SubjectGradeService subjectGradeService) {
        this.sessionService = sessionService;
        this.subjectGradeService = subjectGradeService;
    }

    @GetMapping("/showSessionDetails")
    public String showSessionDetails(@RequestParam("sessionId") int sessionId , Model model){
        Session foundSession;
        try {
            foundSession = sessionService.findById(sessionId);
        } catch (NotFoundException e){
            logger.warn(e.getMessage());
            return "error-404";
        }
        model.addAttribute("mySession", foundSession);
        model.addAttribute("subjectGrades", subjectGradeService.findAllBySession(foundSession));
        return "grades/session-details";
    }

    @GetMapping("/showFormForUpdateGrade")
    public String showFormForUpdateGrade(@RequestParam("subjectGradeId") int subjectGradeId, Model model){
        try {
            model.addAttribute("subject", subjectGradeService.findById(subjectGradeId));
        } catch (NotFoundException e){
            return "error-404";
        }
        model.addAttribute("grades", SubjectGradeTypes.values());
        return "grades/session-detail-form";
    }

    @PostMapping("/saveDetail")
    public String saveDetail(@Valid @ModelAttribute("subject") SubjectGrade subjectGrade, BindingResult bindingResult, RedirectAttributes attributes, Model model){
        if (bindingResult.hasErrors()){
            logger.warn(bindingResult.toString());
            model.addAttribute("grades", SubjectGradeTypes.values());
            return "grades/session-detail-form";
        }
        subjectGradeService.save(subjectGrade);
        attributes.addAttribute("sessionId", subjectGrade.getSession().getId());
        return "redirect:/grades/showSessionDetails";
    }
}
