package com.michaloruba.obslugasesji.service;

import com.michaloruba.obslugasesji.dao.SubjectGradeRepository;
import com.michaloruba.obslugasesji.entity.Session;
import com.michaloruba.obslugasesji.entity.SubjectGrade;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectGradeServiceImpl implements SubjectGradeService {

    private SubjectGradeRepository subjectGradeRepository;

    @Autowired
    public SubjectGradeServiceImpl(SubjectGradeRepository subjectGradeRepository) {
        this.subjectGradeRepository = subjectGradeRepository;
    }

    @Override
    public List<SubjectGrade> findAll() {
        return subjectGradeRepository.findAll();
    }

    @Override
    public SubjectGrade findById(int id) {

        Optional<SubjectGrade> result = subjectGradeRepository.findById(id);
        SubjectGrade subjectGrade;

        if (result.isPresent()){
            subjectGrade = result.get();
        }
        else {
            throw new NotFoundException("Not found SubjectGradeList with id - " + id);
        }

        return subjectGrade;
    }

    @Override
    public void save(SubjectGrade subjectGrade) {
        subjectGradeRepository.save(subjectGrade);
    }

    @Override
    public void deleteById(int id) {
        if (subjectGradeRepository.findById(id).isPresent()){
            subjectGradeRepository.deleteById(id);
        }
        else {
            throw new NotFoundException("Not found SubjectGradeList with id - " + id);
        }
    }

    @Override
    public List<SubjectGrade> findAllBySession(Session session) {
        return subjectGradeRepository.findAllBySession(session);
    }
}
