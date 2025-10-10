package com.hclm.terminal;

import cn.dev33.satoken.stp.StpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:mysql://192.168.3.2:3306/pos_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
        "spring.datasource.username=root",
        "spring.datasource.password=54hanhua@yw.cn",
        "spring.flyway.enabled=false",
        "spring.data.redis.host=192.168.3.2"
})
public class MyTest {
    public static void main(String[] args) {
        Instant instant = Instant.now();
        String formatted = instant.atZone(ZoneId.of("Asia/Shanghai"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(formatted);
    }

    @Test
    public void logout() {
        StpUtil.kickout("USR-174000000001");
    }
}
