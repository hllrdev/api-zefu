package com.service.zefu.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.service.zefu.models.UserModel;
import com.service.zefu.repositories.UserRepository;
import com.service.zefu.security.jwt.ProviderJwt;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final ProviderJwt providerJwt;

    public UserService(UserRepository userRepository, ProviderJwt providerJwt) {
        this.userRepository = userRepository;
        this.providerJwt = providerJwt;
    }
    
    @Transactional
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    public Optional<UserModel> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserModel getByToken(String bearerToken){
        String token = bearerToken.split(" ")[1];
        String email = providerJwt.getEmailFromToken(token);
        UserModel userModel = getByEmail(email).get();
        return userModel;
    }

}
