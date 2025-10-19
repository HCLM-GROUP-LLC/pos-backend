package com.hclm.web.repository;

import com.hclm.web.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Merchant实体的Repository接口
 * 提供商家相关的数据访问方法
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String>, JpaSpecificationExecutor<Merchant> {

    /**
     * 根据邮箱查找商家
     */
    Optional<Merchant> findByEmail(String email);

    /**
     * 根据邮箱查找商家（排除已删除）
     */
    Optional<Merchant> findByEmailAndIsDeleted(String email, Boolean isDeleted);

    /**
     * 根据手机号查找商家（排除已删除）
     */
    Optional<Merchant> findByPhoneNumberAndIsDeleted(String phoneNumber, Boolean isDeleted);

    /**
     * 根据企业名称查找商家
     */
    List<Merchant> findByBusinessName(String businessName);

    /**
     * 根据企业名称模糊查询
     */
    List<Merchant> findByBusinessNameContaining(String businessName);

    /**
     * 根据状态查找商家
     */
    List<Merchant> findByStatus(String status);

    /**
     * 根据状态和删除标记查找商家
     */
    List<Merchant> findByStatusAndIsDeleted(String status, Boolean isDeleted);

    /**
     * 查找未删除的商家
     */
    List<Merchant> findByIsDeleted(Boolean isDeleted);

    /**
     * 根据创建时间范围查找商家
     */
    List<Merchant> findByCreatedAtBetween(Long startTime, Long endTime);

    /**
     * 查找活跃商家（状态为ACTIVE且未删除）
     */
    @Query("SELECT m FROM Merchant m WHERE m.status = 'ACTIVE' AND m.isDeleted = false")
    List<Merchant> findActiveMerchants();

    /**
     * 查找非活跃商家（状态不为ACTIVE或已删除）
     */
    @Query("SELECT m FROM Merchant m WHERE m.status != 'ACTIVE' OR m.isDeleted = true")
    List<Merchant> findInactiveMerchants();

    /**
     * 统计指定状态的商家数量
     */
    @Query("SELECT COUNT(m) FROM Merchant m WHERE m.status = :status AND m.isDeleted = false")
    long countByStatusAndNotDeleted(@Param("status") String status);

    /**
     * 根据企业名称或邮箱模糊查询
     */
    @Query("SELECT m FROM Merchant m WHERE (m.businessName LIKE %:keyword% OR m.email LIKE %:keyword%) AND m.isDeleted = false")
    List<Merchant> findByBusinessNameOrEmailContaining(@Param("keyword") String keyword);

    /**
     * 检查邮箱是否已存在（排除指定商家ID）
     */
//    @Query("SELECT COUNT(m) > 0 FROM Merchant m WHERE m.email = :email AND m.id != :merchantId AND m.isDeleted = false")
//    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("merchantId") String merchantId);

    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmailAndIsDeleted(String email, Boolean isDeleted);

    /**
     * 检查手机号是否已存在
     */
    boolean existsByPhoneNumberAndIsDeleted(String phoneNumber, Boolean isDeleted);

    /**
     * 根据多个状态查找商家
     */
    @Query("SELECT m FROM Merchant m WHERE m.status IN :statuses AND m.isDeleted = false")
    List<Merchant> findByStatusIn(@Param("statuses") List<String> statuses);

    /**
     * 查找最近注册的商家
     */
    @Query("SELECT m FROM Merchant m WHERE m.isDeleted = false ORDER BY m.createdAt DESC")
    List<Merchant> findRecentMerchants();

    /**
     * 根据企业地址模糊查询
     */
    List<Merchant> findByBusinessAddressContaining(String address);

    /**
     * 根据商户名称模糊查询
     */
    List<Merchant> findByNameContaining(String name);
}
