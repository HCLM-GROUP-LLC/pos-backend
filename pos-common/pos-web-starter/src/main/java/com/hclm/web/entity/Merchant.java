package com.hclm.web.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "merchants", schema = "pos_db")
public class Merchant {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String email;
    private String phoneNumber;
    @Column(name = "password_hash", nullable = false, columnDefinition = "VARCHAR(255)")
    private String passwordHash;
    private String name;
    @Column(name = "business_name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String businessName;
    private String businessAddress;
    @Column(name = "industry", nullable = false, length = 100)
    private String industry;

    @ColumnDefault("'USD'")
    @Column(name = "currency", nullable = false, columnDefinition = "CHAR(3)")
    private String currency;

    @ColumnDefault("'US'")
    @Column(name = "country", nullable = false, columnDefinition = "CHAR(2)")
    private String country;

    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", length = 50)
    private String status;

    private Long createdAt;

    @Column(name = "created_by", columnDefinition = "CHAR(36)")
    private String createdBy;

    private Long updatedAt;

    @Column(name = "updated_by", columnDefinition = "CHAR(36)")
    private String updatedBy;

    @ColumnDefault("0")
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

}