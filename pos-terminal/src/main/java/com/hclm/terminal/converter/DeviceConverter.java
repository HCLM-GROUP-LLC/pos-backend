package com.hclm.terminal.converter;


import com.hclm.terminal.pojo.request.DeviceAddRequest;
import com.hclm.terminal.pojo.response.DeviceAddResponse;
import com.hclm.web.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 设备相关对象转化
 *
 * @author hanhua
 * @since 2025/10/05
 */
@Mapper
public interface DeviceConverter {
    DeviceConverter INSTANCE = Mappers.getMapper(DeviceConverter.class);

    /**
     * 请求转实体
     *
     * @param request 请求
     * @return {@link Device }
     */
    Device toEntity(DeviceAddRequest request);

    /**
     * 响应
     *
     * @param device 设备
     * @return {@link DeviceAddResponse }
     */
    DeviceAddResponse toAddResponse(Device device);
}
