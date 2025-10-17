package com.hclm.web.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "merchants", schema = "pos_db")
public class Merchant {
    @Id
    @Column(name = "merchant_id", nullable = false, columnDefinition = "CHAR(36)")
    private String merchantId;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "phone_number", nullable = false, columnDefinition = "VARCHAR(20)")
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false, columnDefinition = "VARCHAR(255)")
    private String passwordHash;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "business_name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String businessName;

    @Column(name = "business_address", nullable = false, columnDefinition = "VARCHAR(255)")
    private String businessAddress;

    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(50)")
    private String status;

    @Column(name = "last_login_at", columnDefinition = "BIGINT UNSIGNED")
    private Long lastLoginAt;

    @Column(name = "created_at", columnDefinition = "BIGINT UNSIGNED")
    private Long createdAt;

    @Column(name = "updated_at", columnDefinition = "BIGINT UNSIGNED")
    private Long updatedAt;

    @ColumnDefault("0")
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT")
    private Boolean isDeleted;
}