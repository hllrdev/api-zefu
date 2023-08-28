package com.service.zefu.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.service.zefu.dtos.UserDTO;
import com.service.zefu.email.EmailDetails;
import com.service.zefu.email.EmailServiceImpl;
import com.service.zefu.enums.EnumProvider;
import com.service.zefu.enums.EnumRole;
import com.service.zefu.models.UserModel;
import com.service.zefu.security.jwt.ProviderJwt;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleService roleService;
    private UserService userService;

    private AuthenticationManager authenticationManager;
    private ProviderJwt providerJwt;

    private EmailServiceImpl emailService;

    public AuthService(BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, UserService userService,
        AuthenticationManager authenticationManager, ProviderJwt providerJwt, EmailServiceImpl emailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.userService = userService;

        this.authenticationManager = authenticationManager;
        this.providerJwt = providerJwt;
        this.emailService = emailService;
    }

    @Transactional
    public UserModel signup(UserDTO userDTO) {
        Optional<UserModel> optionalUserModel = userService.getByEmail(userDTO.getEmail());
        if(optionalUserModel.isPresent()){

            if(optionalUserModel.get().getProvider() == EnumProvider.GOOGLE)
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered with Google.");

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists.");
        }
        else{
            UserModel userModel = new UserModel();
            BeanUtils.copyProperties(userDTO, userModel);

            String passwordEncoded = bCryptPasswordEncoder.encode(userModel.getPassword());
            userModel.setPassword(passwordEncoded);

            userModel.setProvider(EnumProvider.LOCAL);
            userModel.setRoles(List.of(roleService.getRole(EnumRole.USER)));
            
            Date now = new Date();
            userModel.setCreatedAt(now);
            userModel.setUpdatedAt(now);

            return userService.save(userModel);
        }
    }

    public String signin(String email, String password) {
        
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = providerJwt.generateToken(authentication);
        return token;
    }

    public void recoveryPassword(String email){

        Optional<UserModel> userModel = userService.getByEmail(email);
        if(userModel.isPresent()){
            String token = providerJwt.generateTokenForResetPassword(email);

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(email);
            emailDetails.setSubject("Recuperação de senha - ZEFU");
            emailDetails.setBody("Clique no link abaixo para criar uma nova senha.\n\n"
            + "https://www.zefu.com.br" + "/auth/reset?token="+ token);
            emailService.sendSimpleMail(emailDetails);

        }
        else
            throw new RuntimeException("User not found");
    }

    public void resetPassword(String bearerToken, String password){
        String token = bearerToken.split(" ")[1];
        String email = providerJwt.getEmailFromToken(token);
        UserModel userModel = userService.getByEmail(email).get();
        
        String passwordEncoded = bCryptPasswordEncoder.encode(password);
        userModel.setPassword(passwordEncoded);

        Date now = new Date();
        userModel.setUpdatedAt(now);
        userService.save(userModel);
    }
}
