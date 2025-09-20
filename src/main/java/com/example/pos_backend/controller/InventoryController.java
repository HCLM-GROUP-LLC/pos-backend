package com.example.pos_backend.controller;

import com.example.pos_backend.dto.InventoryRequestDTO;
import com.example.pos_backend.dto.InventoryResponseDTO;
import com.example.pos_backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createOrUpdate(@Valid @RequestBody InventoryRequestDTO dto) {
        return ResponseEntity.ok(inventoryService.createOrUpdate(dto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> getByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryService.getByProductId(productId));
    }
}

