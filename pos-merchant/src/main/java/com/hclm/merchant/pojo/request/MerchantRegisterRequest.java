package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 商户注册请求
 *
 * @author qichao
 * @since 2025/10/14
 */
@Data
public class MerchantRegisterRequest {
    @NotEmpty
    @Schema(description = "邮箱")
    private String email;
    
    @NotEmpty
    @Schema(description = "手机号")
    private String phoneNumber;
    
    @NotEmpty
    @Schema(description = "密码")
    private String password;
    
    @NotEmpty
    @Schema(description = "商户名称")
    private String name;
    
    @NotEmpty
    @Schema(description = "企业名称")
    private String businessName;
    
    @NotEmpty
    @Schema(description = "企业地址")
    private String businessAddress;
}
