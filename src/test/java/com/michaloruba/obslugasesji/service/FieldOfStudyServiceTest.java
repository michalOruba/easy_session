package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.FieldOfStudyRepository;
import com.michaloruba.obslugasesji.entity.FieldOfStudy;
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
public class FieldOfStudyServiceTest {
    @TestConfiguration
    static class FieldOfStudyServiceImplTestContextConfiguration {
        @Autowired
        private FieldOfStudyRepository fieldOfStudyRepository;

        @Bean
        public FieldOfStudyService fieldOfStudyService() {
            return new FieldOfStudyServiceImpl(fieldOfStudyRepository);
        }
    }

    @Autowired
    private FieldOfStudyService fieldOfStudyService;
    @MockBean
    private FieldOfStudyRepository fieldOfStudyRepository;

    @Before
    public void setUp() {
        FieldOfStudy fieldOfStudy = new FieldOfStudy("Biology");
        fieldOfStudy.setId(1);
        Optional<FieldOfStudy> biology = Optional.of(fieldOfStudy);

        when(fieldOfStudyRepository.findById(fieldOfStudy.getId()))
                .thenReturn(biology);
        when(fieldOfStudyRepository.findAll())
                .thenReturn(List.of(fieldOfStudy));
    }

    @Test
    public void whenFindById_ThenReturnField() {
        int id = 1;
        FieldOfStudy found = fieldOfStudyService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        fieldOfStudyService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<FieldOfStudy> fields = fieldOfStudyService.findAll();
        assertThat(fields.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<FieldOfStudy> fields = fieldOfStudyService.findAll();
        assertThat(fields.get(0).getName()).isEqualTo("Biology");
        assertThat(fields.get(0).getId()).isEqualTo(1);
    }

    @Test
    public void whenSaveField_ThenOneInteractionOccurs(){
        FieldOfStudy newField = new FieldOfStudy("Mathematics");
        fieldOfStudyService.save(newField);
        verify(fieldOfStudyRepository, times(1)).save(any(FieldOfStudy.class));
    }

    @Test
    public void whenDeleteField_ThenOneInteractionOccurs(){
        int id = 1;
        fieldOfStudyService.deleteById(id);
        verify(fieldOfStudyRepository, times(1)).deleteById(anyInt());
    }

    @Test (expected = NotFoundException.class)
    public void whenDeleteFieldWithWrongId_ThenThrowNotFoundException(){
        int id = -1;
        fieldOfStudyService.deleteById(id);
    }
}
