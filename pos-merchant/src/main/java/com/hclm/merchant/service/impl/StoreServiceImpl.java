package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.StoreConverter;
import com.hclm.merchant.pojo.request.StoreRequest;
import com.hclm.merchant.pojo.response.StoreResponse;
import com.hclm.merchant.service.StoreService;
import com.hclm.mybatis.entity.StoreEntity;
import com.hclm.mybatis.mapper.StoreMapper;
import com.hclm.web.utils.RandomUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, StoreEntity> implements StoreService {

    @Override
    public StoreResponse createStore(StoreRequest requestDTO) {
        StoreEntity storeEntity = StoreConverter.INSTANCE.toEntity(requestDTO);
        storeEntity.setId(RandomUtil.generateStoreId());
        save(storeEntity);
        return StoreConverter.INSTANCE.toResponse(storeEntity);
    }

    @Override
    public StoreResponse updateStore(String id, StoreRequest requestDTO) {
        StoreEntity storeEntity = getOptById(id)
                .orElseThrow(() -> new RuntimeException("StoreEntity not found"));
        // 使用 MapStruct 更新实体字段
        StoreEntity updated = StoreConverter.INSTANCE.toEntity(requestDTO);
        updated.setId(storeEntity.getId()); // 保留原ID
        updated.setCreatedAt(storeEntity.getCreatedAt());
        updated.setCreatedBy(storeEntity.getCreatedBy());
        updateById(updated);
        return StoreConverter.INSTANCE.toResponse(updated);
    }

    @Override
    public void deleteStore(String id) {
        removeById(id);
    }

    @Override
    public StoreResponse getStoreById(String id) {
        StoreEntity storeEntity = getOptById(id)
                .orElseThrow(() -> new RuntimeException("StoreEntity not found"));
        return StoreConverter.INSTANCE.toResponse(storeEntity);
    }

    @Override
    public List<StoreResponse> getStoresByMerchantId(String merchantId) {
        List<StoreEntity> storeEntities = lambdaQuery()
                .eq(StoreEntity::getMerchantId, merchantId)
                .list();
        return StoreConverter.INSTANCE.toResponse(storeEntities);
    }

    public List<StoreResponse> getAllStores() {
        List<StoreEntity> storeEntities = list();
        return StoreConverter.INSTANCE.toResponse(storeEntities);
    }

    @Override
    public StoreResponse updateStatus(String id, String status) {
        StoreEntity storeEntity = getOptById(id)
                .orElseThrow(() -> new RuntimeException("StoreEntity not found"));
        storeEntity.setStatus(status);
        updateById(storeEntity);
        return StoreConverter.INSTANCE.toResponse(storeEntity);
    }

    @Override
    public StoreResponse updateBusinessHours(String id, String businessHours) {
        StoreEntity storeEntity = getOptById(id)
                .orElseThrow(() -> new RuntimeException("StoreEntity not found"));
        storeEntity.setBusinessHours(businessHours);
        updateById(storeEntity);
        return StoreConverter.INSTANCE.toResponse(storeEntity);
    }
}
