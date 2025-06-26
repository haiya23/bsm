package com.haiya.controller;

import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.vo.Result;
import com.haiya.entity.vo.WarmLevelVO;
import com.haiya.service.WarnService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: WarnController
 * Package: com.haiya.controller
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 16:45
 * @ Version: 1.0
 */
@RestController
@RequestMapping("/api/warn")
public class WarnController {
    @Resource
    private WarnService warnService;
    @PostMapping
    public Result<WarmLevelVO> getWarmLevel(@RequestBody List<BatteryInfoDTO >batteryInfoDTOS) {
        List<WarmLevelVO> warmLevel = warnService.getWarmLevel(batteryInfoDTOS);
        System.out.println(batteryInfoDTOS.toString());
        return Result.success(warmLevel);
    }
}
