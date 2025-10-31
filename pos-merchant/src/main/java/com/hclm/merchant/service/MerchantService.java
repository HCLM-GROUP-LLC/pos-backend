package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.request.MrcSmsLoginRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.mybatis.entity.MerchantEntity;

public interface MerchantService extends IService<MerchantEntity> {

    /**
     * 登录
     *
     * @param request 请求
     * @return {@link AuthResponse }
     */
    AuthResponse login(MerchantLoginRequest request);

    /**
     * 短信验证登录
     *
     * @param request 请求
     * @return {@link AuthResponse }
     */
    AuthResponse login(MrcSmsLoginRequest request);

    /**
     * 注册
     *
     * @param request 请求
     * @return {@link AuthResponse }
     */
    AuthResponse register(MerchantRegisterRequest request);

    /**
     * 获取商家信息
     *
     * @return {@link MerchantEntity }
     */
    MerchantEntity getMerchantInfo();

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 电话号码
     */
    void sendSmsCode(String phoneNumber);
}
