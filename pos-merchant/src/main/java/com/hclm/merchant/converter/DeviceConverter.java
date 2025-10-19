package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.response.DeviceResponse;
import com.hclm.web.entity.Device;
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
     * @param device 设备
     * @return {@link DeviceResponse }
     */
    DeviceResponse toResponse(Device device);

    default List<DeviceResponse> toResponses(@NonNull List<Device> devices) {
        return devices.stream().map(this::toResponse).toList();
    }
}
