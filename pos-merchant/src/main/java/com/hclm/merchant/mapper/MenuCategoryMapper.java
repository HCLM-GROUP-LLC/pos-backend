package com.hclm.merchant.mapper;

import com.hclm.web.entity.MenuCategory;
import com.hclm.merchant.pojo.request.MenuCategoryRequest;
import com.hclm.merchant.pojo.response.MenuCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuCategoryMapper {
    MenuCategoryMapper INSTANCE = Mappers.getMapper(MenuCategoryMapper.class);

    MenuCategory toEntity(MenuCategoryRequest request);

    MenuCategoryResponse toResponse(MenuCategory entity);
}
