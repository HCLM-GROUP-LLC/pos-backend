package com.hclm.web.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具
 *
 * @author hanhua
 * @since 2025/10/06
 */
public class PwdUtil {
    private static PasswordEncoder passwordEncoder;

    public PwdUtil(PasswordEncoder passwordEncoder) {
        PwdUtil.passwordEncoder = passwordEncoder;
    }

    /**
     * 编码
     *
     * @param password 密码
     * @return {@link String }
     */
    public static String encode(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 验证
     *
     * @param rawPassword     原密码
     * @param encodedPassword 编码密码
     * @return boolean
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
