package com.hclm.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hclm.web.constant.TableNameConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(TableNameConstant.STORE)
public class Store {
    @TableId(type = IdType.INPUT)
    private String id;

    private String merchantId;


    private String storeName;

    private String address;

    private String timezone;

    private String status;

    private BigDecimal taxRate;

    private String currency;

    private String businessHours;
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    private String createdBy;
    @TableField(fill = FieldFill.UPDATE)
    private Long updatedAt;

    private String updatedBy;

    private Boolean isDeleted;
}