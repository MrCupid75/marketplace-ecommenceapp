package com.beast.ecommenceapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beast.ecommenceapp.domain.USER_ROLE;
import com.beast.ecommenceapp.model.VerificationCode;
import com.beast.ecommenceapp.request.LoginRequest;
import com.beast.ecommenceapp.request.SignupRequest;
import com.beast.ecommenceapp.response.ApiResponse;
import com.beast.ecommenceapp.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {

        String jwt = authService.createUser(req);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
       
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest req) throws Exception {

        AuthResponse authResponse = authService.loginUser(req);
        
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/sent/sent-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode req) throws Exception {
        authService.sentLoginOtp(req.getEmail());
        ApiResponse res = new ApiResponse();
        res.setMessage("OTP sent to email");
        return ResponseEntity.ok(res);
    }

}
