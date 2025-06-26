package com.haiya;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ClassName: BsmBatteryWarningApplication
 * Package: com.haiya
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 13:12
 * @ Version: 1.0
 */
@SpringBootApplication
@EnableDubbo
@EnableScheduling
public class BsmBatteryWarningApplication {
    public static void main(String[] args) {
        SpringApplication.run(BsmBatteryWarningApplication.class, args);
    }
}
