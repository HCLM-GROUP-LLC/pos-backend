package com.hclm.web.repository;

import com.hclm.web.entity.MenuCategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuCategoryItemRepository extends JpaRepository<MenuCategoryItem, String>, JpaSpecificationExecutor<MenuCategoryItem> {

    /**
     * 逻辑删除指定记录
     */
    @Modifying
    @Query("UPDATE MenuCategoryItem mci SET mci.isDeleted = true WHERE mci.id = :id")
    void softDeleteById(@Param("id") String id);
}
