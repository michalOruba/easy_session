package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SpecializationRepository;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.SpecializationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class SpecializationServiceTest {
    @TestConfiguration
    static class SpecializationServiceImplTestContextConfiguration {
        @Autowired
        private SpecializationRepository specializationRepository;

        @Bean
        public SpecializationServiceImpl roleService() {
            return new SpecializationServiceImpl(specializationRepository);
        }
    }

    @Autowired
    private SpecializationService specializationService;
    @MockBean
    private SpecializationRepository specializationRepository;

    @Before
    public void setUp() {
        InformationSpecialization specialization = new InformationSpecialization();
        specialization.setSpecKind(null);
        specialization.setStartDate(LocalDate.of(2020,5,1));
        specialization.setEndDate(LocalDate.of(2021,5,1));
        specialization.setId(1);
        Optional<InformationSpecialization> tester = Optional.of(specialization);

        when(specializationRepository.findById(specialization.getId()))
                .thenReturn(tester);
        when(specializationRepository.findAll())
                .thenReturn(List.of(specialization));
    }

    @Test
    public void whenFindById_ThenReturnSpec() {
        int id = 1;
        InformationSpecialization found = specializationService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        specializationService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<InformationSpecialization> specs = specializationService.findAll();
        assertThat(specs.size()).isEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<InformationSpecialization> specs = specializationService.findAll();
        assertThat(specs.get(0).getStartDate()).isEqualTo(LocalDate.of(2020,5,1));
        assertThat(specs.get(0).getEndDate()).isEqualTo(LocalDate.of(2021,5,1));
        assertThat(specs.get(0).getId()).isEqualTo(1);
        assertThat(specs.get(0).getSpecKind()).isNull();
    }

    @Test
    public void whenSaveSpec_ThenOneInteractionOccurs(){
        InformationSpecialization newSpec = new InformationSpecialization();
        newSpec.setSpecKind(null);
        newSpec.setStartDate(LocalDate.of(2020,5,1));
        newSpec.setEndDate(LocalDate.of(2021,5,1));
        newSpec.setId(2);

        specializationService.save(newSpec);
        verify(specializationRepository, times(1)).save(any(InformationSpecialization.class));
    }

    @Test
    public void whenDeleteSpec_ThenOneInteractionOccurs(){
        int id = 1;
        specializationService.deleteById(id);
        verify(specializationRepository, times(1)).deleteById(anyInt());
    }

    @Test (expected = NotFoundException.class)
    public void whenDeleteSpecWithWrongId_ThenThrowNotFoundException(){
        int id = -1;
        specializationService.deleteById(id);
    }
}
