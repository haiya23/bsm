package com.haiya.service.impl;

import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.pojo.Vehicle;
import com.haiya.entity.pojo.VehicleBatteryMessage;
import com.haiya.entity.vo.WarmLevelVO;
import com.haiya.mapper.VehicleMapper;
import com.haiya.service.WarnService;
import com.haiya.service.WarnWithVehicleINFService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: WarnWithVehicleINFServiceImpl
 * Package: com.haiya.service.impl
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 16:16
 * @ Version: 1.0
 */
@DubboService
@Service
public class WarnWithVehicleINFServiceImpl implements WarnWithVehicleINFService {
    @Resource
    private WarnService warnService;
    @Resource
    private VehicleMapper vehicleMapper;

    @Override
    public List<WarmLevelVO> getWarmLevelWithVehicleINF(List<VehicleBatteryMessage> vehicleBatteryMessages) {
        List<Vehicle> VEHICLES = new ArrayList<>();
        List<BatteryInfoDTO> BATTERIES = new ArrayList<>();
        for (VehicleBatteryMessage vehicleBatteryMessage : vehicleBatteryMessages) {
            if (vehicleBatteryMessage != null){
                VEHICLES.add(vehicleBatteryMessage.getVehicle());
                BATTERIES.add(vehicleBatteryMessage.getBattery());
            }
        }
        //1.车辆添加到数据库中
        for (Vehicle vehicle : VEHICLES) {
            //todo:添加布隆过滤器，判断是否在数据库中，在执行插入
            // 设置创建时间和更新时间
            vehicle.setCreateTime(new Date(System.currentTimeMillis()));
            vehicle.setUpdateTime(new Date(System.currentTimeMillis()));
            //插入数据库
            vehicleMapper.insert(vehicle);
        }
//        vehicleMapper.insetBatch(vehicles);
        //4.获取报警等级并返回返回数据
        return warnService.getWarmLevel(BATTERIES);
    }
}
