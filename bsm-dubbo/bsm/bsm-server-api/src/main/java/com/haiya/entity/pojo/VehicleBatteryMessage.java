package com.haiya.entity.pojo;

import com.haiya.entity.dto.BatteryInfoDTO;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleBatteryMessage implements Serializable {
    private Vehicle vehicle;
    private BatteryInfoDTO battery;

}
