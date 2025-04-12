package com.example.music_server.controller;


import com.example.music_server.common.Result;
import com.example.music_server.entity.ChatMessage;
import com.example.music_server.mapper.ChatMessageMapper;
import com.example.music_server.mapper.UserMapper;
import com.example.music_server.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final WebSocketMessageService webSocketMessageService;

    @Autowired
    public ChatController(WebSocketMessageService webSocketMessageService) {
        this.webSocketMessageService = webSocketMessageService;
    }
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    // 通过 HTTP 请求发送消息到所有 WebSocket 客户端
    @PostMapping("/broadcast")
    public String broadcastMessage(@RequestBody String message) {
        // 这里你可以获取 WebSocket 的所有会话，执行消息广播
        // 假设已经有 sessions 列表或其他机制来存储会话
        // webSocketMessageService.broadcastMessage(sessions, message);
        return "Message broadcasted: " + message;
    }
    @GetMapping("/history")
    public Result getChatHistory(@RequestParam("user") String user,
                                 @RequestParam("with") String withUser) {
        System.out.println(chatMessageMapper.getChatHistoryBetween(user, withUser));
        List<ChatMessage> chatMessages = chatMessageMapper.getChatHistoryBetween(user, withUser);
        return Result.success(chatMessages);
    }
}
