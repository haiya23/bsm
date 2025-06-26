package com.haiya.entity.vo;

import lombok.*;

import java.io.Serializable;

/**
 * ClassName: WarmLevelVO
 * Package: com.haiya.entity.vo
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 16:14
 * @ Version: 1.0
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarmLevelVO implements Serializable {
    private Integer carId;
    private String batteryType;
    private String warnName;
    private String warnLevel;
}
