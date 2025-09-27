package com.example.pos_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory", schema = "pos_db")
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

    @Column(name = "current_stock", nullable = false, columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer currentStock;
    
    @Column(name = "min_stock", columnDefinition = "INT DEFAULT 0")
    private Integer minStock;
    
    @Column(name = "max_stock", columnDefinition = "INT DEFAULT 0")
    private Integer maxStock;
    
    @Column(name = "cost_price", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal costPrice;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdated;
    
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", columnDefinition = "CHAR(36)")
    private UUID createdBy;

    @Column(name = "updated_by", columnDefinition = "CHAR(36)")
    private UUID updatedBy;

    @Builder.Default
    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
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
