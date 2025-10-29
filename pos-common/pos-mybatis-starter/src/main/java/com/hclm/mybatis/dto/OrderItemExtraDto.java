package com.hclm.mybatis.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 额外的收费项
 *
 * @author hanhua
 * @since 2025/10/29
 */
@Data
public class OrderItemExtraDto {
    /**
     * 收费名称
     */
    private String name;
    /**
     * 收费金额
     */
    private BigDecimal price;
}
