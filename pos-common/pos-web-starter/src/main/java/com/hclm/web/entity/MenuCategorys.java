package com.hclm.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hclm.web.constant.TableNameConstant;
import lombok.Data;

/**
 * 菜单类别
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Data
@TableName(TableNameConstant.MENU_CATEGORYS)
public class MenuCategorys {
    /**
     * 类别id
     */
    @TableId(type = IdType.AUTO)
    private Long categoryId;
    /**
     * 所属菜单id
     */
    private String menuId;
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 所属门店id
     */
    private String storeId;
    /**
     * 类别名称
     */
    private String categoryName;
}
