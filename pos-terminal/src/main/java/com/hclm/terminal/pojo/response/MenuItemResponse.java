package com.hclm.terminal.pojo.response;

import com.hclm.mybatis.enums.MenuItemTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜单项响应
 *
 * @author hanhua
 * @since 2025/10/23
 */
@Data
public class MenuItemResponse {
    /**
     * 单品id
     */
    @Schema(description = "单品id")
    private Long itemId;
    /**
     * 单品名称
     */
    @Schema(description = "单品名称")
    private String itemName;
    /**
     * 菜品简介
     */
    @Schema(description = "菜品简介")
    private String itemDescription;
    /**
     * 单价
     */
    @Schema(description = "单价")
    private BigDecimal itemPrice;
    /**
     * 注释标记
     */
    @Schema(description = "注释标记")
    private List<String> noteTags;
    /**
     * 单品类型
     */
    @Schema(description = "单品类型")
    private MenuItemTypeEnum itemType;
    /**
     * 单品图片
     */
    @Schema(description = "单品图片")
    private String itemImage;
}
