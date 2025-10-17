package com.hclm.merchant.service;

import cn.dev33.satoken.stp.StpUtil;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Merchant;
import com.hclm.web.enums.MerchantStatusEnum;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.repository.MerchantRepository;
import com.hclm.web.utils.PwdUtil;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public AuthResponse login(MerchantLoginRequest request) {
        Merchant merchant = merchantRepository.findByEmailAndIsDeleted(request.getEmail(), false)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMAIL_PASSWORD_ERROR));

        if (!PwdUtil.matches(request.getPassword(), merchant.getPasswordHash())) {
            throw new BusinessException(ResponseCode.EMAIL_PASSWORD_ERROR);
        }

        if (!MerchantStatusEnum.ACTIVE.name().equals(merchant.getStatus())) {
            throw new BusinessException(ResponseCode.MERCHANT_DISABLED);
        }
        MerchantLoginUtil.login(merchant.getId());

        return new AuthResponse(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    public AuthResponse register(MerchantRegisterRequest request) {
        // 1. 检查邮箱是否已被注册
        String email = request.getEmail();
        boolean isExist = merchantRepository.existsByEmailAndIsDeleted(request.getEmail(), false);
        if (isExist) {
            throw new BusinessException(ResponseCode.EMAIL_ALREADY_EXISTS);
        }
        // 2. 创建新商户
        long currentTime = System.currentTimeMillis();
        Merchant merchant = Merchant.builder()
                .id(RandomUtil.generateMerchantId())
                .email(email)
                .phoneNumber(request.getPhoneNumber())
                .passwordHash((request.getPassword()))
                .name(request.getName())
                .businessName(request.getBusinessName())
                .businessAddress(request.getBusinessAddress())
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .status(MerchantStatusEnum.ACTIVE.name())
                .isDeleted(false)
                .build();
        merchantRepository.save(merchant);
        // 3. 登录
        MerchantLoginUtil.login(merchant.getId());
        // 4. 返回token
        return new AuthResponse(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    public Merchant getMerchantInfo() {
        String merchantId = MerchantLoginUtil.getMerchantId();
        return merchantRepository.findById(merchantId)
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
    }
}
