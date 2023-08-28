package com.service.zefu.dtos.products;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductDTO {
    
    @NotBlank
    private String title;
    @NotBlank
    private String link;
    @NotNull
    private MultipartFile photo;


}
