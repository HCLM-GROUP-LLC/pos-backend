package com.hclm.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.hclm.mybatis.TableNameConstant;
import com.hclm.mybatis.dto.OrderItemExtraDto;
import com.hclm.mybatis.enums.OrderDetailTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详细信息实体
 *
 * @author hanhua
 * @since 2025/10/29
 */
@Data
@TableName(value = TableNameConstant.ORDER_ITEMS, autoResultMap = true)
public class OrderItemEntity {
    /**
     * 订单详细信息ID
     */
    @TableId(type = IdType.AUTO)
    private Long orderDetailId;
    /**
     * 订单id
     *
     * @see OrderEntity#getOrderId() 属于哪个订单
     */
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
     * 关联ID 可以是菜品ID或优惠券ID等，根据detailType确定具体类型
     *
     * @see com.hclm.mybatis.entity.MenuItemEntity#getItemId 菜单项主键
     */
    private Long referenceId;
    /**
     * 小计金额
     */
    private BigDecimal subtotal;
    /**
     * 额外的收费项
     */
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private List<OrderItemExtraDto> extras;
    /**
     * 订单详细信息名称 可以是：菜品名称 、折扣名称、税费名称等，作为冗余字段
     */
    private String detailName;
    /**
     * 订单详细说明 可以是：菜品备注 、折扣说明、税费说明等
     */
    private String detailDesc;
    /**
     * 详细信息类型
     */
    private OrderDetailTypeEnum detailType;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 单价 配合数量使用 明细是菜品的时候有用
     */
    private BigDecimal unitPrice;
}
