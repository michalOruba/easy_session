package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/roles")
public class AdminRoleController {

    private RoleService roleService;

    @Autowired
    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public String showListOfRoles(Model model){
        model.addAttribute("roles", roleService.findAll());

        return "roles/roles-list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        model.addAttribute("role", new Role());
        return "roles/role-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("roleId") int roleId, Model model){
        try {
            model.addAttribute("role", roleService.findById(roleId));
        } catch (NotFoundException e){
            return "error-404";
        }

        return "roles/role-form";
    }

    @PostMapping("/save")
    public String saveRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "roles/role-form";
        }
        roleService.save(role);
        return "redirect:/admin/roles/list";
    }

    @GetMapping("/delete")
    public String deleteRole(@RequestParam("roleId") int roleId){
        try {
            roleService.deleteById(roleId);
        } catch (NotFoundException e){
            return "error-404";
        }

        return "redirect:/admin/roles/list";
    }
}
