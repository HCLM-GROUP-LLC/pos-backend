package com.hclm.web.repository;

import com.hclm.web.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, String>, JpaSpecificationExecutor<MenuCategory> {

    /**
     * 按ID查找类目
     */
    Optional<MenuCategory> findById(String categoryId);

    /**
     * 查询未删除类目列表
     */
    List<MenuCategory> findByIsDeletedFalse();

    /**
     * 更新类目信息
     */
    @Modifying
    @Query("UPDATE MenuCategory c SET c.name = :name, c.description = :description, c.sortOrder = :sortOrder WHERE c.categoryId = :categoryId")
    void updateCategory(@Param("categoryId") String categoryId,
                        @Param("name") String name,
                        @Param("description") String description,
                        @Param("sortOrder") Integer sortOrder);

    /**
     * 逻辑删除类目
     */
    @Modifying
    @Query("UPDATE MenuCategory c SET c.isDeleted = true WHERE c.categoryId = :categoryId")
    void softDeleteById(@Param("categoryId") String categoryId);
}
