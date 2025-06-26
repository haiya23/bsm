package com.haiya.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * ClassName: WarnMapper
 * Package: com.haiya.mapper
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 17:40
 * @ Version: 1.0
 */
@Mapper
public interface WarnMapper {
//    @Select("SELECT rule_description FROM bms_rules WHERE rule_id = #{ruleId} AND battery_type = #{batteryType}")
    @MapKey("rule_name")
    Map<String, Map<String, String>> getRulesMap(@Param("ruleId") Integer ruleId,
                          @Param("batteryType") String batteryType );

    List<String> getRulesByCarIdAndRuleId(@Param("ruleId") Integer ruleId, @Param("carId") Integer carId);


}
