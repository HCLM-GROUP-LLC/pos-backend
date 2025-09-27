package com.example.pos_backend.repository;

import com.example.pos_backend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByProductIdAndIsDeletedFalse(UUID productId);
}
