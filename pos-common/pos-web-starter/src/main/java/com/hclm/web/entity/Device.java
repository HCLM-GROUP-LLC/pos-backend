package com.hclm.web.entity;

import com.hclm.web.constant.TableNameConstant;
import com.hclm.web.enums.DeviceStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("is_deleted = false or is_deleted is null") // 添加逻辑删除限制
@Data
@Entity
@Table(name = TableNameConstant.DEVICES)
public class Device {
    @Id
    @Column(name = "device_id", nullable = false, columnDefinition = "CHAR(36)")
    private String deviceId;

    @Column(name = "store_id", nullable = false, columnDefinition = "CHAR(36)")
    private String storeId;

    @Size(max = 100)
    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;

    @Size(max = 50)
    @Column(name = "device_type", nullable = false, length = 50)
    private String deviceType;

    @Size(max = 17)
    @Column(name = "mac_address", length = 17)
    private String macAddress;

    @Size(max = 15)
    @Column(name = "ip_address", length = 15)
    private String ipAddress;

    /**
     * 上次联机 毫秒级时间戳
     */
    @Column(name = "last_online")
    private Long lastOnline;

    @Enumerated(EnumType.STRING) // 枚举类型，使用name 存储
    @ColumnDefault("'OFFLINE'")
    @Column(name = "status", nullable = false, length = 20)
    private DeviceStatusEnum status = DeviceStatusEnum.OFFLINE;

    /**
     * 注册于 毫秒级时间戳
     */
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "registered_at")
    private Long registeredAt;

    /**
     * 上次登录于 毫秒级时间戳
     */
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_login_at")
    private Long lastLoginAt;


    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}