package com.example.pos_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories", schema = "pos_db")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id", updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private UUID categoryId;

    @Column(name = "store_id", nullable = false, columnDefinition = "CHAR(36)")
    private UUID storeId;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "display_order", columnDefinition = "INT DEFAULT 0")
    private Integer displayOrder;

    @Builder.Default
    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", columnDefinition = "CHAR(36)")
    private UUID createdBy;

    @Column(name = "updated_by", columnDefinition = "CHAR(36)")
    private UUID updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
