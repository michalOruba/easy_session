package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.helper.SessionStatus;
import com.michaloruba.obslugasesji.service.SessionService;
import com.michaloruba.obslugasesji.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sessions")
public class SessionController {
    private SessionService sessionService;
    private StudentService studentService;

    public SessionController(SessionService sessionService, StudentService studentService) {
        this.sessionService = sessionService;
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public String showSessionList(Model model){
        model.addAttribute("mySessions", sessionService.findAll());

        return "sessions/session-list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        Session session = new Session();

        model.addAttribute("students", studentService.findAll());
        model.addAttribute("sessionStatus", SessionStatus.values());
        model.addAttribute("mySession", session);

        return "sessions/session-form";
    }

    @PostMapping("/save")
    public String saveSession(@ModelAttribute Session session){
        sessionService.save(session);
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
