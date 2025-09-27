package com.example.pos_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "device_codes", schema = "pos_db")
public class DeviceCode {
    @Id
    @Column(name = "device_code_id", nullable = false, columnDefinition = "CHAR(36)")
    private String deviceCodeId;

    @Size(max = 12)
    @NotNull
    @Column(name = "device_code", nullable = false, length = 12, unique = true)
    private String deviceCode;

    @Column(name = "device_id", columnDefinition = "CHAR(36)")
    private String deviceId;

    @Size(max = 255)
    @Column(name = "device_fingerprint", length = 255)
    private String deviceFingerprint;

    @ColumnDefault("0")
    @Column(name = "activation_attempts")
    private Integer activationAttempts;

    @ColumnDefault("3")
    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'UNUSED'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "issued_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant issuedAt;

    @Column(name = "expired_at", columnDefinition = "TIMESTAMP")
    private Instant expiredAt;

    @Column(name = "bound_at", columnDefinition = "TIMESTAMP")
    private Instant boundAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updatedAt;

    @Column(name = "created_by", columnDefinition = "CHAR(36)")
    private String createdBy;

    @Column(name = "updated_by", columnDefinition = "CHAR(36)")
    private String updatedBy;

    @ColumnDefault("0")
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;
}