package com.example.music_server.controller;

import com.example.music_server.entity.User;
import com.example.music_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public List<User> getAlluser(){
        return userService.getAlluser();
    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        System.out.println(user.toString());
        userService.addUser(user);
        return "添加成功";
    }

    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return "更新成功";
    }

    @PutMapping("/updateAvatar/{id}")
    public String updateAvatar(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String avatar = request.get("avatar");
        System.out.println(avatar);
        userService.updateAvatar(id, avatar);
        return "头像更新成功";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "删除成功";
    }

    @DeleteMapping("/deleteBatch")
    public String deleteUsers(@RequestBody List<Integer> ids) {
        userService.deleteUsers(ids);
        return "批量删除成功";
    }

    /**
     * 用户注册
     */
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody User user) {
//        // 检查用户名是否唯一
//        if (userService.existsByUsername(user.getUsername())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("用户名已存在");
//        }
//
//        // 对密码进行加密
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // 保存用户信息
//        userService.addUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body("注册成功");
//    }

    /**
     * 用户登录
     */
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
//        String username = loginData.get("username");
//        String password = loginData.get("password");
//
//        User user = userService.findByUsername(username);
//
//        // 验证用户名和密码
//        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
//        }
//
//        // 生成 JWT Token
//        String token = jwtUtil.generateToken(username);
//
//        // 返回 token
//        Map<String, String> response = new HashMap<>();
//        response.put("token", token);
//        return ResponseEntity.ok(response);
//    }
}