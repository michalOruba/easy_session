package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findRoleByName(String theRoleName);
	
}
