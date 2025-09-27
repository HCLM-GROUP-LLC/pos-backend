package com.example.pos_backend.service;

import com.example.pos_backend.dto.MenuItemRequestDTO;
import com.example.pos_backend.dto.MenuItemResponseDTO;
import com.example.pos_backend.entity.MenuItem;
import com.example.pos_backend.exception.BusinessException;
import com.example.pos_backend.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository repository;

    @Transactional
    public MenuItemResponseDTO create(MenuItemRequestDTO dto) {
        validateMenuItemRequest(dto);
        
        MenuItem item = MenuItem.builder()
                .orgId(dto.getOrgId())
                .storeId(dto.getStoreId())
                .categoryId(dto.getCategoryId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .imageUrl(dto.getImageUrl())
                .sortOrder(dto.getSortOrder())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .isDeleted(false)
                .build();
        return toResponse(repository.save(item));
    }

    public List<MenuItemResponseDTO> findAll() {
        return repository.findAll().stream()
                .filter(item -> !item.getIsDeleted())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponseDTO> findByOrgId(UUID orgId) {
        return repository.findByOrgIdAndNotDeleted(orgId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponseDTO> findByStoreId(UUID storeId) {
        return repository.findByStoreIdAndNotDeleted(storeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponseDTO> findByCategoryId(UUID categoryId) {
        return repository.findByCategoryIdAndNotDeleted(categoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponseDTO> findByOrgIdAndStoreId(UUID orgId, UUID storeId) {
        return repository.findByOrgIdAndStoreIdAndNotDeleted(orgId, storeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponseDTO> findByOrgIdAndStoreIdAndCategoryId(UUID orgId, UUID storeId, UUID categoryId) {
        return repository.findByOrgIdAndStoreIdAndCategoryIdAndNotDeleted(orgId, storeId, categoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MenuItemResponseDTO findById(UUID id) {
        return repository.findById(id)
                .filter(item -> !item.getIsDeleted())
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException("Menu item not found with id: " + id));
    }

    @Transactional
    public MenuItemResponseDTO update(UUID id, MenuItemRequestDTO dto) {
        validateMenuItemRequest(dto);
        
        MenuItem item = repository.findById(id)
                .filter(it -> !it.getIsDeleted())
                .orElseThrow(() -> new BusinessException("Menu item not found with id: " + id));
        
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "USD");
        item.setImageUrl(dto.getImageUrl());
        item.setSortOrder(dto.getSortOrder());
        item.setUpdatedBy(dto.getUpdatedBy());
        
        return toResponse(repository.save(item));
    }

    @Transactional
    public void delete(UUID id) {
        MenuItem item = repository.findById(id)
                .filter(it -> !it.getIsDeleted())
                .orElseThrow(() -> new BusinessException("Menu item not found with id: " + id));
        
        item.setIsDeleted(true);
        repository.save(item);
    }

    private void validateMenuItemRequest(MenuItemRequestDTO dto) {
        if (dto.getOrgId() == null) {
            throw new BusinessException("Organization ID is required");
        }
        if (dto.getStoreId() == null) {
            throw new BusinessException("Store ID is required");
        }
        if (dto.getCategoryId() == null) {
            throw new BusinessException("Category ID is required");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new BusinessException("Item name is required");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Valid price is required");
        }
    }

    private MenuItemResponseDTO toResponse(MenuItem item) {
        MenuItemResponseDTO dto = new MenuItemResponseDTO();
        dto.setId(item.getId());
        dto.setOrgId(item.getOrgId());
        dto.setStoreId(item.getStoreId());
        dto.setCategoryId(item.getCategoryId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setCurrency(item.getCurrency());
        dto.setImageUrl(item.getImageUrl());
        dto.setSortOrder(item.getSortOrder());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        dto.setIsDeleted(item.getIsDeleted());
        return dto;
    }
}