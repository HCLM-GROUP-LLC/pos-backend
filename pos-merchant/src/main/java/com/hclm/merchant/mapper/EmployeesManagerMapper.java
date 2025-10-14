package com.hclm.merchant.mapper;

import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.web.entity.Employees;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
     * 至响应
     *
     * @param employees 员工
     * @return {@link EmployeesResponse }
     */
    EmployeesResponse toResponse(Employees employees);
}
