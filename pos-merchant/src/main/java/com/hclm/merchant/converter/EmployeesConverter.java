package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.request.EmployeesUpdateRequest;
import com.hclm.merchant.pojo.response.EmployeesCopyResponse;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.mybatis.entity.EmployeeEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeesConverter {
    EmployeesConverter INSTANCE = Mappers.getMapper(EmployeesConverter.class);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link EmployeeEntity }
     */
    EmployeeEntity toEntity(EmployeesAddRequest request);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link EmployeeEntity }
     */
    EmployeeEntity toEntity(EmployeesUpdateRequest request);

    /**
     * 复制实体
     *
     * @param target 目标
     * @param source 来源
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copyEntity(@MappingTarget EmployeeEntity target, EmployeeEntity source);

    /**
     * 至响应
     *
     * @param employeeEntity 员工
     * @return {@link EmployeesResponse }
     */
    EmployeesResponse toResponse(EmployeeEntity employeeEntity);

    /**
     * 至响应列表
     *
     * @param employees 员工
     * @return {@link List }<{@link EmployeesResponse }>
     */
    List<EmployeesResponse> toResponseList(List<EmployeeEntity> employees);

    /**
     * 至复制响应
     *
     * @param employeeEntity 员工
     * @return {@link EmployeesCopyResponse }
     */
    EmployeesCopyResponse toCopyResponse(EmployeeEntity employeeEntity);

}
