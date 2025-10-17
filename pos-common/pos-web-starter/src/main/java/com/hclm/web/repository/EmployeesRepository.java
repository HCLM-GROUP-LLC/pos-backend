package com.hclm.web.repository;

import com.hclm.web.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * 查找由门店id和不已删除
     *
     * @param storeId 门店id
     * @return {@link List }<{@link Employees }>
     */
    @Query("SELECT e FROM Employees e WHERE e.isDeleted = false AND e.storeId = :storeId")
    List<Employees> findByStoreIdAndNotDeleted(@Param("storeId") String storeId);

    /**
     * 软删除
     *
     * @param employeesId 员工id
     * @return int
     */
    @Modifying
    @Query("UPDATE Employees e SET e.isDeleted = true WHERE e.employeesId = :employeesId")
    int softDelete(@Param("id") String employeesId);

    /**
     * 查找由门店id和商户id和未删除
     *
     * @param storeId    门店id
     * @param merchantId 商户id
     * @return {@link List }<{@link Employees }>
     */
    @Query("SELECT e FROM Employees e WHERE e.isDeleted = false AND e.storeId = :storeId AND e.merchantId = :merchantId")
    List<Employees> findByStoreIdAndMerchantIdAndNotDeleted(@Param("storeId") String storeId, @Param("merchantId") String merchantId);

    /**
     * 检查邮箱在指定店铺中是否已存在（未删除）
     *
     * @param email   邮箱
     * @param storeId 店铺ID
     * @return boolean
     */
    boolean existsByEmailAndStoreIdAndIsDeletedFalse(String email, String storeId);
}
