package com.hclm.terminal.pojo.request;

import com.hclm.mybatis.dto.OrderItemExtraDto;
import com.hclm.mybatis.enums.OrderDetailTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemRequest {
    /**
     * 关联ID 可以是菜品ID或优惠券ID等，根据detailType确定具体类型
     *
     * @see com.hclm.mybatis.entity.MenuItemEntity#getItemId 菜单项主键
     */
    @NotNull
    @Schema(description = "关联ID 可以是菜品ID或优惠券ID等，根据detailType确定具体类型")
    private Long referenceId;
    /**
     * 小计金额
     */
    @NotNull
    @Schema(description = "小计金额")
    private BigDecimal subtotal;
    /**
     * 额外的收费项
     */
    @Schema(description = "额外的收费项")
    private List<OrderItemExtraDto> extras;
    /**
     * 订单详细信息名称 可以是：菜品名称 、折扣名称、税费名称等，作为冗余字段
     */
    @Schema(description = "订单详细信息名称 可以是：菜品名称 、折扣名称、税费名称等，作为冗余字段")
    private String detailName;
    /**
     * 订单详细说明 可以是：菜品备注 、折扣说明、税费说明等
     */
    @Schema(description = "订单详细说明 可以是：菜品备注 、折扣说明、税费说明等")
    private String detailDesc;
    /**
     * 详细信息类型
     */
    @NotNull
    private OrderDetailTypeEnum detailType;
    /**
     * 数量
     */
    @NotNull
    private BigDecimal quantity;
    /**
     * 单价 配合数量使用 明细是菜品的时候有用
     */
    @NotNull
    private BigDecimal unitPrice;
}
