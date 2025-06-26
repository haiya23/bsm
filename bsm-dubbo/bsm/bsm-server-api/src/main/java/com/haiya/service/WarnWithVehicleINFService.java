package com.haiya.service;

import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.pojo.Vehicle;
import com.haiya.entity.pojo.VehicleBatteryMessage;
import com.haiya.entity.vo.WarmLevelVO;

import java.util.List;

/**
 * ClassName: WarnWithVehicleINFService
 * Package: com.haiya.service
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 16:15
 * @ Version: 1.0
 */
public interface WarnWithVehicleINFService {
    List<WarmLevelVO> getWarmLevelWithVehicleINF(List<VehicleBatteryMessage> vehicleBatteryMessages);
}
