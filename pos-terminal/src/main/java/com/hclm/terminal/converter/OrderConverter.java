package com.hclm.terminal.converter;

import com.hclm.mybatis.entity.OrderEntity;
import com.hclm.mybatis.entity.OrderItemEntity;
import com.hclm.terminal.pojo.request.OrderCreateRequest;
import com.hclm.terminal.pojo.request.OrderItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 订单转换器
 *
 * @author hanhua
 * @since 2025/10/30
 */
@Mapper
public interface OrderConverter {
    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link OrderEntity }
     */
    OrderEntity toEntity(OrderCreateRequest request);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link OrderItemEntity }
     */
    OrderItemEntity toEntity(OrderItemRequest request);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link List }<{@link OrderItemEntity }>
     */
    List<OrderItemEntity> toEntity(List<OrderItemRequest> request);
}
