package com.hclm.merchant.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家信息响应
 *
 * @author hanhua
 * @since 2025/10/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInfoResponse {
    
    @Schema(description = "商家ID")
    private String merchantId;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "手机号")
    private String phoneNumber;
    
    @Schema(description = "商户名称")
    private String name;
    
    @Schema(description = "企业名称")
    private String businessName;
    
    @Schema(description = "企业地址")
    private String businessAddress;
    
    @Schema(description = "状态")
    private String status;
    
    @Schema(description = "最后登录时间")
    private Long lastLoginAt;
    
    @Schema(description = "创建时间")
    private Long createdAt;
    
    @Schema(description = "更新时间")
    private Long updatedAt;
}