package com.example.music_server.controller;

import com.example.music_server.common.Result;
import com.example.music_server.entity.Admin;
import com.example.music_server.exception.ServiceException;
import com.example.music_server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    // 登录接口
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        System.out.println(admin);
        try {
            // 调用 Service 层进行登录验证
            String token = adminService.login(admin.getUsername(), admin.getPassword());
            return Result.success(token);  // 返回生成的 token
        } catch (ServiceException e) {
            return Result.error(e.getCode(), e.getMessage());  // 返回错误信息
        }
    }
}
