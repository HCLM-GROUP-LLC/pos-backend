package com.hclm.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hclm.mybatis.entity.MenuItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuItemMapper extends BaseMapper<MenuItemEntity> {
    /**
     * 按类别id查找
     *
     * @return {@link List }<{@link MenuItemEntity }>
     */
    List<MenuItemEntity> findByCategoryId(@Param("categoryId") Long categoryId);
}
