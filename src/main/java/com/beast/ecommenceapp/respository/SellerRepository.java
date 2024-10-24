package com.beast.ecommenceapp.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beast.ecommenceapp.model.AccountStatus;
import com.beast.ecommenceapp.model.Seller;


public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus accountStatus);
}
