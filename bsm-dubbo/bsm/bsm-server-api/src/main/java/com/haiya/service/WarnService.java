package com.haiya.service;

import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.vo.WarmLevelVO;

import java.util.List;

/**
 * ClassName: WarnService
 * Package: com.haiya.service
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 17:39
 * @ Version: 1.0
 */
public interface WarnService {
    List<WarmLevelVO> getWarmLevel(List<BatteryInfoDTO> batteryInfoDTOS);
}
