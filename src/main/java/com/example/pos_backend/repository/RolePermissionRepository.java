package com.example.pos_backend.repository;

import com.example.pos_backend.entity.Permission;
import com.example.pos_backend.entity.RolePermission;
import com.example.pos_backend.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.permissionId = rp.permissionId WHERE rp.roleId = :roleId")
    List<Permission> findPermissionsByRoleId(@Param("roleId") String roleId);
}
