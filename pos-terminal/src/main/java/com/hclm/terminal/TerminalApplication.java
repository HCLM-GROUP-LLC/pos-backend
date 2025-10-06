package com.hclm.terminal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TerminalApplication {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(TerminalApplication.class, args);
        System.out.println("员工端启动耗时:" + String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0) + "s");
    }
}
