package com.example.pos_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "menu_categories", schema = "pos_db")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "org_id", nullable = false, length = 36, columnDefinition = "CHAR(36)")
    private UUID orgId;

    @Column(name = "store_id", nullable = false, length = 36, columnDefinition = "CHAR(36)")
    private UUID storeId;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 36, columnDefinition = "CHAR(36)")
    private UUID createdBy;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 36, columnDefinition = "CHAR(36)")
    private UUID updatedBy;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private Boolean isDeleted = false;
}
