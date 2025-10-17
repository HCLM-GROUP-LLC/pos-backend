package com.hclm.web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items", schema = "pos_db")
public class Item {

    @Id
    @Column(name = "item_id", nullable = false, columnDefinition = "CHAR(36)")
    private String itemId;

    @Column(name = "merchant_id", nullable = false, columnDefinition = "CHAR(36)")
    private String merchantId;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal price;

    @Column(name = "image_url", columnDefinition = "VARCHAR(255)")
    private String imageUrl;

    @ColumnDefault("0")
    @Column(name = "is_combo", nullable = false, columnDefinition = "TINYINT")
    private Boolean isCombo;

    @Column(name = "combo_items", columnDefinition = "TEXT")
    private String comboItems;

    @Column(name = "status", columnDefinition = "VARCHAR(50)")
    private String status;

    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT")
    private Boolean isActive;

    @Column(name = "created_at", columnDefinition = "BIGINT UNSIGNED")
    private Long createdAt;

    @Column(name = "updated_at", columnDefinition = "BIGINT UNSIGNED")
    private Long updatedAt;

    @ColumnDefault("0")
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT")
    private Boolean isDeleted;
}
