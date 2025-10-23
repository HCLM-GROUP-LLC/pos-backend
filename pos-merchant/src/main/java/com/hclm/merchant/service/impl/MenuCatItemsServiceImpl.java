package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.pojo.request.CatItemsAddRequest;
import com.hclm.merchant.service.MenuCatItemsService;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.CatItems;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.mapper.CatItemsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuCatItemsServiceImpl extends ServiceImpl<CatItemsMapper, CatItems> implements MenuCatItemsService {
    @Transactional
    @Override
    public void add(CatItemsAddRequest request) {
        if (getBaseMapper().coutByCatId(request.getCategoryId(), request.getItemIds()) > 0) {
            throw new BusinessException(ResponseCode.CAT_ITEMS_REPEAT);
        }
        List<CatItems> catItemsList = new ArrayList<>(request.getItemIds().size());
        for (Long itemId : request.getItemIds()) {
            CatItems catItems = new CatItems();
            catItems.setCategoryId(request.getCategoryId());
            catItems.setItemId(itemId);
            catItemsList.add(catItems);
        }
        saveBatch(catItemsList);
    }

    @Override
    public void remove(CatItemsAddRequest request) {
        getBaseMapper().removeByCatId(request.getCategoryId(), request.getItemIds());
    }
}
