package com.example.music_server.mapper;

import com.example.music_server.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAlluser();

    // 获取用户总数
    int count(String searchWord);

    // 获取所有用户
    List<User> getAllUsers();

    // 根据ID查找用户
    User findById(Integer id);

    // 添加用户
    void addUser(User user);

    //注册用户
    int register(User user);

    // 更新用户信息
    void update(User user);

    // 更新用户头像
    void updateAvatar(Integer id, String avatar);

    // 删除用户
    void deleteById(Integer id);

    // 批量删除用户
    void deleteBatch(List<Integer> ids);

    // 检查用户名是否存在
    int existsByUsername(String username);

    // 根据用户名查找用户
    User findByUsername(String username);

    //更新在线状态
    int updateStatusByUsername(@Param("username") String username);
}

