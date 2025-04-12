package com.example.music_server.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    private Long id;
    private String fromUser;
    private String toUser;
    private String message;
    private int status;
    private Date createTime;
}
