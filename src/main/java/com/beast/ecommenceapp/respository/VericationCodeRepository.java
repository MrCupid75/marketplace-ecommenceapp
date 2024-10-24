package com.beast.ecommenceapp.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beast.ecommenceapp.model.VerificationCode;


public interface VericationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByEmail(String email);
}
