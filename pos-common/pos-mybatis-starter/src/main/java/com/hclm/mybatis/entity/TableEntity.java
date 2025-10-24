package com.hclm.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hclm.mybatis.TableNameConstant;
import com.hclm.mybatis.enums.DiningTableShapeEnum;
import com.hclm.mybatis.enums.DiningTableStatusEnum;
import lombok.Data;

/**
 * 桌子
 *
 * @author hanhua
 * @since 2025/10/13
 */
@Data
@TableName(TableNameConstant.TABLES)
public class TableEntity {
    /**
     * 餐桌id
     */
    @TableId(type = IdType.AUTO)
    private Long diningTableId;
    private String merchantId;
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
    private DiningTableStatusEnum status;
    /**
     * 开台服务员ID（员工表ID），即桌子对应的服务员
     */
    private String opener;
}
