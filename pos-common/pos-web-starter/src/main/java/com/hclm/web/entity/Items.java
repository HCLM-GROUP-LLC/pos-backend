package com.hclm.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.hclm.web.constant.TableNameConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜品 菜单项目
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Data
@TableName(TableNameConstant.MENU_ITEMS)
public class Items {
    /**
     * 单品id
     */
    @TableId(type = IdType.AUTO)
    private Long itemId;
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 所属门店id
     */
    private String storeId;
    /**
     * 单品名称
     */
    private String itemName;
    /**
     * 菜品简介
     */
    private String itemDescription;
    /**
     * 单价
     */
    private BigDecimal itemPrice;
    /**
     * 注释标记
     */
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private List<String> noteTags;
    //TODO 还缺一个 打印机端口
}
