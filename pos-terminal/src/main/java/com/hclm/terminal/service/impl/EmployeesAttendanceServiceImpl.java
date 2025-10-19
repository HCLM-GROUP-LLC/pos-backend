package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.terminal.service.EmployeesAttendanceService;
import com.hclm.web.entity.EmployeesAttendance;
import com.hclm.web.mapper.EmployeesAttendanceMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeesAttendanceServiceImpl extends ServiceImpl<EmployeesAttendanceMapper, EmployeesAttendance> implements EmployeesAttendanceService {
}
