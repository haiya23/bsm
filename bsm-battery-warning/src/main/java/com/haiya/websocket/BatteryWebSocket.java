package com.haiya.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 服务类，用于向前端推送实时数据
 */
@Component
@ServerEndpoint("/ws/battery")
public class BatteryWebSocket {

    // 存储所有连接的客户端会话
    private static final CopyOnWriteArraySet<BatteryWebSocket> sessions = new CopyOnWriteArraySet<>();

    // 当前会话
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        sessions.add(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 可选：接收前端的消息
    }

    /**
     * 向所有连接的客户端广播消息
     * @param data 要发送的数据
     */
    public static void sendMessageToAll(String data) {
        for (BatteryWebSocket client : sessions) {
            try {
                if (client.session.isOpen()) {
                    client.session.getBasicRemote().sendText(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addDataAndNotifyFrontend(String jsonData) {
        sendMessageToAll(jsonData);
    }
}