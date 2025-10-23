package com.hclm.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hclm.web.constant.TableNameConstant;
import lombok.Data;

@Data
@TableName(TableNameConstant.CAT_ITEMS)
public class CatItems {
    /**
     * 中间表 id
     */
    @TableId(type = IdType.AUTO)
    private Long catItemId;
    /**
     * 商家ID
     */
    private String merchantId;
    /**
     * 菜单类别id
     */
    private Long categoryId;
    /**
     * 单品id
     */
    private Long itemId;
}
