package com.hclm.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hclm.web.entity.MenuItems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuItemsMapper extends BaseMapper<MenuItems> {
    /**
     * 按类别id查找
     *
     * @return {@link List }<{@link MenuItems }>
     */
    List<MenuItems> findByCategoryId(@Param("categoryId") Long categoryId);
}
