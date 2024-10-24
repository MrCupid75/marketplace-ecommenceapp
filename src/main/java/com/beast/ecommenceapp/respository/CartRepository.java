package com.beast.ecommenceapp.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beast.ecommenceapp.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
