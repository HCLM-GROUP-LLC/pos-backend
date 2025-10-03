package com.example.pos_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.pos_backend.dto.UserRequestDTO;
import com.example.pos_backend.dto.UserResponseDTO;
import com.example.pos_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SaCheckLogin //登录校验
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired 
    private UserService userService;

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/{userId}")
    public UserResponseDTO updateUser(@PathVariable String userId, @RequestBody UserRequestDTO dto) {
        return userService.updateUser(userId, dto);
    }

    @GetMapping
    public List<UserResponseDTO> listUsers() {
        return userService.listUsers();
    }

    @GetMapping("/{userId}")
    public UserResponseDTO getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }
}

