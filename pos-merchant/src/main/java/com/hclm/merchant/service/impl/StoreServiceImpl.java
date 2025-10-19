package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.StoreConverter;
import com.hclm.merchant.pojo.request.StoreRequest;
import com.hclm.merchant.pojo.response.StoreResponse;
import com.hclm.merchant.service.StoreService;
import com.hclm.web.entity.Store;
import com.hclm.web.utils.RandomUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl extends ServiceImpl<com.hclm.web.mapper.StoreMapper, Store> implements StoreService {

    @Override
    public StoreResponse createStore(StoreRequest requestDTO) {
        Store store = StoreConverter.INSTANCE.toEntity(requestDTO);
        store.setId(RandomUtil.generateStoreId());
        save(store);
        return StoreConverter.INSTANCE.toResponse(store);
    }

    @Override
    public StoreResponse updateStore(String id, StoreRequest requestDTO) {
        Store store = getOptById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        // 使用 MapStruct 更新实体字段
        Store updated = StoreConverter.INSTANCE.toEntity(requestDTO);
        updated.setId(store.getId()); // 保留原ID
        updated.setCreatedAt(store.getCreatedAt());
        updated.setCreatedBy(store.getCreatedBy());
        updateById(updated);
        return StoreConverter.INSTANCE.toResponse(updated);
    }

    @Override
    public void deleteStore(String id) {
        removeById(id);
    }

    @Override
    public StoreResponse getStoreById(String id) {
        Store store = getOptById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return StoreConverter.INSTANCE.toResponse(store);
    }

    @Override
    public List<StoreResponse> getStoresByMerchantId(String merchantId) {
        List<Store> stores = lambdaQuery()
                .eq(Store::getMerchantId, merchantId)
                .list();
        return StoreConverter.INSTANCE.toResponse(stores);
    }

    public List<StoreResponse> getAllStores() {
        List<Store> stores = list();
        return StoreConverter.INSTANCE.toResponse(stores);
    }

    @Override
    public StoreResponse updateStatus(String id, String status) {
        Store store = getOptById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        store.setStatus(status);
        updateById(store);
        return StoreConverter.INSTANCE.toResponse(store);
    }

    @Override
    public StoreResponse updateBusinessHours(String id, String businessHours) {
        Store store = getOptById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        store.setBusinessHours(businessHours);
        updateById(store);
        return StoreConverter.INSTANCE.toResponse(store);
    }
}
