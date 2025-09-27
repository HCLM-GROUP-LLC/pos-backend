package com.example.pos_backend.repository;

import com.example.pos_backend.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, UUID> {
    
    @Query("SELECT mc FROM MenuCategory mc WHERE mc.orgId = :orgId AND mc.isDeleted = false ORDER BY mc.sortOrder ASC")
    List<MenuCategory> findByOrgIdAndNotDeleted(@Param("orgId") UUID orgId);
    
    @Query("SELECT mc FROM MenuCategory mc WHERE mc.storeId = :storeId AND mc.isDeleted = false ORDER BY mc.sortOrder ASC")
    List<MenuCategory> findByStoreIdAndNotDeleted(@Param("storeId") UUID storeId);
    
    @Query("SELECT mc FROM MenuCategory mc WHERE mc.orgId = :orgId AND mc.storeId = :storeId AND mc.isDeleted = false ORDER BY mc.sortOrder ASC")
    List<MenuCategory> findByOrgIdAndStoreIdAndNotDeleted(@Param("orgId") UUID orgId, @Param("storeId") UUID storeId);
}