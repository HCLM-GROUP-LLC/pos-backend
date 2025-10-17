package com.hclm.merchant.service;

import com.hclm.merchant.mapper.ItemMapper;
import com.hclm.merchant.pojo.request.ItemRequest;
import com.hclm.merchant.pojo.response.ItemResponse;
import com.hclm.web.entity.Item;
import com.hclm.web.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        Item item = ItemMapper.INSTANCE.toEntity(request);
        item.setItemId(UUID.randomUUID().toString());
        itemRepository.save(item);
        return ItemMapper.INSTANCE.toResponse(item);
    }

    @Transactional
    public ItemResponse updateItem(String itemId, ItemRequest request) {
        Item existing = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(BigDecimal.valueOf(request.getPrice()));
        existing.setStatus(request.getStatus());
        existing.setImageUrl(request.getImageUrl());
        itemRepository.save(existing);
        return ItemMapper.INSTANCE.toResponse(existing);
    }

    @Transactional
    public void deleteItem(String itemId) {
        itemRepository.softDeleteById(itemId);
    }

    @Transactional
    public void updateStatus(String itemId, String status) {
        itemRepository.updateStatus(itemId, status);
    }
}
