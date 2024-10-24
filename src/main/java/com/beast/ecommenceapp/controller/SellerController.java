package com.beast.ecommenceapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beast.ecommenceapp.config.JwtProvider;
import com.beast.ecommenceapp.domain.USER_ROLE;
import com.beast.ecommenceapp.model.Seller;
import com.beast.ecommenceapp.request.LoginRequest;
import com.beast.ecommenceapp.request.SignupRequest;
import com.beast.ecommenceapp.service.AuthService;
import com.beast.ecommenceapp.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

        req.setEmail("seller_" + req.getEmail());
        //req.setRole(USER_ROLE.ROLE_SELLER);
        AuthResponse response = authService.loginUser(req);

        return ResponseEntity.ok(response);
    }

    /* @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupSeller(@RequestBody SignupRequest req) throws Exception {

        
    } */




    @GetMapping("/profile")
    public ResponseEntity<Seller> showSellerProfile(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);

        return ResponseEntity.ok(seller);
    
    }
}
