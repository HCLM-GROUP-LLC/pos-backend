package com.hclm.web.entity;

import com.hclm.web.constant.TableNameConstant;
import com.hclm.web.enums.EmployeesRoleEnum;
import com.hclm.web.enums.EmployeesSatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

/**
 * 员工实体类
 *
 * @author hanhua
 * @since 2025/10/09
 */
@Entity
@Table(name = TableNameConstant.EMPLOYEES)
@Data
public class Employees {

    /**
     * 员工（Square风格）
     */
    @Id
    @Column(name = "employees_id", nullable = false, columnDefinition = "CHAR(36)")
    private String employeesId;

    /**
     * 商家ID
     */
    @Column(name = "merchant_id", nullable = false, columnDefinition = "CHAR(36)")
    private String merchantId;

    /**
     * 门店ID
     */
    @Column(name = "store_id", nullable = false, columnDefinition = "CHAR(36)")
    private String storeId;

    /**
     * 邮箱（唯一）
     */
    @Column(name = "email")
    private String email;

    /**
     * PIN码哈希
     */
    @Column(name = "pass_code")
    private String passCode;

    /**
     * 名
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * 姓
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    @Enumerated(EnumType.STRING) // 枚举类型，使用name 存储
    private EmployeesRoleEnum roleId;

    /**
     * 账号状态
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmployeesSatusEnum status = EmployeesSatusEnum.ACTIVE;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_at")
    private Long lastLoginAt;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private Long createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private Long updatedAt;

    /**
     * 删除标记
     */
    @ColumnDefault("0")
    @Column(name = "is_deleted", columnDefinition = "tinyint unsigned default 0")
    private Boolean isDeleted = false;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = System.currentTimeMillis();
    }
}
