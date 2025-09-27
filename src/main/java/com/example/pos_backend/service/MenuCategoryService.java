package com.example.pos_backend.service;

import com.example.pos_backend.dto.MenuCategoryRequestDTO;
import com.example.pos_backend.dto.MenuCategoryResponseDTO;
import com.example.pos_backend.entity.MenuCategory;
import com.example.pos_backend.exception.BusinessException;
import com.example.pos_backend.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository repository;

    @Transactional
    public MenuCategoryResponseDTO create(MenuCategoryRequestDTO dto) {
        validateMenuCategoryRequest(dto);
        
        MenuCategory category = MenuCategory.builder()
                .orgId(dto.getOrgId())
                .storeId(dto.getStoreId())
                .name(dto.getName())
                .sortOrder(dto.getSortOrder())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .isDeleted(false)
                .build();
        return toResponse(repository.save(category));
    }

    public List<MenuCategoryResponseDTO> findAll() {
        return repository.findAll().stream()
                .filter(category -> !category.getIsDeleted())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuCategoryResponseDTO> findByOrgId(UUID orgId) {
        return repository.findByOrgIdAndNotDeleted(orgId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuCategoryResponseDTO> findByStoreId(UUID storeId) {
        return repository.findByStoreIdAndNotDeleted(storeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuCategoryResponseDTO> findByOrgIdAndStoreId(UUID orgId, UUID storeId) {
        return repository.findByOrgIdAndStoreIdAndNotDeleted(orgId, storeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MenuCategoryResponseDTO findById(UUID id) {
        return repository.findById(id)
                .filter(category -> !category.getIsDeleted())
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException("Menu category not found with id: " + id));
    }

    @Transactional
    public MenuCategoryResponseDTO update(UUID id, MenuCategoryRequestDTO dto) {
        validateMenuCategoryRequest(dto);
        
        MenuCategory category = repository.findById(id)
                .filter(cat -> !cat.getIsDeleted())
                .orElseThrow(() -> new BusinessException("Menu category not found with id: " + id));
        
        category.setName(dto.getName());
        category.setSortOrder(dto.getSortOrder());
        category.setUpdatedBy(dto.getUpdatedBy());
        
        return toResponse(repository.save(category));
    }

    @Transactional
    public void delete(UUID id) {
        MenuCategory category = repository.findById(id)
                .filter(cat -> !cat.getIsDeleted())
                .orElseThrow(() -> new BusinessException("Menu category not found with id: " + id));
        
        category.setIsDeleted(true);
        repository.save(category);
    }

    private void validateMenuCategoryRequest(MenuCategoryRequestDTO dto) {
        if (dto.getOrgId() == null) {
            throw new BusinessException("Organization ID is required");
        }
        if (dto.getStoreId() == null) {
            throw new BusinessException("Store ID is required");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new BusinessException("Category name is required");
        }
    }

    private MenuCategoryResponseDTO toResponse(MenuCategory category) {
        MenuCategoryResponseDTO dto = new MenuCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setOrgId(category.getOrgId());
        dto.setStoreId(category.getStoreId());
        dto.setName(category.getName());
        dto.setSortOrder(category.getSortOrder());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        dto.setIsDeleted(category.getIsDeleted());
        return dto;
    }
}
