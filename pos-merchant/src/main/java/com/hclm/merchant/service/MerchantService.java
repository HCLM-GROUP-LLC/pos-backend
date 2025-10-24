package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.mybatis.entity.MerchantEntity;

public interface MerchantService extends IService<MerchantEntity> {

    AuthResponse login(MerchantLoginRequest request);

    AuthResponse register(MerchantRegisterRequest request);

    MerchantEntity getMerchantInfo();
}
