package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.Role;
import com.michaloruba.obslugasesji.entity.User;
import com.michaloruba.obslugasesji.service.RoleService;
import com.michaloruba.obslugasesji.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
@WebMvcTest(AdminUserController.class)
public class AdminUserControllerTest {
    @MockBean
    private RoleService roleService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;

    static private List<Role> roles;
    static private User user;
    static List<User> users;
    static Role role;

    @BeforeClass
    public static void setUp(){
        role = new Role("ROLE_STUDENT");
        role.setId(1);
        roles = Arrays.asList(role);

        user = new User();
        user.setId(1);
        user.setUserName("test");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test@test.com");
        user.setRoles(roles);
        users = Arrays.asList(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void list_ShouldAddRoleEntriesToModelAndRenderRolesListView() throws Exception {


        given(userService.findAll()).willReturn(users);

        mvc.perform(get("/admin/users/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/users-list"))
                .andExpect(model().attribute("users", hasSize(1)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("userName", is("test")),
                                hasProperty("password", is("test")),
                                hasProperty("firstName", is("test")),
                                hasProperty("lastName", is("test")),
                                hasProperty("email", is("test@test.com"))
                        )
                )));
        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldUpdateExistingUserAndRenderUserForm() throws Exception {
        given(userService.findByUserName("test")).willReturn(user);

        mvc.perform(get("/admin/users/showFormForUpdate").param("userName", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/user-form"));
        verify(userService, times(1)).findByUserName("test");
        verifyNoMoreInteractions(userService);
    }

    @Test (expected = UsernameNotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldThrowUsernameNotFoundExceptionWhenWrongIdPassed() throws Exception{
        when(userService.findByUserName("NONAME")).thenThrow(UsernameNotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        userService.findByUserName("NONAME");

        mvc.perform(get("/admin/users/showFormForUpdate").param("userName", "NONAME"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(userService, times(2)).findByUserName("NONAME");
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdateRole_ShouldUpdateExistingUsersRoleAndRenderUserRoleForm() throws Exception {
        given(userService.findByUserName("test")).willReturn(user);
        given(roleService.findAll()).willReturn(roles);

        mvc.perform(get("/admin/users/showFormForUpdateRoles")
                    .param("userName", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/role-form"))
                .andExpect(model().attribute("user", hasProperty("id", is(user.getId()))))
                .andExpect(model().attribute("user", hasProperty("firstName", is(user.getFirstName()))))
                .andExpect(model().attribute("user", hasProperty("lastName", is(user.getLastName()))))
                .andExpect(model().attribute("user", hasProperty("email", is(user.getEmail()))))
                .andExpect(model().attribute("roles", hasSize(1)))
                .andExpect(model().attribute("roles", hasItem(
                    allOf(
                            hasProperty("name", is("ROLE_STUDENT"))
                    )
                )));
        verify(userService, times(1)).findByUserName("test");
        verifyNoMoreInteractions(userService);
        verify(roleService, times(1)).findAll();
        verifyNoMoreInteractions(roleService);
    }

    @Test (expected = UsernameNotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdateRole_ShouldThrowUsernameNotFoundExceptionWhenWrongUsernamePassed() throws Exception{
        when(userService.findByUserName("NONAME")).thenThrow(UsernameNotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        userService.findByUserName("NONAME");

        mvc.perform(get("/admin/users/showFormForUpdateRole").param("userName", "NONAME"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(userService, times(2)).findByUserName("NONAME");
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldSaveUserAndRedirectToUsersList() throws Exception {
        doNothing().when(userService).update(isA(User.class));

        mvc.perform(post("/admin/users/save")
                    .with(csrf())
                    .param("firstName" ,user.getFirstName())
                    .param("lastName", user.getLastName())
                    .param("email", "test@gmail.com")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/list"));
        verify(userService, times(1)).update(isA(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveUserAndReturnUserFormView() throws Exception {
        doNothing().when(userService).update(isA(User.class));

        mvc.perform(post("/admin/users/save")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("user", 4)) /* Two errors for email, one for firstName and one for lastName */
                .andExpect(model().attributeHasFieldErrors("user", "firstName"))
                .andExpect(model().attributeHasFieldErrors("user", "lastName"))
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(view().name("/users/user-form"));
        verifyNoInteractions(userService);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveRole_ShouldSaveUserRoleAndRedirectToUsersListWhenRolesAreNotNull() throws Exception {
        when(userService.findByUserName("test")).thenReturn(user);
        when(roleService.findById(1)).thenReturn(role);
        doNothing().when(userService).updateRoles(isA(User.class));

        mvc.perform(post("/admin/users/saveRole")
                .with(csrf())
                .param("userName" ,user.getUserName())
                .param("roles", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/list"));
        verify(roleService, times(1)).findById(1);
        verifyNoMoreInteractions(roleService);
        verify(userService, times(1)).findByUserName("test");
        verify(userService, times(1)).updateRoles(isA(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveRole_ShouldSaveUserRoleAndRedirectToUsersListWhenRolesAreNull() throws Exception {
        when(userService.findByUserName("test")).thenReturn(user);
        doNothing().when(userService).updateRoles(isA(User.class));

        mvc.perform(post("/admin/users/saveRole")
                .with(csrf())
                .param("userName" ,user.getUserName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/list"));
        verify(userService, times(1)).findByUserName("test");
        verify(userService, times(1)).updateRoles(isA(User.class));
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(roleService);
    }

    @Test (expected = UsernameNotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveUserRoleAndReturnUserFormView_whenUsernameNotFound() throws Exception {
        doNothing().when(userService).updateRoles(isA(User.class));
        when(userService.findByUserName("NONAME")).thenThrow(UsernameNotFoundException.class);

        /*
         *   Method call added to perform UsernameNotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        userService.findByUserName("NONAME");

        mvc.perform(post("/admin/users/save")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(userService, times(2)).findByUserName("NONAME");
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldDeleteUserAndRedirectToUserList() throws Exception {
        doNothing().when(userService).deleteByUserName("test");

        mvc.perform(get("/admin/users/delete")
                    .param("userName", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/list"));
        verify(userService, times(1)).deleteByUserName("test");
        verifyNoMoreInteractions(userService);
    }

    @Test (expected = UsernameNotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldNotDeleteUserThrowNotFoundExceptionAndRedirectToErrorPage() throws Exception {
        doThrow(UsernameNotFoundException.class).when(userService).deleteByUserName("NONAME");

        /*
        *   Method call added to perform UsernameNotFoundException.
        *   Exception was not thrown when mvc.perform was called.
        */
        userService.deleteByUserName("NONAME");

        mvc.perform(get("/admin/users/delete")
                .param("userName", "NONAME"))
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(userService, times(2)).deleteByUserName("NONAME");
        verifyNoMoreInteractions(userService);
    }


    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnRoleService_shouldSucceedWith200OnlyForList() throws Exception {
        mvc.perform(get("/admin/users/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/admin/users/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/admin/users/save")
                .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/users/delete"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnRoleService_shouldSucceedWith403() throws Exception {
        mvc.perform(get("/admin/users/list"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/users/showFormForUpdateRoles"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/users/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/admin/users/save")
                    .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/users/delete"))
                .andExpect(status().isForbidden());
    }
}