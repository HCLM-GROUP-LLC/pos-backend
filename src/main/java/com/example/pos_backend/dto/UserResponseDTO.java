package com.example.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户响应 DTO
 * 用于返回用户信息，不包含敏感数据如密码哈希
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roleId;
    private String roleName;
    private String status;
    private BigDecimal salary;
    private LocalDate hireDate;
    private LocalDateTime lastLoginAt;

    // Lombok @Data annotation automatically generates getters, setters, equals, hashCode, and toString
}