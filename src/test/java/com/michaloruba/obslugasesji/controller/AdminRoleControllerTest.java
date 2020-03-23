package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.RoleService;
import com.michaloruba.obslugasesji.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminRoleController.class)
public class AdminRoleControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @MockBean
    private RoleService roleService;
    @Autowired
    private MockMvc mvc;

    private static Role role;
    private static List<Role> roles;
    @BeforeClass
    public static void setUp(){
        role = new Role("ROLE_TESTER");
        role.setId(1);
        roles = Arrays.asList(role);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void list_ShouldAddRoleEntriesToModelAndRenderRolesListView() throws Exception {
        given(roleService.findAll()).willReturn(roles);

        mvc.perform(get("/admin/roles/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/roles/roles-list"))
                .andExpect(model().attribute("roles", hasSize(1)))
                .andExpect(model().attribute("roles", hasItem(
                        allOf(
                                hasProperty("name", is("ROLE_TESTER"))
                        )
                )));
        verify(roleService, times(1)).findAll();
        verifyNoMoreInteractions(roleService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd_ShouldAddEmptyRoleAndRenderRoleForm() throws Exception {
        mvc.perform(get("/admin/roles/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("/roles/role-form"))
                .andExpect(model().attribute("role", hasProperty("id", is(0))))
                .andExpect(model().attribute("role", hasProperty("name", nullValue())));
        verifyNoInteractions(roleService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldAddExistingRoleAndRenderRoleForm() throws Exception {
        given(roleService.findById(1)).willReturn(role);

        mvc.perform(get("/admin/roles/showFormForUpdate")
                .param("roleId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/roles/role-form"))
                .andExpect(model().attribute("role", hasProperty("id", is(1))))
                .andExpect(model().attribute("role", hasProperty("name", is(role.getName()))));
        verify(roleService, times(1)).findById(1);
        verifyNoMoreInteractions(roleService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldThrowNotFoundExceptionWhenWrongIdPassed() throws Exception{
        when(roleService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        roleService.findById(-1);

        mvc.perform(get("/admin/roles/showFormForUpdate")
                .param("roleId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(roleService, times(2)).findById(-1);
        verifyNoMoreInteractions(roleService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldSaveRoleAndRedirectToRolesList() throws Exception {
        doNothing().when(roleService).save(isA(Role.class));

        mvc.perform(post("/admin/roles/save")
                    .with(csrf())
                    .param("id", "" + role.getId())
                    .param("name", role.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/roles/list"));
        verify(roleService, times(1)).save(isA(Role.class));
        verifyNoMoreInteractions(roleService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveRoleAndReturnFormView_WhenFormContainsErrors() throws Exception {
        doNothing().when(roleService).save(isA(Role.class));

        mvc.perform(post("/admin/roles/save")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("role", 1))
                .andExpect(model().attributeHasFieldErrors("role", "name"))
                .andExpect(view().name("/roles/role-form"));
        verifyNoInteractions(roleService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldDeleteUserAndRedirectToRolesList() throws Exception {
        doNothing().when(roleService).deleteById(1);

        mvc.perform(get("/admin/roles/delete")
                    .param("roleId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/roles/list"));
        verify(roleService, times(1)).deleteById(1);
        verifyNoMoreInteractions(roleService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldNotDeleteUserThrowNotFoundExceptionAndRedirectToErrorPage_WhenWrongIdPassed() throws Exception {
        doThrow(NotFoundException.class).when(roleService).deleteById(-1);

        /*
        *   Method call added to perform NotFoundException.
        *   Exception was not thrown when mvc.perform was called.
        */
        roleService.deleteById(-1);

        mvc.perform(get("/admin/roles/delete")
                .param("roleId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(roleService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(roleService);
    }


    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnRoleService_shouldSucceedWith200OnlyForList() throws Exception {
        mvc.perform(get("/admin/roles/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/admin/roles/showFormForAdd"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/roles/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/admin/roles/save")
                .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/roles/delete"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnRoleService_shouldFailedWith403() throws Exception {
        mvc.perform(get("/admin/roles/list"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/roles/showFormForAdd"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/roles/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/admin/roles/save")
                    .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/roles/delete"))
                .andExpect(status().isForbidden());
    }
}