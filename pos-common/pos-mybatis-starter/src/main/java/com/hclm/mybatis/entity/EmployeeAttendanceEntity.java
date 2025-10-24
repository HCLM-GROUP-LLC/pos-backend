package com.hclm.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hclm.mybatis.TableNameConstant;
import lombok.Data;

/**
 * 考勤表
 */
@Data
@TableName(TableNameConstant.EMPLOYEES_ATTENDANCE)
public class EmployeeAttendanceEntity {
    /**
     * 考勤主键
     */
    @TableId(type = IdType.AUTO)
    private Long attendanceId;
    /**
     * 商家ID
     */
    private String merchantId;
    /**
     * 员工ID
     */
    private String employeesId;

    /**
     * 所属店铺
     */
    private String storeId;

    /**
     * 上班打卡时间,毫秒级时间戳
     */
    private Long clockInTime;

    /**
     * 下班打卡时间,毫秒级时间戳
     */
    private Long clockOutTime;

    /**
     * 工作总时长=clock_in_time-clock_out_time
     */
    private Long totalTime;
}
