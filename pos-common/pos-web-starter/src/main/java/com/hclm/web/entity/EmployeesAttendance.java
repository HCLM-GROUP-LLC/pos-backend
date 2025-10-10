package com.hclm.web.entity;

import com.hclm.web.constant.TableNameConstant;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 考勤表
 */
@Data
@Entity
@Table(name = TableNameConstant.EMPLOYEES_ATTENDANCE)
public class EmployeesAttendance {
    /**
     * 考勤主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    /**
     * 员工ID
     */
    @Column(name = "employees_id")
    private String employeesId;

    /**
     * 所属店铺
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 上班打卡时间,毫秒级时间戳
     */
    @Column(name = "clock_in_time")
    private Long clockInTime;

    /**
     * 下班打卡时间,毫秒级时间戳
     */
    @Column(name = "clock_out_time")
    private Long clockOutTime;

    /**
     * 工作总时长=clock_in_time-clock_out_time
     */
    @Column(name = "total_time")
    private Long totalTime;
}
