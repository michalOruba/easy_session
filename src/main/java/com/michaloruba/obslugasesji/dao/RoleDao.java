package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
