package com.haiya;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.pojo.Vehicle;
import com.haiya.entity.pojo.VehicleBatteryMessage;
import com.haiya.entity.vo.WarmLevelVO;
import com.haiya.service.WarnService;
import com.haiya.service.WarnWithVehicleINFService;
import com.haiya.service.impl.TimingTaskService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * ClassName: CombineTest
 * Package: com.haiya
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 17:26
 * @ Version: 1.0
 */
@SpringBootTest
public class CombineTest {

////    @Resource
////    private WarnWithVehicleINFService warnWithVehicleINFService;
//    @Resource
//    private WarnService warnService;
//    @Resource
//    private TimingTaskService timingTaskService;
//    @Test
//    public void test1() throws JsonProcessingException, ExecutionException, InterruptedException {
//
//        List<VehicleBatteryMessage> vehicleBatteryMessages = timingTaskService.sendBatteryInfoMessage();
//        List<BatteryInfoDTO>  batteryInfoDTOS = new ArrayList<>();
//        for(VehicleBatteryMessage vehicleBatteryMessage:vehicleBatteryMessages){
//            batteryInfoDTOS.add(vehicleBatteryMessage.getBattery());
//        }
//        List<WarmLevelVO> warmLevel = warnService.getWarmLevel(batteryInfoDTOS);
//        System.out.println(warmLevel);
//    }
}
