package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
    Role findById(int id);
    Role findRoleByName(String name);
    void save(Role role);
    void deleteById(int id);
}
