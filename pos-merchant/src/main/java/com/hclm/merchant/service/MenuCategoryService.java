package com.hclm.merchant.service;

import com.hclm.merchant.mapper.MenuCategoryMapper;
import com.hclm.merchant.pojo.request.MenuCategoryRequest;
import com.hclm.merchant.pojo.response.MenuCategoryResponse;
import com.hclm.web.entity.MenuCategory;
import com.hclm.web.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository categoryRepository;

    @Transactional
    public MenuCategoryResponse createCategory(MenuCategoryRequest request) {
        MenuCategory category = MenuCategoryMapper.INSTANCE.toEntity(request);
        category.setCategoryId(UUID.randomUUID().toString());
        categoryRepository.save(category);
        return MenuCategoryMapper.INSTANCE.toResponse(category);
    }

    @Transactional
    public MenuCategoryResponse updateCategory(String categoryId, MenuCategoryRequest request) {
        MenuCategory existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("类目不存在"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setSortOrder(request.getSortOrder());
        categoryRepository.save(existing);
        return MenuCategoryMapper.INSTANCE.toResponse(existing);
    }

    @Transactional
    public void deleteCategory(String categoryId) {
        categoryRepository.softDeleteById(categoryId);
    }

    @Transactional
    public void updateCategoryInfo(String categoryId, String name, String description, Integer sortOrder) {
        categoryRepository.updateCategory(categoryId, name, description, sortOrder);
    }
}
