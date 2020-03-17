package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();
    User findByUserName(String userName);
    void save(User user);
    void deleteByUserName(String userName);
    
}
