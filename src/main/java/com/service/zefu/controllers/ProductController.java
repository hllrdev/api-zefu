package com.service.zefu.controllers;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.service.zefu.dtos.products.ProductDTO;

import com.service.zefu.services.ProductService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "https://www.zefu.com.br")
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Object> saveProduct(@RequestHeader("Authorization") String bearerToken, @Valid ProductDTO productDTO) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.create(bearerToken, productDTO));
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product save error.");
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable String id, 
    @RequestParam("title") String title,
    @RequestParam("link") String link,
    @RequestParam(value = "photo", required = false) MultipartFile photo) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.productService.update(id, title, link, photo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product update error.");
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable String id){
        try {
            UUID uuid = UUID.fromString(id);
            productService.delete(uuid);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully deleted!");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product delete error.");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllProducts(){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Products get error.");
        }
    }

    @GetMapping("/main")
    public ResponseEntity<Object> getIndexProducts(){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.getProducts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Products index get error.");
        }
    }
}
