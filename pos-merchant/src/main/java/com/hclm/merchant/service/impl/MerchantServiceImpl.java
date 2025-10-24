package com.hclm.merchant.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.merchant.service.MerchantService;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.mybatis.entity.MerchantEntity;
import com.hclm.mybatis.enums.MerchantStatusEnum;
import com.hclm.mybatis.mapper.MerchantMapper;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.utils.PwdUtil;
import com.hclm.web.utils.RandomUtil;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, MerchantEntity> implements MerchantService {
    @Override
    public AuthResponse login(MerchantLoginRequest request) {
        MerchantEntity merchantEntity = lambdaQuery()
                .eq(MerchantEntity::getEmail, request.getEmail())
                .oneOpt()
                .orElseThrow(() -> new BusinessException(ResponseCode.EMAIL_PASSWORD_ERROR));

        if (!PwdUtil.matches(request.getPassword(), merchantEntity.getPasswordHash())) {
            throw new BusinessException(ResponseCode.EMAIL_PASSWORD_ERROR);
        }

        if (!MerchantStatusEnum.ACTIVE.equals(merchantEntity.getStatus())) {
            throw new BusinessException(ResponseCode.MERCHANT_DISABLED);
        }
        MerchantLoginUtil.login(merchantEntity.getId());

        return new AuthResponse(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    @Override
    public AuthResponse register(MerchantRegisterRequest request) {
        // 1. 检查邮箱是否已被注册
        String email = request.getEmail();
        boolean isExist = lambdaQuery()
                .eq(MerchantEntity::getEmail, request.getEmail())
                .exists();
        if (isExist) {
            throw new BusinessException(ResponseCode.EMAIL_ALREADY_EXISTS);
        }
        // 2. 创建新商户
        long currentTime = System.currentTimeMillis();
        MerchantEntity merchantEntity = MerchantEntity.builder()
                .id(RandomUtil.generateMerchantId())
                .email(email)
                .phoneNumber(request.getPhoneNumber())
                .passwordHash((request.getPassword()))
                .name(request.getName())
                .businessName(request.getBusinessName())
                .businessAddress(request.getBusinessAddress())
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .status(MerchantStatusEnum.ACTIVE)
                .isDeleted(false)
                .build();
        save(merchantEntity);
        // 3. 登录
        MerchantLoginUtil.login(merchantEntity.getId());
        // 4. 返回token
        return new AuthResponse(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    public MerchantEntity getMerchantInfo() {
        String merchantId = MerchantLoginUtil.getMerchantId();
        return getOptById(merchantId)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
    }
}
