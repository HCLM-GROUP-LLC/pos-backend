package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 商户短信登录请求
 *
 * @author hanhua
 * @since 2025/10/31
 */
@Data
public class MrcSmsLoginRequest {
    /**
     * 短信验证码
     */
    @Schema(description = "短信验证码")
    @NotEmpty
    private String smsCode;
    /**
     * 电话号码
     */
    @Schema(description = "电话号码")
    @NotEmpty
    private String phoneNumber;
}
