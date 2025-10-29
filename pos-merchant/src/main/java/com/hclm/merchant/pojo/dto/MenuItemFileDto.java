package com.hclm.merchant.pojo.dto;

import com.hclm.mybatis.enums.FileOwnerEnum;
import com.hclm.resource.FileOwner;

public record MenuItemFileDto(Long menuItemId) implements FileOwner {

    @Override
    public FileOwnerEnum getOwnerType() {
        return FileOwnerEnum.Menu_Item;
    }

    @Override
    public String getOwnerId() {
        return menuItemId.toString();
    }
}
