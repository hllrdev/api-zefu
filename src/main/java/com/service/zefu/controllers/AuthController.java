package com.service.zefu.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.service.zefu.dtos.SignInDTO;
import com.service.zefu.dtos.UserDTO;
import com.service.zefu.dtos.recovery.RecoveryDTO;
import com.service.zefu.dtos.recovery.ResetDTO;
import com.service.zefu.models.UserModel;
import com.service.zefu.services.AuthService;
import com.service.zefu.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private AuthService authService;
    private UserService userService;
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            UserModel userModel = this.authService.signup(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
        } 
        catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage()); 
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup error.");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody @Valid SignInDTO signInDTO) {
        try {
            
            String token = authService.signin(signInDTO.getEmail(), signInDTO.getPassword());
            UserModel user = userService.getByEmail(signInDTO.getEmail()).get();

            Map<String, Object> body = new HashMap<String, Object>();
            body.put("token", token);
            body.put("user", user);

            return ResponseEntity.status(HttpStatus.OK).body(body);
            
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials.");
        } 
        
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sign in error.");
        }
    }

    @PostMapping("/recovery")
    public ResponseEntity<Object> recoveryPassword(@RequestBody @Valid RecoveryDTO recoveryDTO){

        try {
            authService.recoveryPassword(recoveryDTO.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body("Email successfully sent");
        } 
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There was an error sending the email");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid ResetDTO resetDTO){

        try {
            authService.resetPassword(bearerToken, resetDTO.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body("Successfully reset password.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to reset password.");
        }
    }

}
