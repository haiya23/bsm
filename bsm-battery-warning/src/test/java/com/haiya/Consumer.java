package com.haiya;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiya.entity.pojo.VehicleBatteryMessage;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * ClassName: Consumer
 * Package: com.haiya
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/26 - 14:37
 * @ Version: 1.0
 */
@SpringBootTest
public class Consumer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws MQClientException {
        List<VehicleBatteryMessage> info = new ArrayList<>();
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                info.add(received);
//                System.out.printf("收到消息：%s%n", messageBody);
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
            if(!info.isEmpty()){

                System.out.println(info);
                info.clear();
            }
        }
    }
}
