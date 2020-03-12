package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.User;

public interface UserDao {

    User findByUserName(String userName);
    
    void save(User user);
    
}
