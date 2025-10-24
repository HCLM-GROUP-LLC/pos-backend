package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.EmployeeAttendanceEntity;
import com.hclm.mybatis.mapper.EmployeeAttendanceMapper;
import com.hclm.terminal.service.EmployeesAttendanceService;
import org.springframework.stereotype.Service;

@Service
public class EmployeesAttendanceServiceImpl extends ServiceImpl<EmployeeAttendanceMapper, EmployeeAttendanceEntity> implements EmployeesAttendanceService {
}
