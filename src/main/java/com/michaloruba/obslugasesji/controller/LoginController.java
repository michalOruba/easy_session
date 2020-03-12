package com.michaloruba.obslugasesji.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(){
        return "fancy-login";
    }

    @GetMapping("/access-denied")
    public String showGetAccessDenied(){
        return "access-denied";
    }

    @PostMapping("/access-denied")
    public String showPostAccessDenied(){
        return "access-denied";
    }

}
