package com.example.pos_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles", schema = "pos_db")
public class Role {
    @Id
    @Column(name = "role_id", length = 36, columnDefinition = "CHAR(36)")
    private String roleId;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    // Getters and Setters
    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
