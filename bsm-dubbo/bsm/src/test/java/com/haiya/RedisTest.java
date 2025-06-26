package com.haiya;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * ClassName: RedisTest
 * Package: com.haiya
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 3:34
 * @ Version: 1.0
 */
@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;
    
    @Test
    public void test1(){
        redisTemplate.opsForValue().set("name","jack");
    }
}
