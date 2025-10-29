package com.hclm.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hclm.mybatis.TableNameConstant;
import com.hclm.mybatis.enums.OrderStatusEnum;
import com.hclm.mybatis.enums.OrderTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

@TableName(TableNameConstant.ORDERS)
@Data
public class OrderEntity {
    /**
     * 订单id
     */
    @TableId(type = IdType.AUTO)
    private Long orderId;
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 门店ID
     */
    private String storeId;
    /**
     * 餐桌id  可以是null
     *
     * @see TableEntity#getDiningTableId() 属于哪个餐桌ID
     */
    private Long diningTableId;
    /**
     * 订单总价
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;
    /**
     * 订单类型
     */
    private OrderTypeEnum orderType;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
}
