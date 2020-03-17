package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.helper.SessionStatus;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SessionService;
import com.michaloruba.obslugasesji.service.StudentService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/sessions")
public class SessionController {
    private SessionService sessionService;
    private StudentService studentService;

    public SessionController(SessionService sessionService, StudentService studentService) {
        this.sessionService = sessionService;
        this.studentService = studentService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String showListOfSessions(Model model){
        model.addAttribute("mySessions", sessionService.findAll());

        return "/sessions/session-list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        Session session = new Session();

        model.addAttribute("students", studentService.findAll());
        model.addAttribute("sessionStatus", SessionStatus.values());
        model.addAttribute("mySession", session);

        return "/sessions/session-form";
    }

    @PostMapping("/save")
    public String saveSession(@Valid @ModelAttribute("mySession") Session mySession, BindingResult bindingResult, Model model){
        try {
            if (mySession.getStudent() == null) throw new NotFoundException("Student not found");
            studentService.findById(mySession.getStudent().getId());
        } catch (NotFoundException e){
            bindingResult.rejectValue("student", "error.mySession", "invalid student (can not be null)");
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("students", studentService.findAll());
            model.addAttribute("sessionStatus", SessionStatus.values());
            return "/sessions/session-form";
        }

        sessionService.save(mySession);
        return "redirect:/sessions/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("sessionId") int sessionId, Model model){
        model.addAttribute("mySession", sessionService.findById(sessionId));
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("sessionStatus", SessionStatus.values());

        return "/sessions/session-form";
    }

    @GetMapping("/delete")
    public String deleteSession(@RequestParam("sessionId") int sessionId){
        sessionService.deleteById(sessionId);

        return "redirect:/sessions/list";
    }


}
