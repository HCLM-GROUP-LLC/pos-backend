package com.example.pos_backend.common;

import org.springframework.lang.NonNull;

import java.security.SecureRandom;

/**
 * 随机工具
 *
 * @author hanhua
 * @since 2025/10/04
 */
public class RandomUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    /**
     * 大写字母
     */
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 小写字母
     */
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    /**
     * 编号
     */
    private static final String NUMBERS = "0123456789";

    /**
     * Square风格
     *
     * @param prefix 前缀
     * @return {@link String }
     */
    public static String squareStyle(@NonNull String prefix) {
        long timestamp = System.currentTimeMillis();
        int randomNum = RANDOM.nextInt(1000);
        return String.format(prefix + "-%d-%03d", timestamp, randomNum);
    }

    /**
     * 获取随机字符串
     *
     * @param length     长度
     * @param characters 字符
     * @return {@link String }
     */
    public static String getRandomString(int length, String characters) {
        StringBuilder sb = new StringBuilder(length);
        int charactersLength = characters.length();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(RANDOM.nextInt(charactersLength)));
        }
        return sb.toString();
    }

    /**
     * 大写字母和数字组成的随机字符串
     *
     * @param length 长度
     * @return {@link String }
     */
    public static String upperAndNumbers(int length) {
        return getRandomString(length, UPPER_CASE_LETTERS + NUMBERS);
    }

    /**
     * 小写字母和数字组成的随机字符串
     *
     * @param length 长度
     * @return {@link String }
     */
    public static String lowerAndNumbers(int length) {
        return getRandomString(length, LOWER_CASE_LETTERS + NUMBERS);
    }

    /**
     * 字母和数字组成的随机字符串
     *
     * @param length 长度
     * @return {@link String }
     */
    public static String lettersAndNumbers(int length) {
        return getRandomString(length, UPPER_CASE_LETTERS + LOWER_CASE_LETTERS + NUMBERS);
    }
}
