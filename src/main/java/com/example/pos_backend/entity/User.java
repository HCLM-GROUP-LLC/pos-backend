package com.example.pos_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "pos_db")
public class User {
    @Id
    @Column(name = "user_id", length = 36, columnDefinition = "CHAR(36)")
    private String userId;

    @Column(name = "merchant_id", nullable = false, length = 36, columnDefinition = "CHAR(36)")
    private String merchantId;

    @Column(name = "store_id", nullable = false, length = 36, columnDefinition = "CHAR(36)")
    private String storeId;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "VARCHAR(255)")
    private String passwordHash;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "role_id", columnDefinition = "CHAR(36)")
    private Role role;

    @Column(name = "status", nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'")
    private String status;

    @Column(name = "salary", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal salary;

    @Column(name = "hire_date", columnDefinition = "DATE")
    private LocalDate hireDate;

    @Column(name = "last_login_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLoginAt;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}