package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.RoleRepository;
import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RoleServiceTest {
    @TestConfiguration
    static class RoleServiceImplTestContextConfiguration {
        @Autowired
        private RoleRepository roleRepository;

        @Bean
        public RoleServiceImpl roleService() {
            return new RoleServiceImpl(roleRepository);
        }
    }

    @Autowired
    private RoleService roleService;
    @MockBean
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        Role role = new Role("ROLE_TESTER");
        role.setId(1);
        Optional<Role> tester = Optional.of(role);

        when(roleRepository.findById(role.getId()))
                .thenReturn(tester);
        when(roleRepository.findAll())
                .thenReturn(List.of(role));
        when(roleRepository.findRoleByName(role.getName()))
                .thenReturn(role);
    }

    @Test
    public void whenFindById_ThenReturnRole() {
        int id = 1;
        Role found = roleService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        roleService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<Role> roles = roleService.findAll();
        assertThat(roles.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<Role> fields = roleService.findAll();
        assertThat(fields.get(0).getName()).isEqualTo("ROLE_TESTER");
        assertThat(fields.get(0).getId()).isEqualTo(1);
    }

    @Test
    public void whenSaveRole_ThenOneInteractionOccurs(){
        Role newRole = new Role("ROLE_PROGRAMMER");
        roleService.save(newRole);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void whenDeleteRole_ThenOneInteractionOccurs(){
        int id = 1;
        roleService.deleteById(id);
        verify(roleRepository, times(1)).deleteById(anyInt());
    }

    @Test (expected = NotFoundException.class)
    public void whenDeleteRoleWithWrongId_ThenThrowNotFoundException(){
        int id = -1;
        roleService.deleteById(id);
    }

    @Test
    public void whenFindByName_ThenReturnRole() {
        Role role = new Role("ROLE_TESTER");
        Role foundRole = roleRepository.findRoleByName(role.getName());
        assertThat(foundRole.getName()).isEqualTo(role.getName());
    }

    @Test
    public void whenFindByWrongName_ThenReturnNull() {
        Role foundRole = roleRepository.findRoleByName("WRONG_ROLE");
        assertThat(foundRole).isNull();
    }
}
