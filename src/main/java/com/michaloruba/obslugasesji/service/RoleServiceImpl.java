package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.RoleRepository;
import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(int id) {
        Optional<Role> result = roleRepository.findById(id);
        Role role;
        if (result.isPresent()){
            role = result.get();
        }
        else throw new NotFoundException("Not found Role with id - " + id);

        return role;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteById(int id) {
        Optional<Role> result = roleRepository.findById(id);
        if (!result.isPresent()){
            throw new NotFoundException("Not found Role with id - " + id);
        }
        roleRepository.deleteById(id);
    }
}
