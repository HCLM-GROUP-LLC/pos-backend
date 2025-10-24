package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.CatItemsAddRequest;
import com.hclm.mybatis.entity.CatItemEntity;

/**
 * 菜品-分类 关联
 *
 * @author hanhua
 * @since 2025/10/23
 */
public interface MenuCatItemsService extends IService<CatItemEntity> {
    void add(CatItemsAddRequest request);

    void remove(CatItemsAddRequest request);
}
