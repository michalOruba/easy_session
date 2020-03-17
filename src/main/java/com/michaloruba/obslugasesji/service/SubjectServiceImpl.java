package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SubjectRepository;
import com.michaloruba.obslugasesji.entity.InformationSpecialization;
import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findById(int id) {
        Optional<Subject> result = subjectRepository.findById(id);
        Subject subject;

        if (result.isPresent()){
            subject = result.get();
        }
        else {
            throw new NotFoundException("Not found Subject with ID - " + id);
        }

        return subject;
    }

    @Override
    public void save(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public void deleteById(int id) {
        if (!subjectRepository.findById(id).isPresent()){
            throw new NotFoundException("Not found Subject with ID - " + id);
        }
        subjectRepository.deleteById(id);
    }

    @Override
    public List<Subject> findAllBySemesterAndSpecialization(int semester, InformationSpecialization specialization) {
        return subjectRepository.findAllBySemesterAndSpecialization(semester, specialization);
    }

    @Override
    public Page<Subject> findByName(String name, Pageable pageable) {
        return subjectRepository.findByName(name, pageable);
    }
}
