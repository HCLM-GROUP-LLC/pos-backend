package com.hclm.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hclm.web.entity.Employees;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeesMapper extends BaseMapper<Employees> {
}
