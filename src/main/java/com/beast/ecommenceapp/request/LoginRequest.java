package com.beast.ecommenceapp.request;

import com.beast.ecommenceapp.domain.USER_ROLE;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String otp;
}
