package com.hclm.terminal.converter;

import com.hclm.mybatis.entity.EmployeeEntity;
import com.hclm.terminal.pojo.cache.EmployeesLoginCache;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 员工映射器
 *
 * @author hanhua
 * @since 2025/10/09
 */
@Mapper
public interface EmployeesConverter {
    EmployeesConverter INSTANCE = Mappers.getMapper(EmployeesConverter.class);

    /**
     * 登录响应
     *
     * @param employeeEntity 员工
     * @return {@link EmployeesLoginResponse }
     */
    EmployeesLoginResponse toLoginResponse(EmployeeEntity employeeEntity);

    /**
     * 登录缓存
     *
     * @param employeeEntity 员工
     * @return {@link EmployeesLoginCache }
     */
    EmployeesLoginCache toLoginCache(EmployeeEntity employeeEntity);
}
