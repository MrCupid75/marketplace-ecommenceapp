package com.beast.ecommenceapp.service;

import com.beast.ecommenceapp.controller.AuthResponse;
import com.beast.ecommenceapp.request.LoginRequest;
import com.beast.ecommenceapp.request.SignupRequest;

public interface AuthService {

    void sentLoginOtp(String email) throws Exception;
    String createUser (SignupRequest req) throws Exception;
    AuthResponse loginUser (LoginRequest req) throws Exception ;
}
