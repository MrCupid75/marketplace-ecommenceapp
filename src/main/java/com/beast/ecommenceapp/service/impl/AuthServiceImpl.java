package com.beast.ecommenceapp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beast.ecommenceapp.config.JwtProvider;
import com.beast.ecommenceapp.controller.AuthResponse;
import com.beast.ecommenceapp.domain.USER_ROLE;
import com.beast.ecommenceapp.model.Cart;
import com.beast.ecommenceapp.model.User;
import com.beast.ecommenceapp.model.VerificationCode;
import com.beast.ecommenceapp.request.LoginRequest;
import com.beast.ecommenceapp.request.SignupRequest;
import com.beast.ecommenceapp.respository.CartRepository;
import com.beast.ecommenceapp.respository.UserRepository;
import com.beast.ecommenceapp.respository.VericationCodeRepository;
import com.beast.ecommenceapp.service.AuthService;
import com.beast.ecommenceapp.service.EmailService;
import com.beast.ecommenceapp.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomeUserService customeUserService;
    private final VericationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    @Override
    public String createUser(SignupRequest req) throws Exception {


        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        
        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("wrong otp");
        }

        User user = userRepository.findByEmail(req.getEmail());

        //String SIGNING_PREFIX = "signing"

        if (user == null ) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("1234567890");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        } else if (user.getEmail().equals(req.getEmail())) {
                throw new Exception("User already exists");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return jwtProvider.generateToken(authentication);
    }
    
    public AuthResponse loginUser(LoginRequest req) throws Exception {

        String username = req.getEmail();
        String password = req.getOtp();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Successful");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
       
        UserDetails userDetails = customeUserService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("Invalid otp");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public void sentLoginOtp(String email) throws Exception {
        
        String SIGNING_PREFIX = "signing_";

        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            User user = userRepository.findByEmail(email);

            if (user == null) {
                throw new Exception("User not found with this email" + email);
            }
        }

        VerificationCode isExisting = verificationCodeRepository.findByEmail(email);

        if (isExisting != null) {
            verificationCodeRepository.delete(isExisting);
        }

        String otp = OtpUtil.generateOtp(); 

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);

        String subject = "OTP for login";
        String text = "Your OTP is: " + otp;

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

}    