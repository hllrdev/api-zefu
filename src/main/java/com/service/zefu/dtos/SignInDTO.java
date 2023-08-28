package com.service.zefu.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignInDTO {
    
    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
