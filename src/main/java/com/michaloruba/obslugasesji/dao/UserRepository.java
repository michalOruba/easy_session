package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    void deleteByUserName(String userName);
}
