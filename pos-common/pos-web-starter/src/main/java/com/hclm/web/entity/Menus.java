package com.hclm.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hclm.web.constant.TableNameConstant;
import lombok.Data;

/**
 * 菜单
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Data
@TableName(TableNameConstant.MENUS)
public class Menus {
    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 所属门店id
     */
    private String storeId;
    /**
     * 菜单名称
     */
    private String menuName;

}
