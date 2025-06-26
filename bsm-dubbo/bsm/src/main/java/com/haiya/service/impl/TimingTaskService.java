package com.haiya.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.pojo.Vehicle;
import com.haiya.entity.pojo.VehicleBatteryMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TimingTaskService {

    static final String TOPIC = "Battery-INF";
    private static final int THREAD_COUNT = 4; // 线程数量
    private static final int MESSAGES_PER_CALL = 10; // 每次生成的消息条数
    private static final AtomicInteger vinCounter = new AtomicInteger(20); // VIN起始值
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private static final ObjectMapper objectMapper = new ObjectMapper();



    public List<VehicleBatteryMessage> sendBatteryInfoMessage() throws InterruptedException, JsonProcessingException, ExecutionException {
        List<VehicleBatteryMessage> messages = new ArrayList<>();
        List<Future<VehicleBatteryMessage>> futures = new ArrayList<>();
        for (int i = 0; i < MESSAGES_PER_CALL; i++) {
            futures.add(executorService.submit(TimingTaskService::generateVehicleAndBatteryInfo));
        }

        for (Future<VehicleBatteryMessage> future : futures) {

            VehicleBatteryMessage messageBody = future.get(); // 获取线程结果
            messages.add(messageBody);

        }
        return messages;
    }


    /**
     * 随机生成车辆和电池信息，并转换为 JSON 格式
     */
    public static VehicleBatteryMessage generateVehicleAndBatteryInfo() {
        // 生成VIN（int）和VID（String）
        int vin = vinCounter.getAndIncrement();
        String vid = generateUnique16BitString();

        // 构造 Vehicle 和 BatteryInfoDTO 对象
        Vehicle vehicle = new Vehicle();
        vehicle.setVid(vid);
        vehicle.setVin(String.valueOf(vin));
        vehicle.setBatteryType(Math.random() < 0.5 ? "三元电池" : "铁锂电池");
        vehicle.setTotalMileage(Math.round(Math.random() * 10000 * 100) / 100.0); // 随机里程 要求保留两位小数
        vehicle.setBatteryHealth(Math.round((70 + Math.random() * 30) * 100) / 100.0);  // 随机健康度 要求保留两位小数

        BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO();
        batteryInfoDTO.setCarId( vin); //
        // 随机状态 1 或 2 或 null共三种状态
        Integer warnId = Math.random() < 0.5 ? Integer.valueOf(1) : Math.random() < 0.5 ? 2 : null;
        batteryInfoDTO.setWarnId(warnId);

        batteryInfoDTO.setSignal(generateSignal(warnId)); // 随机信号
        // 构建组合对象
        return new VehicleBatteryMessage(vehicle, batteryInfoDTO);
    }


    /**
     * 将车辆与电池信息组合成JSON
     */
    private static String toJson(Vehicle vehicle, BatteryInfoDTO batteryInfoDTO) {
        try {
            return "{\"vehicle\":" + objectMapper.writeValueAsString(vehicle) + ",\"battery\":" + objectMapper.writeValueAsString(batteryInfoDTO) + "}";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    /**
     * 解析JSON字符串为Vehicle和BatteryInfoDTO对象
     */
    public static List<Vehicle> parseVehiclesFromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Vehicle>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析JSON字符串为BatteryInfoDTO对象列表
     */
    public static List<BatteryInfoDTO> parseBatteriesFromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<BatteryInfoDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成唯一16位随机字符串
     */
    private static String generateUnique16BitString() {
        StringBuilder sb = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 16; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private static String generateSignal(Integer warnId) {
        // 随机生成浮点数值
        double mx = Math.round((5.0 + Math.random() * 10) * 100) / 100.0; // 5.0 ~ 15.0
        double mi = Math.round((0.0 + Math.random() * 5)  * 100) / 100.0; // 0.0 ~ 5.0
        double ix = Math.round((10.0 + Math.random() * 5) * 100) / 100.0; // 10.0 ~ 15.0
        double ii = Math.round((0.0 + Math.random() * 12)  * 100) / 100.0; // 0.0 ~ 12.0

        // 定义三种信号模板（使用 %f 作为浮点数占位符）
        String template1 = "{\"Mx\":%f,\"Mi\":%f}";
        String template2 = "{\"Ix\":%f,\"Ii\":%f}";
        String template3 = "{\"Mx\":%f,\"Mi\":%f,\"Ix\":%f,\"Ii\":%f}";

        if (warnId == null) {
            return String.format(template3, mx, mi, ix, ii);
        }

        switch (warnId) {
            case 1:
                return String.format(template1, mx, mi);  // 状态1 电压
            case 2:
                return String.format(template2, ix, ii);  // 状态2 电流
            default:
                return "";
        }
    }
}