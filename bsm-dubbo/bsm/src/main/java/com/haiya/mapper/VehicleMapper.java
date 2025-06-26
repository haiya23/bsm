package com.haiya.mapper;

import com.haiya.entity.pojo.Vehicle;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: VehicleMapper
 * Package: com.haiya.mapper
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 19:20
 * @ Version: 1.0
 */
@Mapper
public interface VehicleMapper {
    @Select("select * from vehicle_info where vin = #{carId}")
    Vehicle getById(Integer carId);

    @Insert("insert into vehicle_info(vid, vin, battery_type, total_mileage, battery_health, create_time,update_time) values" +
            "(#{vid}, #{vin}, #{batteryType}, #{totalMileage}, #{batteryHealth}, #{createTime}, #{updateTime})")
    void insert(Vehicle vehicle);
}
