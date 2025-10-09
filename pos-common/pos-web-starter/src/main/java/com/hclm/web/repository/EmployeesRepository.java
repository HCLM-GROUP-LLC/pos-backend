package com.hclm.web.repository;

import com.hclm.web.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 员工存储库
 *
 * @author hanhua
 * @since 2025/10/09
 */
@Repository
public interface EmployeesRepository extends JpaRepository<Employees, String> {

    /**
     * 通过密码和门店id查找
     *
     * @param passCode 通行码
     * @param storeId  Storeid
     * @return {@link Employees }
     */
    Optional<Employees> findByPassCodeAndStoreId(String passCode, String storeId);
}
