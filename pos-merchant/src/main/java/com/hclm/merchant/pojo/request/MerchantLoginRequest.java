package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 商户登录请求
 *
 * @author hanhua
 * @since 2025/10/06
 */
@Data
public class MerchantLoginRequest {
    /**
     * 邮箱
     */
    //@Email(message = "邮箱格式不正确") 这里的验证方式改为这个是否更好
    @NotEmpty
    @Schema(description = "邮箱")
    private String email;
    /**
     * 密码
     */
    @NotEmpty
    @Schema(description = "密码")
    private String password;
}
