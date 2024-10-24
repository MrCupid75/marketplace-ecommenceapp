package com.beast.ecommenceapp.controller;

import com.beast.ecommenceapp.domain.USER_ROLE;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
    
} 