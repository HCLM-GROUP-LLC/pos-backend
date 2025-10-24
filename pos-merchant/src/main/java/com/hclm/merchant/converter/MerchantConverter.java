package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.response.MerchantInfoResponse;
import com.hclm.mybatis.entity.MerchantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MerchantConverter {
    MerchantConverter INSTANCE = Mappers.getMapper(MerchantConverter.class);

    MerchantInfoResponse toInfoResponse(MerchantEntity merchantEntity);
}