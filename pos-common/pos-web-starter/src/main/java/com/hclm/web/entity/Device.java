package com.hclm.web.entity;

import com.hclm.web.constant.TableNameConstant;
import com.hclm.web.enums.DeviceStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

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

    @Column(name = "last_online", columnDefinition = "TIMESTAMP")
    private Instant lastOnline;

    @ColumnDefault("'OFFLINE'")
    @Column(name = "status", nullable = false, length = 20)
    private String status = DeviceStatusEnum.OFFLINE.getCode();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "registered_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant registeredAt;

    /**
     * 上次登录于
     */
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_login_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant lastLoginAt;


    @ColumnDefault("0")
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;

}