package com.hclm.merchant.mapper;

import com.hclm.web.entity.Item;
import com.hclm.merchant.pojo.request.ItemRequest;
import com.hclm.merchant.pojo.response.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    Item toEntity(ItemRequest request);

    ItemResponse toResponse(Item entity);
}

