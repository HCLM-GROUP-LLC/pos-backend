package com.example.pos_backend.repository;

import com.example.pos_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRoleCode(String roleCode);
}