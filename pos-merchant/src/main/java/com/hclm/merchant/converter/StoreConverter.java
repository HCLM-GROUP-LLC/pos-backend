package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.request.StoreRequest;
import com.hclm.merchant.pojo.response.StoreResponse;
import com.hclm.web.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StoreConverter {
    StoreConverter INSTANCE = Mappers.getMapper(StoreConverter.class);

    Store toEntity(StoreRequest request);

    StoreResponse toResponse(Store store);

    List<StoreResponse> toResponse(List<Store> stores);
}
