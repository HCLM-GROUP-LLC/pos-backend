package com.hclm.merchant.service;

import com.hclm.merchant.mapper.MenuCategoryItemMapper;
import com.hclm.merchant.pojo.request.MenuCategoryItemRequest;
import com.hclm.merchant.pojo.response.MenuCategoryItemResponse;
import com.hclm.web.entity.MenuCategoryItem;
import com.hclm.web.repository.MenuCategoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuCategoryItemService {

    private final MenuCategoryItemRepository mciRepository;


    @Transactional
    public MenuCategoryItemResponse bindItemToCategory(MenuCategoryItemRequest request) {
        MenuCategoryItem entity = MenuCategoryItemMapper.INSTANCE.toEntity(request);
        entity.setId(UUID.randomUUID().toString());
        mciRepository.save(entity);
        return MenuCategoryItemMapper.INSTANCE.toResponse(entity);
    }

    @Transactional
    public void deleteBinding(String id) {
        mciRepository.softDeleteById(id);
    }
}
