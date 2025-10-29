package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.OrderEntity;
import com.hclm.mybatis.entity.OrderItemEntity;
import com.hclm.mybatis.mapper.OrderMapper;
import com.hclm.terminal.converter.OrderConverter;
import com.hclm.terminal.pojo.request.OrderCreateRequest;
import com.hclm.terminal.service.OrderItemService;
import com.hclm.terminal.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
    private final OrderItemService orderItemService;

    @Transactional
    @Override
    public void create(OrderCreateRequest request) {
        OrderEntity orderEntity = OrderConverter.INSTANCE.toEntity(request);
        this.save(orderEntity); //保存订单
        //保存订单详情
        List<OrderItemEntity> orderDetails = OrderConverter.INSTANCE.toEntity(request.getOrderItems());
        for (OrderItemEntity orderDetail : orderDetails) {
            orderDetail.setOrderId(orderEntity.getOrderId());//订单ID
        }
        //批量保存
        orderItemService.saveBatch(orderDetails);
    }
}
