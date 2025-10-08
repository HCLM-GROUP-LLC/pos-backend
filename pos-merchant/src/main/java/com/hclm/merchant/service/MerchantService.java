package com.hclm.merchant.service;

import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.satoken.SaTokenUtil;
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

        if (!MerchantStatusEnum.ENABLED.getCode().equals(merchant.getStatus())) {
            throw new BusinessException(ResponseCode.MERCHANT_DISABLED);
        }
        SaTokenUtil.login(merchant.getId());
        return new AuthResponse(SaTokenUtil.getTokenName(), SaTokenUtil.getTokenValue());
    }
}
