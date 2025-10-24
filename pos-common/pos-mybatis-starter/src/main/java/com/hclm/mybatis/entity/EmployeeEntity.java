package com.hclm.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hclm.mybatis.TableNameConstant;
import com.hclm.mybatis.enums.EmployeesRoleEnum;
import com.hclm.mybatis.enums.EmployeesSatusEnum;
import lombok.Data;

/**
 * 员工实体类
 *
 * @author hanhua
 * @since 2025/10/09
 */

@TableName(TableNameConstant.EMPLOYEES)
@Data
public class EmployeeEntity {

    /**
     * 员工（Square风格）
     */
    @TableId(type = IdType.INPUT)
    private String employeesId;

    /**
     * 商家ID
     */
    private String merchantId;

    /**
     * 门店ID
     */
    private String storeId;

    /**
     * 邮箱（唯一）
     */
    private String email;

    /**
     * PIN码哈希
     */
    private String passCode;

    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 角色ID
     */
    private EmployeesRoleEnum roleId;

    /**
     * 账号状态
     */
    private EmployeesSatusEnum status = EmployeesSatusEnum.ACTIVE;

    /**
     * 最后登录时间
     */
    private Long lastLoginAt;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updatedAt;

    /**
     * 删除标记 查询时 自动附加条件
     */
    @TableLogic
    private Boolean isDeleted;

}
