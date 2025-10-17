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
@Table(name = "menu_category_items", schema = "pos_db")
public class MenuCategoryItem {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "menu_id", nullable = false, columnDefinition = "CHAR(36)")
    private String menuId;

    @Column(name = "category_id", nullable = false, columnDefinition = "CHAR(36)")
    private String categoryId;

    @Column(name = "item_id", nullable = false, columnDefinition = "CHAR(36)")
    private String itemId;

    @Column(name = "display_order", columnDefinition = "INT")
    private Integer sortOrder;

    @Column(name = "created_at", columnDefinition = "BIGINT UNSIGNED")
    private Long createdAt;

    @Column(name = "updated_at", columnDefinition = "BIGINT UNSIGNED")
    private Long updatedAt;

    @ColumnDefault("0")
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT")
    private Boolean isDeleted;
}
