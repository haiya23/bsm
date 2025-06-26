package com.haiya.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * ClassName: BatteryInfoDTO
 * Package: com.haiya.entity.dto
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 16:03
 * @ Version: 1.0
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatteryInfoDTO implements Serializable {
    private Integer carId;
    private Integer warnId;
    private String signal;
}
