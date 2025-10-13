package com.hclm.web.entity;

import com.hclm.web.constant.TableNameConstant;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = TableNameConstant.FLOOR_PLANS)
public class FloorPlans {
    /**
     * 平面图id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_plan_id")
    private Long floorPlanId;
    /**
     * 所属门店id
     */
    private String storeId;
    /**
     * 平面图名称
     */
    private Long floorPlanName;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;
    /**
     * 桌子数量
     */
    private Integer tablesNumber;
    /**
     * 容量
     */
    private Integer capacity;
}
