package com.hclm.web.repository;

import com.hclm.web.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {

    /**
     * 按ID查找菜单
     */
    Optional<Menu> findById(String menuId);

    /**
     * 按商户ID查找未删除菜单列表
     */
    List<Menu> findByMerchantIdAndIsDeletedFalse(String merchantId);

    /**
     * 更新菜单启用状态
     */
    @Modifying
    @Query("UPDATE Menu m SET m.isActive = :isActive, m.updatedAt = :updatedAt WHERE m.menuId = :menuId")
    void updateIsActive(@Param("menuId") String menuId,
                        @Param("isActive") Boolean isActive,
                        @Param("updatedAt") Long updatedAt);

    /**
     * 逻辑删除菜单
     */
    @Modifying
    @Query("UPDATE Menu m SET m.isDeleted = true, m.updatedAt = :updatedAt WHERE m.menuId = :menuId")
    void softDeleteById(@Param("menuId") String menuId, @Param("updatedAt") Long updatedAt);

}


