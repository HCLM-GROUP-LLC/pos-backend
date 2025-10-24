package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.StoreRequest;
import com.hclm.merchant.pojo.response.StoreResponse;
import com.hclm.mybatis.entity.StoreEntity;

import java.util.List;


/**
 * 存储服务
 *
 * @author hanhua
 * @since 2025/10/19
 */
public interface StoreService extends IService<StoreEntity> {

    StoreResponse createStore(StoreRequest requestDTO);

    StoreResponse updateStore(String id, StoreRequest requestDTO);


    void deleteStore(String id);

    StoreResponse getStoreById(String id);

    List<StoreResponse> getStoresByMerchantId(String merchantId);

    List<StoreResponse> getAllStores();


    StoreResponse updateStatus(String id, String status);


    StoreResponse updateBusinessHours(String id, String businessHours);
}
