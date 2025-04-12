package com.example.music_server.component;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.music_server.entity.ChatMessage;
import com.example.music_server.entity.User;
import com.example.music_server.mapper.ChatMessageMapper;
import com.example.music_server.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/imserver/{username}")
@Component
public class WebSocketServer {

    private static ChatMessageMapper chatMessageMapper;
    private static UserMapper userMapper;


    @Autowired
    public void setChatMessageMapper(ChatMessageMapper chatMessageMapper) {
        WebSocketServer.chatMessageMapper = chatMessageMapper;
    }
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }


    /**
     * 记录当前在线的连接数
     */
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessionMap.put(username, session);
        log.info("用户 [{}] 上线，当前在线人数: {}", username, sessionMap.size());
//        userMapper.setUserOnline(username, true); // 标记用户在线

        // 发送所有在线和离线用户列表
        sendAllUsers();
        // 发送未读消息
        List<ChatMessage> unreadMessages = chatMessageMapper.getUnreadMessages(username);
        for (ChatMessage message : unreadMessages) {
            sendMessage(JSONUtil.toJsonStr(message), session);
        }

        // 上线后，将未读消息标记为已读
        for (ChatMessage message : unreadMessages) {
            chatMessageMapper.markMessagesAsRead(username, message.getFromUser());
        }


    }
    // 发送所有用户信息（包括离线和在线）
    private void sendAllUsers() {
        List<User> users = userMapper.getAllUsers();
        for (User user : users) {
            user.setStatus(sessionMap.containsKey(user.getUsername()));
        }
        JSONObject result = new JSONObject();
        result.set("users", users);
        sendAllMessage(JSONUtil.toJsonStr(result));
    }
//    @OnOpen
//    public void onOpen(Session session, @PathParam("username") String username){
//        sessionMap.put(username,session);
//        log.info("有新用户加入，username={}.当前在线人数：{}",username,sessionMap.size());
//        JSONObject result = new JSONObject();
//        JSONArray array = new JSONArray();
//        result.set("users",array);
//        for(Object key : sessionMap.keySet()){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.set("username",key);
//            array.add(jsonObject);
//        }
//        sendAllMessage(JSONUtil.toJsonStr(result));
//    }

    /**
     * 连接关闭调用的
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username){
        sessionMap.remove(username);
        log.info("有一链接关闭，移除username={}的用户session,当前在线人数为：{}",username,sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        JSONObject obj = JSONUtil.parseObj(message);
        // 处理读回执消息
        if(obj.containsKey("type") && "readReceipt".equals(obj.getStr("type"))) {
            String fromUser = obj.getStr("from"); // 当前客户端已读对方消息
            String toUser = obj.getStr("to");
            if (toUser == null || toUser.trim().isEmpty()) {
                log.error("readReceipt 消息缺少 'to' 字段: {}", message);
                return;
            }
            // 更新数据库中两人间的消息为已读
            chatMessageMapper.markMessagesAsRead(username, toUser);
            // 通知对方更新消息状态
            Session senderSession = sessionMap.get(toUser);
            if(senderSession != null) {
                JSONObject readObj = new JSONObject();
                readObj.set("type", "readReceipt");
                readObj.set("from", username);
                readObj.set("to", toUser);
                sendMessage(JSONUtil.toJsonStr(readObj), senderSession);
            }
            return;
        }
        // 处理正常聊天消息
        String toUsername = obj.getStr("toUser");
        String text = obj.getStr("message");
        if (toUsername == null || text == null) {
            log.error("普通消息缺少必要字段: {}", message);
            return;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUser(username);
        chatMessage.setToUser(toUsername);
        chatMessage.setMessage(text);
        Session toSession = sessionMap.get(toUsername);
        if (toSession != null) { // 对方在线，直接发送消息
            sendMessage(JSONUtil.toJsonStr(chatMessage), toSession);
            chatMessage.setStatus(1); // 标记为已读
        } else {
            chatMessage.setStatus(0); // 离线，标记为未读
        }
        chatMessage.setCreateTime(new Date());
        chatMessageMapper.insertMessage(chatMessage);
    }
//    @OnMessage
//    public void onMessage(String message,Session session, @PathParam("username") String username){
//        log.info("服务器收到用户username={}的消息:{}",username,message);
//        JSONObject obj = JSONUtil.parseObj(message);
//        String toUsername = obj.getStr("to");       //发送给哪个用户
//        String text = obj.getStr("text");           //发送的消息
//        Session toSession = sessionMap.get(toUsername);  // 根据toUsername来获取session， 再通过session发送消息文本
//        if ( toSession != null ){
//            // 服务器 消息组装后的消息包含发送人和发送信息
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.set("from",username);
//            jsonObject.set("text",text);
//            this.sendMessage(jsonObject.toString(),toSession);
//            log.info("发送给用户username={}，消息：{}",toUsername,jsonObject.toString());
//        }else {
//            log.info("发送失败，未找到用户username={}的session",toUsername);
//        }
//    }

    /**
     * 失败
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.error("WebSocket 发生错误", error);
        error.printStackTrace();
    }

    /**
     * 服务器发送消息给客户端
     */
    private void sendMessage(String message, Session toSession){
        try {
            log.info("服务器给客户端[{}]发送消息{}",toSession.getId(),message);
            toSession.getBasicRemote().sendText(message);
        }catch (Exception e){
            log.error("服务器发送消息给客户端失败",e);
        }
    }

    /**
     * 服务器发消息给所有客户端
     */
    private void sendAllMessage(String message){
        try {
            for (Session session : sessionMap.values()){
                log.info("服务器给客户端[{}]发送消息{}",session.getId(),message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e){
            log.error("服务发送消息给客户端失败",e);
        }
    }

    private void sendChatHistory(String user1, String user2, Session session) {
        List<ChatMessage> messages = chatMessageMapper.getChatHistory(user1, user2);
        JSONObject result = new JSONObject();
        result.set("type", "chatHistory");
        result.set("messages", messages);
        sendMessage(JSONUtil.toJsonStr(result), session);
    }





}
