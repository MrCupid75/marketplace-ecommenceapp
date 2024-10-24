package com.beast.ecommenceapp.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beast.ecommenceapp.model.User;


public interface UserRepository extends JpaRepository<User, Long>   {

    User findByEmail(String email);    
}
