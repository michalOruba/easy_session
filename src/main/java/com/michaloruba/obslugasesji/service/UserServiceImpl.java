package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.RoleRepository;
import com.michaloruba.obslugasesji.dao.UserRepository;
import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.user.CrmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


	/**
	 * Field injection was used to prevent circular bean dependency
	 */
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public User findByUserName(String userName) {

		return userRepository.findByUserName(userName);
	}

	@Override
	@Transactional
	public void save(CrmUser crmUser) {
		User user = new User();

		user.setUserName(crmUser.getUserName());
		user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
		user.setFirstName(crmUser.getFirstName());
		user.setLastName(crmUser.getLastName());
		user.setEmail(crmUser.getEmail());

		user.setRoles(Arrays.asList(roleRepository.findRoleByName("ROLE_STUDENT")));

		userRepository.save(user);
	}

	@Override
	@Transactional
	public void deleteByUserName(String userName) throws UsernameNotFoundException {
		if (userRepository.findByUserName(userName) == null){
			throw new UsernameNotFoundException("Not found User with user name - " + userName);
		}
		userRepository.deleteByUserName(userName);
	}

	@Override
	@Transactional
	public void update(User user) {
		User userToSave = findByUserName(user.getUserName());

		userToSave.setFirstName(user.getFirstName());
		userToSave.setLastName(user.getLastName());
		userToSave.setEmail(user.getEmail());

		userRepository.save(userToSave);

	}

	@Override
	@Transactional
	public void updateRoles(User user) {
		User userToSave = findByUserName(user.getUserName());
		userToSave.setRoles(user.getRoles());
		userRepository.save(userToSave);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
