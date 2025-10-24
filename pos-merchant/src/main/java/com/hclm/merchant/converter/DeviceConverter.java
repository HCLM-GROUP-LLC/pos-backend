package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.response.DeviceResponse;
import com.hclm.mybatis.entity.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.lang.NonNull;

import java.util.List;

@Mapper
public interface DeviceConverter {
    DeviceConverter INSTANCE = Mappers.getMapper(DeviceConverter.class);

    /**
     * 响应
     *
     * @param deviceEntity 设备
     * @return {@link DeviceResponse }
     */
    DeviceResponse toResponse(DeviceEntity deviceEntity);

    default List<DeviceResponse> toResponses(@NonNull List<DeviceEntity> deviceEntities) {
        return deviceEntities.stream().map(this::toResponse).toList();
    }
}
