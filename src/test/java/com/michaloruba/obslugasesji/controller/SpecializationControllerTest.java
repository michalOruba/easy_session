package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.config.CustomAuthenticationFailureHandler;
import com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler;
import com.michaloruba.obslugasesji.config.CustomLogoutSuccessHandler;
import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.SpecKind;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SpecKindService;
import com.michaloruba.obslugasesji.service.SpecializationService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SpecializationController.class)
public class SpecializationControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @MockBean
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @MockBean
    private SpecKindService specKindService;
    @MockBean
    private SpecializationService specializationService;
    @Autowired
    private MockMvc mvc;

    private static InformationSpecialization specialization;
    private static List<InformationSpecialization> specializations;
    private static SpecKind specKind;
    private static List<SpecKind> specKinds;

    @BeforeClass
    public static void setUp(){
        FieldOfStudy fieldOfStudy = new FieldOfStudy("Information Technology");
        fieldOfStudy.setId(1);

        specKind = new SpecKind();
        specKind.setId(1);
        specKind.setName("Programming");
        specKind.setFieldOfStudy(fieldOfStudy);
        specKinds = Arrays.asList(specKind);

        specialization = new InformationSpecialization();
        specialization.setId(1);
        specialization.setSpecKind(specKind);
        specialization.setStartDate(LocalDate.of(2020,5,1));
        specialization.setEndDate(LocalDate.of(2021,5,1));
        specializations = Arrays.asList(specialization);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void list_ShouldAddSpecializationsEntriesToModelAndRenderSpecializationsListView() throws Exception {
        given(specializationService.findAll()).willReturn(specializations);

        mvc.perform(get("/specs/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("specializations/specs-list"))
                .andExpect(model().attribute("specializations", hasSize(1)))
                .andExpect(model().attribute("specializations", hasItem(
                        allOf(
                                hasProperty("id", is(specialization.getId())),
                                hasProperty("startDate", is(specialization.getStartDate())),
                                hasProperty("endDate", is(specialization.getEndDate())),
                                hasProperty("specKind", is(specialization.getSpecKind()))
                        )
                )));
        verify(specializationService, times(1)).findAll();
        verifyNoMoreInteractions(specializationService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForSelectSpec_ShouldAddSpecializationKindsToModelAndRenderSpecializationKindSelectForm() throws Exception {
        when(specKindService.findAll()).thenReturn(specKinds);

        mvc.perform(get("/specs/showFormForSelectSpec"))
                .andExpect(status().isOk())
                .andExpect(view().name("specializations/specs-select-kind"))
                .andExpect(model().attribute("specKinds", hasSize(1)))
                .andExpect(model().attribute("specKinds", hasItem(
                        allOf(
                                hasProperty("id", is(specKind.getId())),
                                hasProperty("fieldOfStudy", is(specKind.getFieldOfStudy())),
                                hasProperty("name", is(specKind.getName()))
                        )
                )));
        verify(specKindService, times(1)).findAll();
        verifyNoMoreInteractions(specKindService);
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd_ShouldAddEmptySpecializationWithSelectedSpecKindAndRenderSpecializationForm() throws Exception {
        when(specKindService.findById(1)).thenReturn(specKind);
        when(specializationService.findAll()).thenReturn(specializations);

        mvc.perform(get("/specs/showFormForAdd")
                .param("specKindId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("specializations/spec-form"))
                .andExpect(model().attribute("specialization", hasProperty("id", is(0))))
                .andExpect(model().attribute("specialization", hasProperty("startDate", nullValue())))
                .andExpect(model().attribute("specialization", hasProperty("endDate", nullValue())))
                .andExpect(model().attribute("specialization", hasProperty("specKind", is(specKind))));
        verify(specKindService, times(1)).findById(1);
        verifyNoMoreInteractions(specKindService);
        verifyNoInteractions(specializationService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd_ShouldThrowNotFoundException_WhenWrongSpecKindPassed() throws Exception{
        when(specKindService.findById(-1)).thenThrow(NotFoundException.class);
        when(specKindService.findAll()).thenReturn(specKinds);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        specKindService.findById(-1);

        mvc.perform(get("/specs/showFormForAdd")
                .param("specKindId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "invalid specialization kind (Can not be null)"))
                .andExpect(model().attribute("specKinds", specKinds))
                .andExpect(view().name("/specializations/specs-select-kind"));
        verify(specKindService, times(2)).findById(-1);
        verify(specKindService, times(1)).findAll();
        verifyNoMoreInteractions(specKindService);
        verifyNoInteractions(specializationService);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldAddExistingSpecializationToModelAndRenderSpecializationForm() throws Exception {
        given(specializationService.findById(1)).willReturn(specialization);

        mvc.perform(get("/specs/showFormForUpdate")
                .param("specId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("specializations/spec-form"))
                .andExpect(model().attribute("specialization", hasProperty("id", is(specialization.getId()))))
                .andExpect(model().attribute("specialization", hasProperty("specKind", is(specialization.getSpecKind()))))
                .andExpect(model().attribute("specialization", hasProperty("startDate", is(specialization.getStartDate()))))
                .andExpect(model().attribute("specialization", hasProperty("endDate", is(specialization.getEndDate()))));
        verify(specializationService, times(1)).findById(1);
        verifyNoMoreInteractions(specializationService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void showFormForUpdate_ShouldThrowNotFoundException_WhenWrongIdPassed() throws Exception{
        when(specializationService.findById(-1)).thenThrow(NotFoundException.class);

        /*
         *   Method call added to perform NotFoundException.
         *   Exception was not thrown when mvc.perform was called.
         */
        specializationService.findById(-1);

        mvc.perform(get("/specs/showFormForUpdate")
                .param("specId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(specializationService, times(2)).findById(-1);
        verifyNoMoreInteractions(specializationService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldSaveSpecializationAndRedirectToSpecializationsList() throws Exception {
        doNothing().when(specializationService).save(specialization);

        mvc.perform(post("/specs/save")
                .with(csrf())
                .flashAttr("specialization", specialization)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/specs/list"));
        verify(specializationService, times(1)).save(isA(InformationSpecialization.class));
        verifyNoMoreInteractions(specializationService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void save_ShouldNotSaveSpecializationAndReturnFormView_WhenFormContainsErrors() throws Exception {
        doNothing().when(specializationService).save(isA(InformationSpecialization.class));

        mvc.perform(post("/specs/save")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("specialization", 2))
                .andExpect(model().attributeHasFieldErrors("specialization", "startDate"))
                .andExpect(model().attributeHasFieldErrors("specialization", "endDate"))
                .andExpect(view().name("specializations/spec-form"));
        verifyNoInteractions(specializationService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldDeleteSpecializationAndRedirectToSpecializationsList() throws Exception {
        doNothing().when(specializationService).deleteById(1);

        mvc.perform(get("/specs/delete")
                .param("specId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/specs/list"));
        verify(specializationService, times(1)).deleteById(1);
        verifyNoMoreInteractions(specializationService);
    }

    @Test (expected = NotFoundException.class)
    @WithMockUser(roles = "ADMIN")
    public void delete_ShouldNotDeleteFieldThrowNotFoundExceptionAndRedirectToErrorPage_WhenWrongIdPassed() throws Exception {
        doThrow(NotFoundException.class).when(specializationService).deleteById(-1);

        /*
        *   Method call added to perform NotFoundException.
        *   Exception was not thrown when mvc.perform was called.
        */
        specializationService.deleteById(-1);

        mvc.perform(get("/specs/delete")
                .param("specId", "-1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("/error-404"));
        verify(specializationService, times(2)).deleteById(-1);
        verifyNoMoreInteractions(specializationService);
    }


    @Test
    @WithMockUser(roles = "OWNER")
    public void givenOwnerAuthRequestOnSessionService_shouldSucceedWith200() throws Exception {
        given(specializationService.findById(1)).willReturn(specialization);
        given(specializationService.findAll()).willReturn(specializations);

        mvc.perform(get("/specs/list"))
                .andExpect(status().isOk());
        mvc.perform(get("/specs/showFormForAdd")
                .param("specKindId", "1"))
                .andExpect(status().isOk());
        mvc.perform(get("/specs/showFormForSelectSpec"))
                .andExpect(status().isOk());
        mvc.perform(get("/specs/showFormForUpdate")
                .param("specId", "1"))
                .andExpect(status().isOk());
        mvc.perform(post("/specs/save")
                .with(csrf()))
                .andExpect(status().isOk());
        mvc.perform(get("/specs/delete")
                .param("specId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnSessionServiceList_ShouldGive200() throws Exception {
        mvc.perform(get("/specs/list"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"STUDENT", "EMPLOYEE"})
    public void givenStudentOrEmployeeAuthRequestOnSessionService_shouldFailedWith403() throws Exception {
        mvc.perform(get("/specs/showFormForAdd"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/specs/showFormForUpdate"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/specs/save")
                    .with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(get("/specs/delete"))
                .andExpect(status().isForbidden());
    }
}