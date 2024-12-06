package com.example.music_server.controller;


import com.example.music_server.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final WebSocketMessageService webSocketMessageService;

    @Autowired
    public ChatController(WebSocketMessageService webSocketMessageService) {
        this.webSocketMessageService = webSocketMessageService;
    }

    // 通过 HTTP 请求发送消息到所有 WebSocket 客户端
    @PostMapping("/broadcast")
    public String broadcastMessage(@RequestBody String message) {
        // 这里你可以获取 WebSocket 的所有会话，执行消息广播
        // 假设已经有 sessions 列表或其他机制来存储会话
        // webSocketMessageService.broadcastMessage(sessions, message);
        return "Message broadcasted: " + message;
    }
}
