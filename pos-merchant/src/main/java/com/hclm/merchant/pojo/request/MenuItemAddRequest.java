package com.hclm.merchant.pojo.request;

import com.hclm.mybatis.enums.MenuItemTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜单项添加请求
 *
 * @author hanhua
 * @since 2025/10/23
 */
@Schema(contentMediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
@Data
public class MenuItemAddRequest {
    /**
     * 所属门店id
     */
    @NotEmpty
    @Schema(description = "所属门店id")
    private String storeId;
    /**
     * 单品名称
     */
    @NotEmpty
    @Schema(description = "单品名称")
    private String itemName;
    /**
     * 菜品简介
     */
    @NotEmpty
    @Schema(description = "菜品简介")
    private String itemDescription;
    /**
     * 单价
     */
    @NotNull
    @Schema(description = "单价")
    private BigDecimal itemPrice;
    /**
     * 注释标记
     */
    @Schema(description = "词条")
    private List<String> noteTags;
    /**
     * 单品类型
     */
    @NotNull
    @Schema(description = "单品类型")
    private MenuItemTypeEnum itemType;
    @NotNull
    @Schema(description = "单品图片")
    private MultipartFile itemImage;
}
