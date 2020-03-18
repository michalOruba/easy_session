package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.dao.RoleRepository;
import com.michaloruba.obslugasesji.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenFindByName_ThenReturnRole() {
        Role role = new Role("ROLE_TESTER");
        entityManager.persist(role);
        entityManager.flush();

        Role foundRole = roleRepository.findRoleByName(role.getName());
        assertThat(foundRole.getName()).isEqualTo(role.getName());
    }

    @Test
    public void whenFindByWrongName_ThenReturnNull(){
        Role foundRole = roleRepository.findRoleByName(" ");
        assertThat(foundRole).isNull();
    }

}
