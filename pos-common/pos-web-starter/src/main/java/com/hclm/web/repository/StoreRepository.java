package com.hclm.web.repository;

import com.hclm.web.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Store实体的Repository接口
 * 提供店铺相关的数据访问方法
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, String>, JpaSpecificationExecutor<Store> {

    /**
     * 按店铺ID查找
     *
     * @param id 店铺ID
     * @return {@link Optional<Store>}
     */
    Optional<Store> findById(String id);

    /**
     * 按商户ID查找店铺列表
     *
     * @param merchantId 商户ID
     * @return 店铺列表
     */
    List<Store> findByMerchantId(String merchantId);

    /**
     * 更新店铺状态
     *
     * @param id     店铺ID
     * @param status 状态
     */
    @Modifying
    @Query("UPDATE Store s SET s.status = :status WHERE s.id = :id")
    void updateStatus(@Param("id") String id, @Param("status") String status);

    /**
     * 更新营业时间
     *
     * @param id            店铺ID
     * @param businessHours 营业时间(JSON)
     */
    @Modifying
    @Query("UPDATE Store s SET s.businessHours = :businessHours WHERE s.id = :id")
    void updateBusinessHours(@Param("id") String id, @Param("businessHours") String businessHours);

    /**
     * 按ID进行软删除
     *
     * @param id 店铺ID
     */
    @Modifying
    @Query("UPDATE Store s SET s.isDeleted = true WHERE s.id = :id")
    void softDeleteById(@Param("id") String id);

    /**
     * 检查门店是否属于指定商家
     *
     * @param storeId    门店ID
     * @param merchantId 商家ID
     * @return 是否存在
     */
    boolean existsByIdAndMerchantId(String storeId, String merchantId);
}
