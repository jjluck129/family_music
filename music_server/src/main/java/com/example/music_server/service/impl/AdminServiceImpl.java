package com.example.music_server.service.impl;

import com.example.music_server.entity.Admin;
import com.example.music_server.mapper.AdminMapper;
import com.example.music_server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean validateAdminCredentials(String name, String password) {
        Admin admin = adminMapper.selectAdminByUsername(name);
        // 检查用户名是否存在且密码匹配
        return admin != null && admin.getPassword().equals(password); // 这里可以添加密码加密的比较逻辑
    }

    @Override
    public Admin getAdminByName(String name) {
        return adminMapper.selectAdminByUsername(name);
    }
}
