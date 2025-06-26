package com.haiya.entity.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: Vehicle
 * Package: com.haiya.entity.pojo
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 15:58
 * @ Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vehicle implements Serializable {
    private Long id;
    private String vid;
    private String vin;
    private String batteryType;
    private Double totalMileage;
    private Double batteryHealth;
    private Date createTime;
    private Date updateTime;
}
