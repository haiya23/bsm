package com.haiya;

import com.haiya.service.TimingTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * ClassName: GenerateTest
 * Package: com.haiya
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 14:20
 * @ Version: 1.0
 */
@SpringBootTest
public class GenerateTest {
    @Resource
    private TimingTaskService timingTaskService;
    @Test
    public void testGenerateVehicleAndBatteryInfo(){

//        String s = timingTaskService.generateVehicleAndBatteryInfo();
//        System.out.println(s);
    }
}
