package com.example.pos_backend.controller;

import com.example.pos_backend.common.ApiResponse;
import com.example.pos_backend.dto.MenuItemRequestDTO;
import com.example.pos_backend.dto.MenuItemResponseDTO;
import com.example.pos_backend.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService service;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> create(@Valid @RequestBody MenuItemRequestDTO dto) {
        MenuItemResponseDTO result = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result, "Menu item created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> findAll() {
        List<MenuItemResponseDTO> result = service.findAll();
        return ResponseEntity.ok(ApiResponse.success(result, "Menu items retrieved successfully"));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> findByOrgId(@PathVariable UUID orgId) {
        List<MenuItemResponseDTO> result = service.findByOrgId(orgId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu items retrieved successfully"));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> findByStoreId(@PathVariable UUID storeId) {
        List<MenuItemResponseDTO> result = service.findByStoreId(storeId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu items retrieved successfully"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> findByCategoryId(@PathVariable UUID categoryId) {
        List<MenuItemResponseDTO> result = service.findByCategoryId(categoryId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu items retrieved successfully"));
    }

    @GetMapping("/org/{orgId}/store/{storeId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> findByOrgIdAndStoreId(
            @PathVariable UUID orgId, @PathVariable UUID storeId) {
        List<MenuItemResponseDTO> result = service.findByOrgIdAndStoreId(orgId, storeId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu items retrieved successfully"));
    }

    @GetMapping("/org/{orgId}/store/{storeId}/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDTO>>> findByOrgIdAndStoreIdAndCategoryId(
            @PathVariable UUID orgId, @PathVariable UUID storeId, @PathVariable UUID categoryId) {
        List<MenuItemResponseDTO> result = service.findByOrgIdAndStoreIdAndCategoryId(orgId, storeId, categoryId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu items retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> findById(@PathVariable UUID id) {
        MenuItemResponseDTO result = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu item retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponseDTO>> update(
            @PathVariable UUID id, @Valid @RequestBody MenuItemRequestDTO dto) {
        MenuItemResponseDTO result = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu item updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Menu item deleted successfully"));
    }
}
