package com.haiya.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.pojo.Alert;
import com.haiya.entity.pojo.Vehicle;
import com.haiya.entity.vo.WarmLevelVO;
import com.haiya.mapper.VehicleMapper;
import com.haiya.mapper.WarnMapper;
import com.haiya.service.WarnService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * ClassName: WarnServiceImpl
 * Package: com.haiya.service.impl
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 17:40
 * @ Version: 1.0
 */
//@Service
@DubboService
public class WarnServiceImpl implements WarnService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private WarnMapper warnMapper;
    @Resource
    private VehicleMapper vehicleMapper;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String RULE_KEY_PREFIX = "warn_rules:";
    private static final String VEHICLE_KEY_PREFIX = "vehicle:";
    @Override
    public List<WarmLevelVO> getWarmLevel(List<BatteryInfoDTO> batteryInfoDTOS) {
        List<WarmLevelVO> warmLevelVOS = new ArrayList<>();
        for (BatteryInfoDTO batteryInfoDTO : batteryInfoDTOS){
            if (batteryInfoDTO != null) {
                // 获取车辆信息
                String vehicleKey = VEHICLE_KEY_PREFIX + batteryInfoDTO.getCarId();
                Vehicle vehicle = (Vehicle) redisTemplate.opsForValue().get(vehicleKey);
                if (vehicle == null) {
                    vehicle = vehicleMapper.getById(batteryInfoDTO.getCarId());
                    redisTemplate.opsForValue().set(vehicleKey, vehicle);
                }

                // 获取报警规则
                String ruleKey = RULE_KEY_PREFIX + batteryInfoDTO.getWarnId() + ":" + vehicle.getBatteryType();
                // 先从 Redis 获取规则
                Map<String, Map<String, String>> rules = (Map<String, Map<String, String>>) redisTemplate.opsForValue().get(ruleKey);

                if (rules == null) {
                    // 如果 Redis 中没有，则从数据库获取
                    rules = warnMapper.getRulesMap(batteryInfoDTO.getWarnId(), vehicle.getBatteryType());

                    // 写入 Redis（设置合理的过期时间，例如 5 分钟）
                    redisTemplate.opsForValue().set(ruleKey, rules);
                }
                // 根据规则获取报警级别
                Map<String, String> warmLevel = getLevel(batteryInfoDTO.getSignal(), rules);

                for (String key : warmLevel.keySet()) {

                    warmLevelVOS.add(new WarmLevelVO(
                            batteryInfoDTO.getCarId(),
                            vehicle.getBatteryType(),
                            key,
                            warmLevel.get(key))
                    );
                }
            }

        }

        return warmLevelVOS;
    }

    private static Map<String, String> getLevel(String signal, Map<String, Map<String, String>> rules) {
        Map<String, String> warmLevel = new HashMap<>();
        Map<String, Double> map = null;

        try {
            map = objectMapper.readValue(signal, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (map.containsKey("Mx")) {
            processRule(map, rules, warmLevel, "电压差报警", "Mx", "Mi");
        }

        if (map.containsKey("Ix")) {
            processRule(map, rules, warmLevel, "电流差报警", "Ix", "Ii");
        }

        return warmLevel;
    }

    private static void processRule(Map<String, Double> map, Map<String, Map<String, String>> rules,
                                    Map<String, String> warmLevel, String ruleName, String maxKey, String minKey) {
        String ruleDescription = rules.get(ruleName).get("rule_description");
        List<Alert> alerts = null;

        try {
            alerts = objectMapper.readValue(ruleDescription, new TypeReference<List<Alert>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        double diff = map.get(maxKey) - map.get(minKey);
        if (diff > alerts.get(0).getMin()) {
            warmLevel.put(ruleName, String.valueOf(alerts.get(0).getAlert_level()));
        } else {
            for (int i = 1; i < alerts.size() - 1; i++) {
                if (diff > alerts.get(i).getMin() && diff < alerts.get(i).getMax()) {
                    warmLevel.put(ruleName, String.valueOf(alerts.get(i).getAlert_level()));
                    return;
                }
            }
            if (diff < alerts.get(alerts.size() - 1).getMax()) {
                warmLevel.put(ruleName, "不报警");
            }
        }
    }
}
