package com.example.music_server.service;

import com.example.music_server.entity.Admin;

public interface AdminService {
    // 验证管理员账号密码
    String login(String username, String password);

}
