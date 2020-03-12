package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.user.CrmUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	public User findByUserName(String userName);

	public void save(CrmUser crmUser);
}
