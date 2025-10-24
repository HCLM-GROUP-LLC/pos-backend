package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.request.StoreRequest;
import com.hclm.merchant.pojo.response.StoreResponse;
import com.hclm.mybatis.entity.StoreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StoreConverter {
    StoreConverter INSTANCE = Mappers.getMapper(StoreConverter.class);

    StoreEntity toEntity(StoreRequest request);

    StoreResponse toResponse(StoreEntity storeEntity);

    List<StoreResponse> toResponse(List<StoreEntity> storeEntities);
}
