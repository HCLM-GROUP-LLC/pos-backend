package com.hclm.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hclm.mybatis.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
}
