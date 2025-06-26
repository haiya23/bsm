package com.haiya;

import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.service.WarnService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: WarnServiceTest
 * Package: com.haiya
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 2:01
 * @ Version: 1.0
 */
@SpringBootTest
public class WarnServiceTest {
    @Resource
    private WarnService warnService;
    @Test
    public void testAlertResolve(){
        BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO();
        batteryInfoDTO.setCarId(1);
        batteryInfoDTO.setWarnId(1);
        batteryInfoDTO.setSignal("{\"Mx\":12.0,\"Mi\":0.6}");
        List<BatteryInfoDTO> batteryInfoDTOS = new ArrayList<>();
        batteryInfoDTOS.add(batteryInfoDTO);
        warnService.getWarmLevel(batteryInfoDTOS);
    }
}
