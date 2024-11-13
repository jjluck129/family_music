package com.example.music_server.service;

import com.example.music_server.entity.Admin;

public interface AdminService {
    // 验证管理员账号密码
    boolean validateAdminCredentials(String name, String password);

    // 根据用户名获取管理员
    Admin getAdminByName(String name);
}
