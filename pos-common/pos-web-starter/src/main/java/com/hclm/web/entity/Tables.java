package com.hclm.web.entity;

import com.hclm.web.constant.TableNameConstant;
import com.hclm.web.enums.DiningTableShapeEnum;
import com.hclm.web.enums.DiningTableStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 桌子
 *
 * @author hanhua
 * @since 2025/10/13
 */
@Data
@Entity
@Table(name = TableNameConstant.TABLES)
public class Tables {
    /**
     * 餐桌id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dining_table_id")
    private Long diningTableId;
    /**
     * 所属门店id
     */
    private String storeId;
    /**
     * 所属楼层平面id
     */
    private Long floorPlanId;
    /**
     * 餐桌名称
     */
    private String diningTableName;
    /**
     * 容量
     */
    private Integer capacity;
    /**
     * 使用容量
     */
    private Integer usedCapacity;
    /**
     * 形状
     */
    @Enumerated(EnumType.STRING) // 枚举类型，使用name 存储
    private DiningTableShapeEnum shape;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;
    /**
     * 坐标x
     */
    private Integer positionX;
    /**
     * 坐标y
     */
    private Integer positionY;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING) // 枚举类型，使用name 存储
    private DiningTableStatusEnum status;
    /**
     * 开台服务员ID（员工表ID），即桌子对应的服务员
     */
    private String opener;
}
