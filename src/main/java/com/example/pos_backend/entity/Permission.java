package com.example.pos_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions", schema = "pos_db")
public class Permission {
    @Id
    @Column(name = "permission_id", length = 36, columnDefinition = "CHAR(36)")
    private String permissionId;

    @Column(name = "permission_name", nullable = false, length = 100)
    private String permissionName;

    @Column(name = "permission_code", nullable = false, length = 50)
    private String permissionCode;

    @Column(name = "resource", nullable = false, length = 50)
    private String resource;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Getters and Setters
    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }

    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }

    public String getPermissionCode() { return permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
