package com.example.pos_backend.repository;

import com.example.pos_backend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.orgId = :orgId AND mi.isDeleted = false ORDER BY mi.sortOrder ASC")
    List<MenuItem> findByOrgIdAndNotDeleted(@Param("orgId") UUID orgId);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.storeId = :storeId AND mi.isDeleted = false ORDER BY mi.sortOrder ASC")
    List<MenuItem> findByStoreIdAndNotDeleted(@Param("storeId") UUID storeId);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.categoryId = :categoryId AND mi.isDeleted = false ORDER BY mi.sortOrder ASC")
    List<MenuItem> findByCategoryIdAndNotDeleted(@Param("categoryId") UUID categoryId);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.orgId = :orgId AND mi.storeId = :storeId AND mi.isDeleted = false ORDER BY mi.sortOrder ASC")
    List<MenuItem> findByOrgIdAndStoreIdAndNotDeleted(@Param("orgId") UUID orgId, @Param("storeId") UUID storeId);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.orgId = :orgId AND mi.storeId = :storeId AND mi.categoryId = :categoryId AND mi.isDeleted = false ORDER BY mi.sortOrder ASC")
    List<MenuItem> findByOrgIdAndStoreIdAndCategoryIdAndNotDeleted(@Param("orgId") UUID orgId, @Param("storeId") UUID storeId, @Param("categoryId") UUID categoryId);
}