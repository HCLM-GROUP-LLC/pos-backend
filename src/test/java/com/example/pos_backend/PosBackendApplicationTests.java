package com.example.pos_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class PosBackendApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    public void passwordEncoder() {
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println();
    }
}
