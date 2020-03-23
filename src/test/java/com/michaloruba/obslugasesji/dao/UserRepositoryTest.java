package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private User user;

    @Before
    public void setUp(){
        Role role = new Role("ROLE_STUDENT");
        entityManager.persistAndFlush(role);

        Collection<Role> roles = new ArrayList<>();
        roles.add(role);

        user = new User();
        user.setUserName("test");
        user.setPassword("Test");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@test.com");
        user.setRoles(roles);
        entityManager.persistAndFlush(user);
    }

    @Test
    public void whenFindByUserName_ThenReturnUser(){
        User foundUser = userRepository.findByUserName(user.getUserName());
        assertThat(foundUser.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    public void whenFindByWrongUserName_ThenReturnNull(){
        User foundUser = userRepository.findByUserName("Josh");
        assertThat(foundUser).isNull();
    }

    @Test
    public void whenDeleteByUserName_ThenFindByUserNameReturnsNull(){
        userRepository.deleteByUserName(user.getUserName());
        User foundUser = userRepository.findByUserName(user.getUserName());
        assertThat(foundUser).isNull();
    }

    @Test
    public void whenDeleteByWrongUserName_ThenExistingUserIsNotDeleted(){
        userRepository.deleteByUserName("Josh");
        User foundUser = userRepository.findByUserName(user.getUserName());
        assertThat(foundUser.getUserName()).isEqualTo(user.getUserName());
    }
}
