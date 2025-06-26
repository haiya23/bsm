package com.haiya.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiya.entity.dto.BatteryInfoDTO;
import com.haiya.entity.pojo.Vehicle;
import com.haiya.entity.pojo.VehicleBatteryMessage;
import com.haiya.entity.vo.WarmLevelVO;
import com.haiya.service.WarnService;
import com.haiya.service.WarnWithVehicleINFService;
import com.haiya.websocket.BatteryWebSocket;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: WaringController
 * Package: com.haiya.controller
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 14:50
 * @ Version: 1.0
 */
@RestController
@RequestMapping("/waring")
public class WaringController {
    @DubboReference
    private WarnWithVehicleINFService warnWithVehicleINFService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static final List<VehicleBatteryMessage> INF = new ArrayList<>();
    @GetMapping("/getWaring")
    public String getWaring() throws MQClientException {
        // 1. 创建消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("BsmBatteryInfoGroup");

        // 2. 设置 Name Server 地址
        consumer.setNamesrvAddr("localhost:9876");

        // 3. 订阅 Topic 及 Tag（* 表示订阅所有标签）
        consumer.subscribe("Battery-INF", "*");


        // 4. 注册消息监听器
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            VehicleBatteryMessage received = null;
            for (MessageExt msg : msgs) {
                // 直接返回原始消息内容
                try {
                    received = objectMapper.readValue(msg.getBody(), VehicleBatteryMessage.class);
                    INF.add(received);
                    // 新增：将收到的数据转换为 JSON 并发送给前端
                    BatteryWebSocket.addDataAndNotifyFrontend(objectMapper.writeValueAsString(received));
                } catch (Exception e) {
                    throw new RuntimeException("Error processing message", e);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // 5. 启动消费者
        consumer.start();

        System.out.println("消费者已启动，等待接收消息...");
        while ( true){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!INF.isEmpty()){
                //TODO:调用接口处理数据

                List<WarmLevelVO> warmLevelWithVehicleINF = warnWithVehicleINFService.getWarmLevelWithVehicleINF(INF);
                System.out.println(warmLevelWithVehicleINF);
                //todo: 通过socket发送数据给前端
                INF.clear();
            }
        }
    }
}
