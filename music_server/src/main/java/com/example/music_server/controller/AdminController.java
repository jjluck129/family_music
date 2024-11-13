package com.example.music_server.controller;

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
    public ResponseEntity<String> login( @RequestBody Map<String, String> request) {
        String name = request.get("username");
        String password = request.get("password");
        System.out.println(name);
        System.out.println(password);
        boolean isValid = adminService.validateAdminCredentials(name, password);
        if (isValid) {
            return ResponseEntity.ok("登录成功");
        } else {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
    }
}
