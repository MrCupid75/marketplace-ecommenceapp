package com.beast.ecommenceapp.service;

import com.beast.ecommenceapp.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
