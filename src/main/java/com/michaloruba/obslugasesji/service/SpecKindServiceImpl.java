package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SpecKindRepository;
import com.michaloruba.obslugasesji.entity.SpecKind;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecKindServiceImpl implements SpecKindService{
    private SpecKindRepository specKindRepository;

    @Autowired
    public SpecKindServiceImpl(SpecKindRepository specKindRepository) {
        this.specKindRepository = specKindRepository;
    }


    @Override
    public List<SpecKind> findAll() {
        return specKindRepository.findAll();
    }

    @Override
    public SpecKind findById(int kindId) {
        Optional<SpecKind> result = specKindRepository.findById(kindId);
        SpecKind specKind = null;

        if (result.isPresent()){
            specKind = result.get();
        }
        else {
            throw new NotFoundException("Not found Spec Kind with id - " + kindId);
        }

        return specKind;
    }

    @Override
    public List<SpecKind> findByFieldOfStudy_Id(int id) {
        return specKindRepository.findByFieldOfStudy_Id(id);
    }
}
