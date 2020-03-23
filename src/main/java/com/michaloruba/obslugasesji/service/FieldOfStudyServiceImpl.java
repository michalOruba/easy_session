package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.FieldOfStudyRepository;
import com.michaloruba.obslugasesji.entity.FieldOfStudy;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldOfStudyServiceImpl implements FieldOfStudyService {

    FieldOfStudyRepository fieldOfStudyRepository;

    @Autowired
    public FieldOfStudyServiceImpl(FieldOfStudyRepository fieldOfStudyRepository) {
        this.fieldOfStudyRepository = fieldOfStudyRepository;
    }

    @Override
    public List<FieldOfStudy> findAll() {
        return fieldOfStudyRepository.findAll();
    }

    @Override
    public FieldOfStudy findById(int id) throws NotFoundException {
        Optional<FieldOfStudy> result = fieldOfStudyRepository.findById(id);
        FieldOfStudy fieldOfStudy;

        if(result.isPresent()){
            fieldOfStudy = result.get();
        }
        else {
            throw new NotFoundException("Did not find field of study id - " + id);
        }
        return fieldOfStudy;
    }

    @Override
    public void save(FieldOfStudy fieldOfStudy) {
        fieldOfStudyRepository.save(fieldOfStudy);
    }

    @Override
    public void deleteById(int id) throws NotFoundException {
        if (!fieldOfStudyRepository.findById(id).isPresent()){
            throw new NotFoundException("Not found Field with ID - " + id);
        }
        fieldOfStudyRepository.deleteById(id);
    }
}
