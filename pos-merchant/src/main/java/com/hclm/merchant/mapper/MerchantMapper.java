package com.hclm.merchant.mapper;

import com.hclm.web.entity.Merchant;
import com.hclm.merchant.pojo.response.MerchantInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MerchantMapper {
    MerchantMapper INSTANCE = Mappers.getMapper(MerchantMapper.class);

    MerchantInfoResponse toInfoResponse(Merchant merchant);
}