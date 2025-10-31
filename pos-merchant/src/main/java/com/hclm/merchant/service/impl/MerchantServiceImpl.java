package com.hclm.merchant.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.request.MrcSmsLoginRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.merchant.service.MerchantService;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.mybatis.entity.MerchantEntity;
import com.hclm.mybatis.enums.MerchantStatusEnum;
import com.hclm.mybatis.mapper.MerchantMapper;
import com.hclm.redis.MrcSmsUtil;
import com.hclm.redis.cache.MerchantCodeCache;
import com.hclm.twilio.TwilioUtil;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.utils.MessageUtil;
import com.hclm.web.utils.PwdUtil;
import com.hclm.web.utils.RandomUtil;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, MerchantEntity> implements MerchantService {
    /**
     * 检查实体
     *
     * @param merchantEntity 商户实体
     * @param nullSupplier   空供应商
     * @return {@link MerchantEntity }
     */
    @NonNull
    private MerchantEntity checkEntity(MerchantEntity merchantEntity, Supplier<BusinessException> nullSupplier) {
        if (merchantEntity == null) {
            throw nullSupplier.get();
        }
        if (!MerchantStatusEnum.ACTIVE.equals(merchantEntity.getStatus())) {
            throw new BusinessException(ResponseCode.MERCHANT_DISABLED);
        }
        return merchantEntity;
    }

    private AuthResponse loginByEntity(@NonNull MerchantEntity merchantEntity) {
        MerchantLoginUtil.login(merchantEntity.getId());
        return new AuthResponse(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    @Override
    public AuthResponse login(MerchantLoginRequest request) {
        MerchantEntity merchantEntity = lambdaQuery()
                .eq(MerchantEntity::getEmail, request.getEmail())
                .one();
        // 检查是否存在
        checkEntity(merchantEntity, () -> new BusinessException(ResponseCode.EMAIL_PASSWORD_ERROR));
        if (!PwdUtil.matches(request.getPassword(), merchantEntity.getPasswordHash())) {
            throw new BusinessException(ResponseCode.EMAIL_PASSWORD_ERROR);
        }
        return loginByEntity(merchantEntity);
    }

    @Override
    public AuthResponse login(MrcSmsLoginRequest request) {
        var smsCodeCache = MrcSmsUtil.getSmsCode(request.getSmsCode());
        if (smsCodeCache == null) {
            throw new BusinessException(ResponseCode.SMS_CODE_EXPIRE);
        }
        if (!request.getPhoneNumber().equals(smsCodeCache.getPhoneNumber())) {
            throw new BusinessException(ResponseCode.PHONE_NUMBER_NOT_MATCH);
        }
        MerchantEntity merchantEntity = getById(smsCodeCache.getMerchantId());
        return loginByEntity(
                checkEntity(merchantEntity, () -> new BusinessException(ResponseCode.MERCHANT_NOT_FOUND))
        );
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

    @Override
    public void sendSmsCode(String phoneNumber) {

        if (MrcSmsUtil.existsPhoneNumber(phoneNumber)) {
            throw new BusinessException(ResponseCode.SMS_SEND_TOO_FREQUENT);
        }
        // 根据手机号查询商户
        MerchantEntity merchantEntity = lambdaQuery()
                .eq(MerchantEntity::getPhoneNumber, phoneNumber)
                .one();
        // 检查手机号是否已存在 商户是否激活
        checkEntity(merchantEntity, () -> new BusinessException(ResponseCode.PHONE_NUMBER_NOT_EXIST));
        String code = RandomUtil.numbers(5);
        //记录该手机号最近一次发送时间
        MrcSmsUtil.lastSendTime(phoneNumber);
        //记录验证码对应的手机号和商户ID
        MerchantCodeCache cache = new MerchantCodeCache();
        cache.setPhoneNumber(phoneNumber);
        cache.setMerchantId(merchantEntity.getId());
        MrcSmsUtil.smsCode(code, cache);
        TwilioUtil.sendSms(phoneNumber, MessageUtil.message("sms.code", code));
    }
}
