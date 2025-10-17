package com.hclm.merchant.mapper;

import com.hclm.web.entity.Menu;
import com.hclm.merchant.pojo.request.MenuRequest;
import com.hclm.merchant.pojo.response.MenuResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    Menu toEntity(MenuRequest request);

    MenuResponse toResponse(Menu entity);
}

