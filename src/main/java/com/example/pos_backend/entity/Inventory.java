package com.example.pos_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue
    @Column(name = "inventory_id", updatable = false, nullable = false, length = 36, columnDefinition = "CHAR(36)")
    private UUID inventoryId;

    @Column(name = "product_id", nullable = false, unique = true, length = 36, columnDefinition = "CHAR(36)")
    private UUID productId;

    private Integer currentStock;
    private Integer minStock;
    private Integer maxStock;
    
    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double costPrice;

    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "CHAR(36)")
    private UUID createdBy;

    @Column(columnDefinition = "CHAR(36)")
    private UUID updatedBy;

    @Builder.Default
    private Boolean isDeleted = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
}
