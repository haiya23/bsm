
CREATE TABLE `bms_rules` (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '序号',
                             `rule_id` int NOT NULL COMMENT '规则编号',
                             `rule_name` varchar(50) NOT NULL COMMENT '规则名称',
                             `battery_type` enum('三元电池','铁锂电池') NOT NULL COMMENT '电池类型',
                             `rule_description` json NOT NULL COMMENT '规则描述(JSON格式)',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_rule_battery` (`rule_id`, `battery_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BMS预警规则表';

-- 三元电池电压差报警规则
INSERT INTO `bms_rules`
(`rule_id`, `rule_name`, `battery_type`, `rule_description`)
VALUES
    (1, '电压差报警', '三元电池', '[
      {"max": null, "min": 5, "alert_level": 0},
      {"max": 5, "min": 3, "alert_level": 1},
      {"max": 3, "min": 1, "alert_level": 2},
      {"max": 1, "min": 0.6, "alert_level": 3},
      {"max": 0.6, "min": 0.2, "alert_level": 4},
      {"max": 0.2, "min": null, "alert_level": -1}
    ]');

-- 铁锂电池电压差报警规则
INSERT INTO `bms_rules`
(`rule_id`, `rule_name`, `battery_type`, `rule_description`)
VALUES
    (1, '电压差报警', '铁锂电池', '[
      {"max": null, "min": 2, "alert_level": 0},
      {"max": 2, "min": 1, "alert_level": 1},
      {"max": 1, "min": 0.7, "alert_level": 2},
      {"max": 0.7, "min": 0.4, "alert_level": 3},
      {"max": 0.4, "min": 0.2, "alert_level": 4},
      {"max": 0.2, "min": null, "alert_level": -1}
    ]');

-- 三元电池电流差报警规则
INSERT INTO `bms_rules`
(`rule_id`, `rule_name`, `battery_type`, `rule_description`)
VALUES
    (2, '电流差报警', '三元电池', '[
      {"max": null, "min": 3, "alert_level": 0},
      {"max": 3, "min": 1, "alert_level": 1},
      {"max": 1, "min": 0.2, "alert_level": 2},
      {"max": 0.2, "min": null, "alert_level": -1}
    ]');

-- 铁锂电池电流差报警规则
INSERT INTO `bms_rules`
(`rule_id`, `rule_name`, `battery_type`, `rule_description`)
VALUES
    (2, '电流差报警', '铁锂电池', '[
      {"max": null, "min": 1, "alert_level": 0},
      {"max": 1, "min": 0.5, "alert_level": 1},
      {"max": 0.5, "min": 0.2, "alert_level": 2},
      {"max": 0.2, "min": null, "alert_level": -1}
    ]');


drop table if exists vehicle_info;
CREATE TABLE `vehicle_info` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `vid` varchar(16) NOT NULL COMMENT '车辆识别码',
                                `vin` int NOT NULL COMMENT '车架编号',
                                `battery_type` enum('三元电池','铁锂电池') NOT NULL COMMENT '电池类型',
                                `total_mileage` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总里程(km)',
                                `battery_health` decimal(5,2) NOT NULL DEFAULT '100.00' COMMENT '电池健康状态(%)',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_vid` (`vid`),
                                UNIQUE KEY `uk_vin` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆信息表';

-- 插入第一条记录: 车架编号1, 三元电池, 100km, 100%
INSERT INTO `vehicle_info`
(`vid`, `vin`, `battery_type`, `total_mileage`, `battery_health`)
VALUES
    ('XK8J7H5G2F3D1C9A', 1, '三元电池', 100.00, 100.00);

-- 插入第二条记录: 车架编号2, 铁锂电池, 600km, 95%
INSERT INTO `vehicle_info`
(`vid`, `vin`, `battery_type`, `total_mileage`, `battery_health`)
VALUES
    ('B2N4M6Q8P0R1S3T5', 2, '铁锂电池', 600.00, 95.00);

-- 插入第三条记录: 车架编号3, 三元电池, 300km, 98%
INSERT INTO `vehicle_info`
(`vid`, `vin`, `battery_type`, `total_mileage`, `battery_health`)
VALUES
    ('Z9Y7X5W3V1U0T2S4', 3, '三元电池', 300.00, 98.00);