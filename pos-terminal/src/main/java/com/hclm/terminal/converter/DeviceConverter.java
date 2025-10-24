package com.hclm.terminal.converter;


import com.hclm.mybatis.entity.DeviceEntity;
import com.hclm.terminal.pojo.request.DeviceAddRequest;
import com.hclm.terminal.pojo.response.DeviceAddResponse;
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
     * @return {@link DeviceEntity }
     */
    DeviceEntity toEntity(DeviceAddRequest request);

    /**
     * 响应
     *
     * @param deviceEntity 设备
     * @return {@link DeviceAddResponse }
     */
    DeviceAddResponse toAddResponse(DeviceEntity deviceEntity);
}
