package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.web.entity.Merchant;

public interface MerchantService extends IService<Merchant> {

    AuthResponse login(MerchantLoginRequest request);

    AuthResponse register(MerchantRegisterRequest request);

    Merchant getMerchantInfo();
}
