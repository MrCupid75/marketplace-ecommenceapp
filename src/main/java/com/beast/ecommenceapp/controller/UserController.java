package com.beast.ecommenceapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.beast.ecommenceapp.model.User;
import com.beast.ecommenceapp.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/profile")
    public ResponseEntity<User> showUserProfile(@RequestHeader ("Authorization") String jwt) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);

        return ResponseEntity.ok(user);
    }

    
}
