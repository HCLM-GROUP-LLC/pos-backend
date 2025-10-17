package com.hclm.web.repository;

import com.hclm.web.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String>, JpaSpecificationExecutor<Item> {

    /**
     * 按ID查找菜品
     *
     * @param itemId 菜品ID
     * @return {@link Optional<Item>}
     */
    Optional<Item> findById(String itemId);

    /**
     * 按商户ID查找菜品列表（如果Item实体里有merchantId字段）
     *
     * @param merchantId 商户ID
     * @return 菜品列表
     */
    List<Item> findByMerchantId(String merchantId);

    /**
     * 更新菜品状态
     *
     * @param itemId 菜品ID
     * @param status 状态
     */
    @Modifying
    @Query("UPDATE Item i SET i.status = :status WHERE i.itemId = :itemId")
    void updateStatus(@Param("itemId") String itemId, @Param("status") String status);

    /**
     * 按ID进行软删除
     *
     * @param itemId 菜品ID
     */
    @Modifying
    @Query("UPDATE Item i SET i.isDeleted = true WHERE i.itemId = :itemId")
    void softDeleteById(@Param("itemId") String itemId);
}

