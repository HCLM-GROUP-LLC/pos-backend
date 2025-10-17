package com.hclm.web.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "menus", schema = "pos_db")
public class Menu {

    @Id
    @Column(name = "menu_id", nullable = false, columnDefinition = "CHAR(36)")
    private String menuId;

    @Column(name = "merchant_id", nullable = false, columnDefinition = "CHAR(36)")
    private String merchantId;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_time", columnDefinition = "VARCHAR(20)")
    private String startTime;

    @Column(name = "end_time", columnDefinition = "VARCHAR(20)")
    private String endTime;

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
