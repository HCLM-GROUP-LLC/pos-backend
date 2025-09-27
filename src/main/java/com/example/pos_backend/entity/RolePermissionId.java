package com.example.pos_backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class RolePermissionId implements Serializable {
    private String roleId;
    private String permissionId;

    public RolePermissionId() {}
    public RolePermissionId(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof RolePermissionId)) return false;
        RolePermissionId that = (RolePermissionId)o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }

    // Getters and Setters
    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }
}
