package com.example.pos_backend.service;

import com.example.pos_backend.dto.InventoryRequestDTO;
import com.example.pos_backend.dto.InventoryResponseDTO;
import com.example.pos_backend.entity.Inventory;
import com.example.pos_backend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryResponseDTO createOrUpdate(InventoryRequestDTO dto) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsDeletedFalse(dto.getProductId())
                .orElse(Inventory.builder().productId(dto.getProductId()).build());

        inventory.setCurrentStock(dto.getCurrentStock());
        inventory.setMinStock(dto.getMinStock());
        inventory.setMaxStock(dto.getMaxStock());
        inventory.setCostPrice(dto.getCostPrice() != null ? BigDecimal.valueOf(dto.getCostPrice()) : null);
        inventory.setCreatedBy(dto.getCreatedBy());
        inventory.setIsDeleted(false);

        return toResponse(inventoryRepository.save(inventory));
    }

    public InventoryResponseDTO getByProductId(UUID productId) {
        Inventory inventory = inventoryRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new RuntimeException("库存不存在"));
        return toResponse(inventory);
    }

    private InventoryResponseDTO toResponse(Inventory inv) {
        return InventoryResponseDTO.builder()
                .inventoryId(inv.getInventoryId())
                .productId(inv.getProductId())
                .currentStock(inv.getCurrentStock())
                .minStock(inv.getMinStock())
                .maxStock(inv.getMaxStock())
                .costPrice(inv.getCostPrice())
                .isDeleted(inv.getIsDeleted())
                .build();
    }
}
