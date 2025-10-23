package com.hclm.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hclm.web.entity.CatItems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface CatItemsMapper extends BaseMapper<CatItems> {
    /**
     * 按分类ID批量删除
     *
     * @param categoryId 类别id
     * @param itemIds    项目id
     * @return int
     */
    int removeByCatId(@Param("categoryId") Long categoryId, @Nullable @Param("itemIds") List<Long> itemIds);

    long coutByCatId(@Param("categoryId") Long categoryId, @NonNull @Param("itemIds") List<Long> itemIds);
}
