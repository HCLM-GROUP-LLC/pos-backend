package com.hclm.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 商户应用启动类
 *
 * @author hanhua
 * @since 2025/10/06
 */
@SpringBootApplication
public class MerchantApplication {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(MerchantApplication.class, args);
        System.out.println("启动耗时:" + String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0) + "s");
    }
}
