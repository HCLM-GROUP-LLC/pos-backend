package com.example.pos_backend.service;

import com.example.pos_backend.dto.UserRequestDTO;
import com.example.pos_backend.dto.UserResponseDTO;
import com.example.pos_backend.entity.User;
import com.example.pos_backend.repository.RoleRepository;
import com.example.pos_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        // Validate required fields for creation
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required for user creation");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required for user creation");
        }
        if (dto.getPasswordHash() == null || dto.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required for user creation");
        }
        if (dto.getRoleId() == null || dto.getRoleId().trim().isEmpty()) {
            throw new IllegalArgumentException("Role ID is required for user creation");
        }
        if (dto.getMerchantId() == null || dto.getMerchantId().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant ID is required for user creation");
        }
        if (dto.getStoreId() == null || dto.getStoreId().trim().isEmpty()) {
            throw new IllegalArgumentException("Store ID is required for user creation");
        }
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setMerchantId(dto.getMerchantId());
        user.setStoreId(dto.getStoreId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPasswordHash());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(roleRepository.findById(dto.getRoleId()).orElseThrow());
        user.setStatus("ACTIVE");
        user.setSalary(dto.getSalary());
        user.setHireDate(LocalDate.now());
        user.setIsDeleted(false);
        return toResponseDTO(userRepository.save(user));
    }

    public List<UserResponseDTO> listUsers() {
        return userRepository.findByIsDeletedFalse().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public UserResponseDTO getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found with id: " + userId)
        );
        return toResponseDTO(user);
    }

    public UserResponseDTO updateUser(String userId, UserRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found with id: " + userId)
        );
        
        // Update fields if provided
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPasswordHash() != null && !dto.getPasswordHash().isEmpty()) {
            user.setPasswordHash(dto.getPasswordHash());
        }
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getSalary() != null) {
            user.setSalary(dto.getSalary());
        }
        if (dto.getRoleId() != null) {
            user.setRole(roleRepository.findById(dto.getRoleId()).orElseThrow(
                () -> new RuntimeException("Role not found with id: " + dto.getRoleId())
            ));
        }
        
        return toResponseDTO(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found with id: " + userId)
        );
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || user.getIsDeleted()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return toResponseDTO(user);
    }

    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null || user.getIsDeleted()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return toResponseDTO(user);
    }

    public List<UserResponseDTO> findByStoreId(String storeId) {
        return userRepository.findByStoreIdAndIsDeletedFalse(storeId)
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<UserResponseDTO> findByMerchantId(String merchantId) {
        return userRepository.findByMerchantIdAndIsDeletedFalse(merchantId)
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        if(user.getRole()!=null){
            dto.setRoleId(user.getRole().getRoleId());
            dto.setRoleName(user.getRole().getRoleName());
        }
        dto.setStatus(user.getStatus());
        dto.setSalary(user.getSalary());
        dto.setHireDate(user.getHireDate());
        dto.setLastLoginAt(user.getLastLoginAt());
        return dto;
    }
}

