package com.example.pos_backend.controller;

import com.example.pos_backend.common.ApiResponse;
import com.example.pos_backend.dto.MenuCategoryRequestDTO;
import com.example.pos_backend.dto.MenuCategoryResponseDTO;
import com.example.pos_backend.service.MenuCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-categories")
@RequiredArgsConstructor
public class MenuCategoryController {

    private final MenuCategoryService service;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuCategoryResponseDTO>> create(@Valid @RequestBody MenuCategoryRequestDTO dto) {
        MenuCategoryResponseDTO result = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result, "Menu category created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuCategoryResponseDTO>>> findAll() {
        List<MenuCategoryResponseDTO> result = service.findAll();
        return ResponseEntity.ok(ApiResponse.success(result, "Menu categories retrieved successfully"));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<ApiResponse<List<MenuCategoryResponseDTO>>> findByOrgId(@PathVariable UUID orgId) {
        List<MenuCategoryResponseDTO> result = service.findByOrgId(orgId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu categories retrieved successfully"));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<MenuCategoryResponseDTO>>> findByStoreId(@PathVariable UUID storeId) {
        List<MenuCategoryResponseDTO> result = service.findByStoreId(storeId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu categories retrieved successfully"));
    }

    @GetMapping("/org/{orgId}/store/{storeId}")
    public ResponseEntity<ApiResponse<List<MenuCategoryResponseDTO>>> findByOrgIdAndStoreId(
            @PathVariable UUID orgId, @PathVariable UUID storeId) {
        List<MenuCategoryResponseDTO> result = service.findByOrgIdAndStoreId(orgId, storeId);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu categories retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuCategoryResponseDTO>> findById(@PathVariable UUID id) {
        MenuCategoryResponseDTO result = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu category retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuCategoryResponseDTO>> update(
            @PathVariable UUID id, @Valid @RequestBody MenuCategoryRequestDTO dto) {
        MenuCategoryResponseDTO result = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(result, "Menu category updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Menu category deleted successfully"));
    }
}
