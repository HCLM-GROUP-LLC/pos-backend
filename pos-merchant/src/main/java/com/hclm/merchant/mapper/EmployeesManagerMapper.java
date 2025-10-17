package com.hclm.merchant.mapper;

import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.request.EmployeesUpdateRequest;
import com.hclm.merchant.pojo.response.EmployeesCopyResponse;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.web.entity.Employees;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeesManagerMapper {
    EmployeesManagerMapper INSTANCE = Mappers.getMapper(EmployeesManagerMapper.class);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link Employees }
     */
    Employees toEntity(EmployeesAddRequest request);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link Employees }
     */
    Employees toEntity(EmployeesUpdateRequest request);

    /**
     * 将 EmployeesUpdateRequest 中的非null值复制到 Employees 实体中
     *
     * @param employees 员工实体
     * @param request   更新请求
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(@MappingTarget Employees employees, EmployeesUpdateRequest request);

    /**
     * 至响应
     *
     * @param employees 员工
     * @return {@link EmployeesResponse }
     */
    EmployeesResponse toResponse(Employees employees);

    /**
     * 至响应列表
     *
     * @param employees 员工
     * @return {@link List }<{@link EmployeesResponse }>
     */
    List<EmployeesResponse> toResponseList(List<Employees> employees);

    /**
     * 至复制响应
     *
     * @param employees 员工
     * @return {@link EmployeesCopyResponse }
     */
    EmployeesCopyResponse toCopyResponse(Employees employees);

}
