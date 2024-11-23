package com.example.music_server.service.impl;

import com.example.music_server.entity.Admin;
import com.example.music_server.exception.ServiceException;
import com.example.music_server.mapper.AdminMapper;
import com.example.music_server.service.AdminService;
import com.example.music_server.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public String login(String username, String password) {
        // 根据用户名查询用户
        Admin user = adminMapper.findByUsername(username);
        if (user == null) {
            throw new ServiceException("401", "用户名不存在");
        }

        // 校验密码
        if (!user.getPassword().equals(password)) {
            throw new ServiceException("401", "密码错误");
        }

        // 登录成功，生成 Token
        String token = TokenUtils.createToken(String.valueOf(user.getId()), user.getPassword());
        return token; // 返回生成的 token
    }

}
