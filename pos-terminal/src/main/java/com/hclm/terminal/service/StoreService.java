package com.hclm.terminal.service;

import com.hclm.web.entity.Store;
import com.hclm.terminal.mapper.StoreMapper;
import com.hclm.terminal.pojo.request.StoreRequest;
import com.hclm.terminal.pojo.response.StoreResponse;
import com.hclm.web.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Transactional
    public StoreResponse createStore(StoreRequest requestDTO) {
        Store store = storeMapper.toEntity(requestDTO);
        store.setId(UUID.randomUUID().toString());
        storeRepository.save(store);
        return storeMapper.toResponse(store);
    }

    @Transactional
    public StoreResponse updateStore(String id, StoreRequest requestDTO) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        // 使用 MapStruct 更新实体字段
        Store updated = storeMapper.toEntity(requestDTO);
        updated.setId(store.getId()); // 保留原ID
        updated.setCreatedAt(store.getCreatedAt());
        updated.setCreatedBy(store.getCreatedBy());
        storeRepository.save(updated);
        return storeMapper.toResponse(updated);
    }

    @Transactional
    public void deleteStore(String id) {
        storeRepository.softDeleteById(id);
    }

    public StoreResponse getStoreById(String id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return storeMapper.toResponse(store);
    }

    public List<StoreResponse> getStoresByMerchantId(String merchantId) {
        List<Store> stores = storeRepository.findByMerchantId(merchantId);
        return stores.stream().map(storeMapper::toResponse).collect(Collectors.toList());
    }

    public List<StoreResponse> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(storeMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public StoreResponse updateStatus(String id, String status) {
        storeRepository.updateStatus(id, status);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return storeMapper.toResponse(store);
    }

    @Transactional
    public StoreResponse updateBusinessHours(String id, String businessHours) {
        storeRepository.updateBusinessHours(id, businessHours);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return storeMapper.toResponse(store);
    }
}
