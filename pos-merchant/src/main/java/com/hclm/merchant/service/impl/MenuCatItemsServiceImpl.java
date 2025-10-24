package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.pojo.request.CatItemsAddRequest;
import com.hclm.merchant.service.MenuCatItemsService;
import com.hclm.mybatis.entity.CatItemEntity;
import com.hclm.mybatis.mapper.CatItemMapper;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ResponseCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuCatItemsServiceImpl extends ServiceImpl<CatItemMapper, CatItemEntity> implements MenuCatItemsService {
    @Transactional
    @Override
    public void add(CatItemsAddRequest request) {
        if (getBaseMapper().coutByCatId(request.getCategoryId(), request.getItemIds()) > 0) {
            throw new BusinessException(ResponseCode.CAT_ITEMS_REPEAT);
        }
        List<CatItemEntity> catItemEntityList = new ArrayList<>(request.getItemIds().size());
        for (Long itemId : request.getItemIds()) {
            CatItemEntity catItemEntity = new CatItemEntity();
            catItemEntity.setCategoryId(request.getCategoryId());
            catItemEntity.setItemId(itemId);
            catItemEntityList.add(catItemEntity);
        }
        saveBatch(catItemEntityList);
    }

    @Override
    public void remove(CatItemsAddRequest request) {
        getBaseMapper().removeByCatId(request.getCategoryId(), request.getItemIds());
    }
}
