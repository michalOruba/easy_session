package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.entity.CrmUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
	List<User> findAll();
	User findByUserName(String userName);
	void save(CrmUser crmUser);
	void deleteByUserName(String userName);
	void update(User user);
	void updateRoles(User user);
}
