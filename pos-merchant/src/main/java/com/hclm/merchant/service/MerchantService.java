package com.hclm.merchant.service;

import cn.dev33.satoken.stp.StpUtil;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Merchant;
import com.hclm.web.enums.MerchantStatusEnum;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.repository.MerchantRepository;
import com.hclm.web.utils.PwdUtil;
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
        StpUtil.login(merchant.getId());
        return new AuthResponse(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    public AuthResponse register(MerchantRegisterRequest request) {
        // 1. 获取邮箱
        String email = request.getEmail();
        // 2. 检查邮箱是否已被注册
        boolean isExist = merchantRepository.existsByEmailAndIsDeleted(email, false);
        if(isExist){
            throw new BusinessException(ResponseCode.EMAIL_ALREADY_EXISTS);
        }
        // 3. 创建新商户
        Merchant merchant = new Merchant();
        merchant.setEmail(email);
        merchant.setPasswordHash((request.getPassword()));
        merchant.setStatus(MerchantStatusEnum.ACTIVE.name());
        merchant.setIsDeleted(false);
        merchantRepository.save(merchant);
        // 4. 登录
        StpUtil.login(merchant.getId());
        // 5. 返回token
        return new AuthResponse(StpUtil.getTokenName(), StpUtil.getTokenValue());
     }
}
