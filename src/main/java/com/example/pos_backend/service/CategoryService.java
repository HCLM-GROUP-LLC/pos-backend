package com.example.pos_backend.service;

import com.example.pos_backend.dto.CategoryRequestDTO;
import com.example.pos_backend.dto.CategoryResponseDTO;
import com.example.pos_backend.entity.Category;
import com.example.pos_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category category = Category.builder()
                .storeId(dto.getStoreId())
                .categoryName(dto.getCategoryName())
                .description(dto.getDescription())
                .displayOrder(dto.getDisplayOrder())
                .isActive(dto.getIsActive())
                .createdBy(dto.getCreatedBy())
                .isDeleted(false)
                .build();
        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponseDTO getById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        return toResponse(category);
    }

    public List<CategoryResponseDTO> list(UUID storeId) {
        return categoryRepository.findByStoreIdAndIsDeletedFalse(storeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CategoryResponseDTO update(UUID id, CategoryRequestDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        category.setDisplayOrder(dto.getDisplayOrder());
        category.setIsActive(dto.getIsActive());
        
        return toResponse(categoryRepository.save(category));
    }

    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    private CategoryResponseDTO toResponse(Category c) {
        return CategoryResponseDTO.builder()
                .categoryId(c.getCategoryId())
                .storeId(c.getStoreId())
                .categoryName(c.getCategoryName())
                .description(c.getDescription())
                .displayOrder(c.getDisplayOrder())
                .isActive(c.getIsActive())
                .isDeleted(c.getIsDeleted())
                .build();
    }
}
