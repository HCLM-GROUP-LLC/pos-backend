package com.example.pos_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户请求 DTO
 * 用于接收创建和更新用户的请求数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    private String passwordHash; // 创建时必填，更新时可选
    
    private String firstName;
    private String lastName;
    
    private String roleId;
    
    private BigDecimal salary;
    
    // Fields for user creation - will be validated in service layer
    private String merchantId;
    
    private String storeId;
}

