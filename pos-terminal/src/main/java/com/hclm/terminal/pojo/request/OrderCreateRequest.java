package com.hclm.terminal.pojo.request;

import com.hclm.mybatis.entity.TableEntity;
import com.hclm.mybatis.enums.OrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateRequest {
    /**
     * 餐桌id  可以是null
     *
     * @see TableEntity#getDiningTableId() 属于哪个餐桌ID
     */
    @NotNull
    @Schema(description = "餐桌id,可以是null")
    private Long diningTableId;
    /**
     * 订单总价
     */
    @NotNull
    @Schema(description = "订单总价")
    private BigDecimal totalAmount;
    /**
     * 订单类型
     */
    @NotNull
    @Schema(description = "订单类型")
    private OrderTypeEnum orderType;
    /**
     * 订单详细信息
     */
    @Valid
    @NotEmpty
    @Schema(description = "订单详细信息")
    private List<OrderItemRequest> orderItems;
}
