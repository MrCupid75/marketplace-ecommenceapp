package com.beast.ecommenceapp.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beast.ecommenceapp.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
