package com.hclm.merchant.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * auth响应
 *
 * @author hanhua
 * @since 2025/10/08
 */
@AllArgsConstructor
@Data
public class AuthResponse {
    /**
     * 令牌名称
     */
    @Schema(description = "令牌名称")
    private String tokenName;
    /**
     * 令牌值
     */
    @Schema(description = "令牌值")
    private String tokenValue;
}
