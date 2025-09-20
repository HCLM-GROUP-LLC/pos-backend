package com.example.pos_backend.repository;

import com.example.pos_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByStoreIdAndIsDeletedFalse(UUID storeId);
}
