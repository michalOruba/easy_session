package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public StudentRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Student> searchForStudent(String name) {

      return entityManager.createQuery("FROM Student WHERE firstName LIKE ?1 or lastName LIKE ?1", Student.class)
                .setParameter(1, "%" + name + "%")
                .getResultList();
    }

    @Override
    public List<Student> searchForStudent(int id) {
        return entityManager.createQuery("FROM Student WHERE id=?1", Student.class)
                .setParameter(1, id)
                .getResultList();
    }
}
