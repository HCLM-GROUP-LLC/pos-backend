package com.example.pos_backend.repository;

import com.example.pos_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByStoreIdAndIsDeletedFalse(UUID storeId);
    List<Product> findByStoreIdAndCategoryIdAndIsDeletedFalse(UUID storeId, UUID categoryId);
}
