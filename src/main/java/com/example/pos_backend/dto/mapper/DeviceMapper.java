package com.example.pos_backend.dto.mapper;

import com.example.pos_backend.dto.request.DeviceAddRequestDTO;
import com.example.pos_backend.dto.response.DeviceAddResponse;
import com.example.pos_backend.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 设备相关对象转化
 *
 * @author hanhua
 * @since 2025/10/05
 */
@Mapper
public interface DeviceMapper {
    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    /**
     * 请求转实体
     *
     * @param request 请求
     * @return {@link Device }
     */
    Device toEntity(DeviceAddRequestDTO request);

    /**
     * 响应
     *
     * @param device 设备
     * @return {@link DeviceAddResponse }
     */
    DeviceAddResponse toAddResponse(Device device);
}
