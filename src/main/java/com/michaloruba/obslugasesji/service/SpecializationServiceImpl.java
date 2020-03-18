package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SpecializationRepository;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    private SpecializationRepository specializationRepository;

    @Autowired
    public SpecializationServiceImpl(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    @Override
    public List<InformationSpecialization> findAll() {
        return specializationRepository.findAll();
    }

    @Override
    public InformationSpecialization findById(int id) {
        Optional<InformationSpecialization> result = specializationRepository.findById(id);
        InformationSpecialization specialization = null;

        if (result.isPresent()){
            specialization = result.get();
        }
        else{
            throw new NotFoundException("Not found specialization with id - " + id);
        }
        return specialization;
    }

    @Override
    public void save(InformationSpecialization specialization) {
        specializationRepository.save(specialization);
    }

    @Override
    public void deleteById(int id) {
        Optional<InformationSpecialization> result = specializationRepository.findById(id);
        if (!result.isPresent()){
            throw new NotFoundException("Not found specialization with id - " + id);
        }
        specializationRepository.deleteById(id);
    }
}
