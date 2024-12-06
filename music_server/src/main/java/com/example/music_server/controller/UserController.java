package com.example.music_server.controller;

import com.example.music_server.common.Result;
import com.example.music_server.entity.User;
import com.example.music_server.exception.ServiceException;
import com.example.music_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @GetMapping
    public Result getAlluser(){
        List<User> list = userService.getAlluser();
        return Result.success(list);
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
    public Result updateAvatar(@PathVariable Integer id,  @RequestHeader("token") String token,@RequestBody Map<String, String> request) {
//        logger.info("进入更新头像方法，处理用户ID为 {} 的头像更新请求", id);
//        String avatar = request.get("avatar");
//        System.out.println(avatar);
//        userService.updateAvatar(id, avatar);
        try {
            String avatar = request.get("avatar");
            logger.info("从请求体中获取到的头像信息: {}", avatar);

            // 调用服务层方法更新头像
            userService.updateAvatar(id, avatar);

            logger.info("头像更新成功");
            return Result.success();
        } catch (Exception e) {
            logger.error("头像更新过程中出现错误: {}", e.getMessage());
            // 返回一个包含错误信息的响应给前端，状态码设置为500，表示服务器内部错误
            return Result.error() ;
        }
//        return Result.success();
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
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        try {
            User loggedInUser = userService.login(user);
            return Result.success(loggedInUser); // 登录成功返回数据
        } catch (ServiceException e) {
            return Result.error(e.getCode(), e.getMessage()); // 捕获自定义异常返回错误信息
        } catch (Exception e) {
            return Result.error("500", "系统异常，请稍后再试"); // 捕获其他未知异常
        }
    }

}