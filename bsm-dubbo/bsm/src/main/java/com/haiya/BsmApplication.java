package com.haiya;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.haiya.mapper")
public class BsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BsmApplication.class, args);
    }

}
