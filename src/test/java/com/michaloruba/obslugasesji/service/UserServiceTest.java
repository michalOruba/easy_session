package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.RoleRepository;
import com.michaloruba.obslugasesji.dao.UserRepository;
import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.entity.CrmUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    private User user;

    @Before
    public void setUp() {
        Role role = new Role();
        role.setName("ROLE_STUDENT");
        role.setId(1);

        Collection<Role> roles = new ArrayList<>();
        roles.add(role);

        user = new User();
        user.setUserName("test");
        user.setPassword("Test");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@test.com");
        user.setRoles(roles);
        user.setId(1);

        when(userRepository.findByUserName(user.getUserName()))
                .thenReturn(user);
        when(userRepository.findAll())
                .thenReturn(Arrays.asList(user));
    }

    @Test
    public void whenFindByUserName_ThenReturnUser() {
        User found = userService.findByUserName(user.getUserName());
        assertThat(found.getId()).isEqualTo(user.getId());
    }

    @Test (expected = UsernameNotFoundException.class)
    public void whenFindWrongUserName_ThenReturnNull(){
        userService.findByUserName("Josh");
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<User> users = userService.findAll();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<User> fields = userService.findAll();
        assertThat(fields.get(0).getUserName()).isEqualTo(user.getUserName());
        assertThat(fields.get(0).getEmail()).isEqualTo(user.getEmail());
        assertThat(fields.get(0).getFirstName()).isEqualTo(user.getFirstName());
        assertThat(fields.get(0).getLastName()).isEqualTo(user.getLastName());
        assertThat(fields.get(0).getPassword()).isEqualTo(user.getPassword());
        assertThat(fields.get(0).getId()).isEqualTo(user.getId());
        assertThat(fields.get(0).getRoles().size()).isEqualTo(user.getRoles().size());
    }

    @Test
    public void whenSaveUser_ThenOneInteractionOccurs(){
        CrmUser newUser = new CrmUser();
        userService.save(newUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void whenDeleteUser_ThenOneInteractionOccurs(){
        userService.deleteByUserName(user.getUserName());
        verify(userRepository, times(1)).deleteByUserName(anyString());
    }

    @Test (expected = UsernameNotFoundException.class)
    public void whenDeleteUserWithWrongUserName_ThenThrowUsernameNotFoundException(){
        userService.deleteByUserName("Josh");
    }

    @Test
    public void whenUpdateUser_ThenOneInteractionOccurs(){
        userService.update(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void whenLoadUserByUsername_ThenReturnNewUserDetails(){
        UserDetails foundUserDetails = userService.loadUserByUsername(user.getUserName());
        assertThat(foundUserDetails.getUsername()).isEqualTo(user.getUserName());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void whenLoadUserByWrongUsername_ThenThrowUsernameNotFoundException(){
        userService.loadUserByUsername("Josh");
    }
}
