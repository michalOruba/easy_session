package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.service.RoleService;
import com.michaloruba.obslugasesji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedList;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminUsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String showListOfUsers(Model model){
        model.addAttribute("users", userService.findAll());
        return "/users/users-list";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "/users/user-form";
        }
        userService.update(user);

        return "redirect:/admin/users/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("userName") String userName, Model model){
        model.addAttribute("user", userService.findByUserName(userName));

        return "/users/user-form";
    }

    @GetMapping("/delete")
    public String deleteUserByUserName(@RequestParam("userName") String userName){
        userService.deleteByUserName(userName);
        return "redirect:/admin/users/list";
    }

    @GetMapping("/showFormForUpdateRoles")
    public String showFormForUpdateRoles(@RequestParam("userName") String userName, Model model){
        model.addAttribute("roles", roleService.findAll());

        model.addAttribute("user", userService.findByUserName(userName));

        return "/users/role-form";
    }

    @GetMapping("saveRole")
    public String saveRole(@ModelAttribute("userName") String userName, @RequestParam("roles") int[] roles){
        User user = userService.findByUserName(userName);
        Collection<Role> usersRoles = new LinkedList<>();
        for (int roleId : roles) {
                usersRoles.add(roleService.findById(roleId));
        }

        user.setRoles(usersRoles);
        userService.updateRoles(user);



        return "redirect:/admin/users/list";
    }
}
