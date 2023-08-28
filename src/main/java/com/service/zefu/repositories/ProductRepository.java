package com.service.zefu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.zefu.models.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

}
