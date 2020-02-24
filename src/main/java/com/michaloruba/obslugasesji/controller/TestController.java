package com.michaloruba.obslugasesji.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String sayHello(Model model){
        model.addAttribute("theTime",new java.util.Date());
        return "hello_world";
    }

}
