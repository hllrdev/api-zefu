package com.service.zefu.dtos.recovery;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecoveryDTO {
    
    @NotBlank
    private String email;

}
