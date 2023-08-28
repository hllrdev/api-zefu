package com.service.zefu.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.zefu.dtos.products.ProductDTO;
import com.service.zefu.models.ProductModel;
import com.service.zefu.models.UserModel;
import com.service.zefu.repositories.ProductRepository;
import com.service.zefu.security.jwt.ProviderJwt;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    
    private ProductRepository productRepository;
    private UserService userService;
    private ProviderJwt providerJwt;

    private final ResourceLoader resourceLoader;

    public ProductService(ProductRepository productRepository, UserService userService, ProviderJwt providerJwt, ResourceLoader resourceLoader){
        this.productRepository = productRepository;
        this.userService = userService;
        this.providerJwt = providerJwt;
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public ProductModel create(String bearerToken, ProductDTO productDTO) throws IOException{
        String token = bearerToken.split(" ")[1];
        String email = providerJwt.getEmailFromToken(token);
        UserModel userModel = userService.getByEmail(email).get();

        Date now = new Date();

        MultipartFile photo = productDTO.getPhoto();
        String photoName = photo.getOriginalFilename();
        String[] splitFilename = photoName.split("\\.");
        String format = splitFilename[splitFilename.length - 1];
        String pathFile = userModel.getId() + Long.toString(now.getTime()) + "." + format;

        byte[] bytes = photo.getBytes();
        String staticPath = resourceLoader.getResource("classpath:static").getFile().getAbsolutePath();
        Path path = Paths.get(staticPath + "/images/" + pathFile);
        Files.write(path, bytes);

        ProductModel productModel = new ProductModel();
        productModel.setTitle(productDTO.getTitle());
        productModel.setLink(productDTO.getLink());
        productModel.setPhoto("/images/" + pathFile);
        productModel.setUserModel(userModel);
        productModel.setCreatedAt(now);
        productModel.setUpdatedAt(now);

        return productRepository.save(productModel);
    }

    @Transactional
    public ProductModel update(String id, String title, String link, MultipartFile photo) throws IOException{

        ProductModel productModel = productRepository.findById(UUID.fromString(id)).get();

        if(photo != null){
            byte[] bytes = photo.getBytes();
            String staticPath = resourceLoader.getResource("classpath:static").getFile().getAbsolutePath();
            Path path = Paths.get(staticPath + productModel.getPhoto());
            Files.write(path, bytes);
        }

        productModel.setTitle(title);
        productModel.setLink(link);
        
        Date now = new Date();
        productModel.setUpdatedAt(now);

        return productRepository.save(productModel);
    }

    @Transactional
    public void delete(UUID id) throws Exception{

        Optional<ProductModel> optionalProductModel = productRepository.findById(id);
        if(!optionalProductModel.isPresent())
            throw new RuntimeException("Product not found");

        productRepository.deleteById(id);
        
    }

    public List<ProductModel> getAll(){
        return productRepository.findAll();
    }
    
    public List<ProductModel> getProducts(){
        List<ProductModel> allProducts = getAll();
        return allProducts.subList(0, 3);
    }

    @Transactional
    public ProductModel save(ProductModel productModel){
        return productRepository.save(productModel);
    }
}
