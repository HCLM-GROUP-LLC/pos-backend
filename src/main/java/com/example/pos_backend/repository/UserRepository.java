package com.example.pos_backend.repository;

import com.example.pos_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByStoreIdAndIsDeletedFalse(String storeId);
    List<User> findByMerchantIdAndIsDeletedFalse(String merchantId);
    List<User> findByIsDeletedFalse();
}
