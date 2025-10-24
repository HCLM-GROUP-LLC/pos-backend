package com.hclm.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hclm.mybatis.TableNameConstant;
import lombok.Data;

@Data
@TableName(TableNameConstant.FLOOR_PLANS)
public class FloorPlanEntity {
    /**
     * 平面图id
     */
    @TableId(type = IdType.AUTO)
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
