package com.example.music_server.service;


import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class WebSocketMessageService {

    // 广播消息给所有连接的用户
    public void broadcastMessage(List<WebSocketSession> sessions, String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    // 给指定用户发送消息
    public void sendMessageToUser(WebSocketSession session, String message) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
