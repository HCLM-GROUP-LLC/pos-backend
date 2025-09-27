package com.example.pos_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_permissions", schema = "pos_db")
@IdClass(RolePermissionId.class)
public class RolePermission {
    @Id
    @Column(name = "role_id", length = 36, columnDefinition = "CHAR(36)")
    private String roleId;

    @Id
    @Column(name = "permission_id", length = 36, columnDefinition = "CHAR(36)")
    private String permissionId;

    @Column(name = "granted_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime grantedAt;

    // Getters and Setters
    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }

    public LocalDateTime getGrantedAt() { return grantedAt; }
    public void setGrantedAt(LocalDateTime grantedAt) { this.grantedAt = grantedAt; }
}

