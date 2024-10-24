package com.beast.ecommenceapp.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beast.ecommenceapp.config.JwtProvider;
import com.beast.ecommenceapp.domain.USER_ROLE;
import com.beast.ecommenceapp.model.AccountStatus;
import com.beast.ecommenceapp.model.Address;
import com.beast.ecommenceapp.model.Seller;
import com.beast.ecommenceapp.respository.AddressRepository;
import com.beast.ecommenceapp.respository.SellerRepository;
import com.beast.ecommenceapp.service.SellerService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService { 

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
       
        Seller sellerExists = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExists != null) {
            throw new Exception("Seller already exists, use a different email");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());
        
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws Exception {
        return sellerRepository.findById(id).orElseThrow(() -> new Exception("Seller not found" + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
       
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("Seller not found");
        }

        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) throws Exception {
        
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
       Seller existingSeller = this.getSellerById(id);

       if (seller.getSellerName() != null) {
        existingSeller.setSellerName(seller.getSellerName());
       }

       if (seller.getMobile() != null) {
        existingSeller.setMobile(seller.getMobile());
       }

       if (seller.getEmail() != null) {
        existingSeller.setEmail(seller.getEmail());
       }

       if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
        existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
       }

       if (seller.getBankDetails() != null 
       && seller.getBankDetails().getAccountHolderName() != null
       && seller.getBankDetails().getIfscCode() != null
       && seller.getBankDetails().getAccountNumber() != null) {

        existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
        existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
        existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        
       }

       if (seller.getPickupAddress() != null
            && seller.getPickupAddress().getAddress() != null
            && seller.getPickupAddress().getCity() != null
            && seller.getPickupAddress().getState() != null
            && seller.getPickupAddress().getMobile() != null) {
            
                existingSeller.setPickupAddress(seller.getPickupAddress());
                existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
                existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
                existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
                existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            }
    return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        
        Seller seller = this.getSellerById(id);
        sellerRepository.delete(seller);

    }

    @Override
    public Seller verifyEmail(String email, String password) throws Exception {
        
        Seller seller = this.getSellerByEmail(email);
        seller.setEmailVerified(true);

        return sellerRepository.save(seller);

    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
    
        Seller seller = this.getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
    
}
