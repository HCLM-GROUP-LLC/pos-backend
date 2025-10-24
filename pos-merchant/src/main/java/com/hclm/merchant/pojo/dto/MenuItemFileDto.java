package com.hclm.merchant.pojo.dto;

import com.hclm.mybatis.enums.OwnerTypeEnum;
import com.hclm.resource.FileOwner;

public record MenuItemFileDto(Long menuItemId) implements FileOwner {

    @Override
    public OwnerTypeEnum getOwnerType() {
        return OwnerTypeEnum.Menu_Item;
    }

    @Override
    public String getOwnerId() {
        return menuItemId.toString();
    }
}
