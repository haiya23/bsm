package com.haiya.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.pojo.Vehicle;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TimingTaskService {

    private DefaultMQProducer producer;
    static final String TOPIC = "Battery-INF";
    private static final int THREAD_COUNT = 4; // 线程数量
    private static final int MESSAGES_PER_CALL = 10; // 每次生成的消息条数
    private static final AtomicInteger vinCounter = new AtomicInteger(4); // VIN起始值
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer("BsmBatteryInfoGroup");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
    }

    @Scheduled(fixedRate = 1000 * 10)
    public void sendBatteryInfoMessage() throws InterruptedException, JsonProcessingException {
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < MESSAGES_PER_CALL; i++) {
            futures.add(executorService.submit(TimingTaskService::generateVehicleAndBatteryInfo));
        }

        for (Future<String> future : futures) {
            try {
                String messageBody = future.get(); // 获取线程结果
                Message msg = new Message(TOPIC, messageBody.getBytes(StandardCharsets.UTF_8));
                producer.send(msg);
            } catch (ExecutionException | MQBrokerException | InterruptedException | MQClientException |
                     RemotingException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (producer != null) {
            producer.shutdown();
        }
        executorService.shutdown();
    }

    /**
     * 随机生成车辆和电池信息，并转换为 JSON 格式
     */
    public static String generateVehicleAndBatteryInfo() {
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
        return toJson(vehicle, batteryInfoDTO);
    }


    /**
     * 将车辆与电池信息组合成JSON
     */
    private static String toJson(Vehicle vehicle, BatteryInfoDTO batteryInfoDTO) {
        try {
            return "{"
                    + "\"vehicle\":" + objectMapper.writeValueAsString(vehicle) + ","
                    + "\"battery\":" + objectMapper.writeValueAsString(batteryInfoDTO)
                    + "}";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    // 雪花算法生成唯一ID
    private final long nodeId;
    private long lastTimestamp = -1L;
    private long lastSequence = 0L;

    private static final long NODE_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    public TimingTaskService() {
        this.nodeId = 1; // 可根据线程ID或其他标识设置不同nodeId
    }

    private synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨");
        }

        if (timestamp == lastTimestamp) {
            lastSequence = (lastSequence + 1) & MAX_SEQUENCE;
            if (lastSequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            lastSequence = 0;
        }

        lastTimestamp = timestamp;
        return (timestamp << (NODE_BITS + SEQUENCE_BITS)) 
               | (nodeId << SEQUENCE_BITS) 
               | lastSequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 生成唯一16位随机字符串
     */
    private static String generateUnique16BitString() {
        // 使用UUID前8位 + 时间戳后8位组合成16位字符串
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String timePart = Long.toHexString(System.currentTimeMillis()).substring(4);
        return uuidPart + timePart;
    }

    private static String encodeBase62(long num) {
        StringBuilder sb = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        do {
            sb.insert(0, chars.charAt((int)(num % chars.length())));
            num /= chars.length();
        } while (num > 0);
        // 补足16位长度，前面填充'A'作为占位符
        while (sb.length() < 16) {
            sb.insert(0, 'A');
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