package com.hclm.terminal.mapper;

import com.hclm.web.entity.Store;
import com.hclm.terminal.pojo.request.StoreRequest;
import com.hclm.terminal.pojo.response.StoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreMapper {
    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    Store toEntity(StoreRequest request);

    StoreResponse toResponse(Store store);
}
