package com.hclm.web.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.hclm.web.constant.TableNameConstant;
import com.hclm.web.enums.MerchantStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(TableNameConstant.MERCHANT)
public class Merchant {
    @TableId(type = IdType.INPUT)
    private String id;

    private String email;
    private String phoneNumber;
    private String passwordHash;
    private String name;
    private String businessName;
    private String businessAddress;
    private String industry;

    private String currency;

    private String country;

    private MerchantStatusEnum status;
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    private String createdBy;
    @TableField(fill = FieldFill.UPDATE)
    private Long updatedAt;

    private String updatedBy;
    @TableLogic
    private Boolean isDeleted;

}