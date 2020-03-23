package com.michaloruba.obslugasesji.controller;


import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.service.UserService;
import com.michaloruba.obslugasesji.entity.CrmUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/showRegistrationForm")
    public String showRegistrationForm(Model model){
        model.addAttribute("crmUser", new CrmUser());
        return "registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(@Valid @ModelAttribute("crmUser") CrmUser crmUser, BindingResult bindingResult, Model model){
        String userName = crmUser.getUserName();
        if (bindingResult.hasErrors()){
            return "registration-form";
        }
        User existing = null;
        try {
            existing= userService.findByUserName(userName);
        } catch (UsernameNotFoundException e){
            logger.warn(e.getMessage());
        }
        if (existing != null){
            model.addAttribute("crmUser", new CrmUser());
            model.addAttribute("registrationError", "User name already exists.");
            logger.warn("User name already exists");
            return "registration-form";
        }
        userService.save(crmUser);
        logger.info("Successfully created user: {}", userName);
        model.addAttribute("register", "Registration successful");
        return "fancy-login";
    }
}