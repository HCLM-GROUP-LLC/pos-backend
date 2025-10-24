package com.hclm.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hclm.mybatis.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeeEntity> {
}
