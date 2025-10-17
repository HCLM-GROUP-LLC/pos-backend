package com.hclm.merchant.mapper;

import com.hclm.web.entity.Store;
import com.hclm.merchant.pojo.request.StoreRequest;
import com.hclm.merchant.pojo.response.StoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    Store toEntity(StoreRequest request);

    StoreResponse toResponse(Store store);
}
