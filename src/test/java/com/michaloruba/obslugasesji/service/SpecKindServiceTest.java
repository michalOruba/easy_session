package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SpecKindRepository;
import com.michaloruba.obslugasesji.entity.SpecKind;
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
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SpecKindServiceTest {
    @TestConfiguration
    static class SpecKindServiceImplTestContextConfiguration {
        @Autowired
        private SpecKindRepository specKindRepository;

        @Bean
        public SpecKindService specKindService() {
            return new SpecKindServiceImpl(specKindRepository);
        }
    }

    @Autowired
    private SpecKindService specKindService;
    @MockBean
    private SpecKindRepository specKindRepository;

    @Before
    public void setUp() {
        SpecKind specKind = new SpecKind("Programming");
        specKind.setId(1);
        Optional<SpecKind> tester = Optional.of(specKind);

        when(specKindRepository.findById(specKind.getId()))
                .thenReturn(tester);
        when(specKindRepository.findAll())
                .thenReturn(List.of(specKind));
    }

    @Test
    public void whenFindById_ThenReturnRole() {
        int id = 1;
        SpecKind found = specKindService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test (expected = NotFoundException.class)
    public void whenFindWrongId_ThenThrowNotFoundException(){
        int id = -1;
        specKindService.findById(id);
    }

    @Test
    public void whenFindAll_ThenReturnList() {
        List<SpecKind> specKinds = specKindService.findAll();
        assertThat(specKinds.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void whenFindAll_ThenReturnsCorrectFields() {
        List<SpecKind> specKinds = specKindService.findAll();
        assertThat(specKinds.get(0).getName()).isEqualTo("Programming");
        assertThat(specKinds.get(0).getId()).isEqualTo(1);
        assertThat(specKinds.get(0).getFieldOfStudy()).isNull();
    }
}
