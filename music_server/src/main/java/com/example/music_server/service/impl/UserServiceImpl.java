package com.example.music_server.service.impl;

import com.example.music_server.entity.User;
import com.example.music_server.exception.ServiceException;
import com.example.music_server.mapper.UserMapper;
import com.example.music_server.service.UserService;
import com.example.music_server.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;  // 注入 RedisTemplate

    private static final String USER_CACHE_PREFIX = "user:";  // Redis 缓存 key 前缀


    @Override
    public List<User> getAlluser() {
        return userMapper.getAlluser();
    }

    @Override
    public int count(String searchWord) {
        return userMapper.count(searchWord);
    }

    @Override
    public void addUser(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.addUser(user);
    }

    @Override
    public boolean register(User user) {
        Integer count = userMapper.existsByUsername(user.getUsername());
        if (count != null && count > 0) {
            throw new ServiceException("用户名已存在"); // 如果用户名已存在，抛出异常
        }
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        // 默认角色赋值，如果未指定
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("member");
        }
        return userMapper.register(user) > 0;
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(new Date());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(Integer id, String avatar) {
        userMapper.updateAvatar(id, avatar);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void deleteUsers(List<Integer> ids) {
        userMapper.deleteBatch(ids);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMapper.existsByUsername(username) > 0;
    }

//    @Override
//    public User findByUsername(String username) {
//        return userMapper.findByUsername(username);
//    }
    @Override
    public User findByUsername(String username) {
        String cacheKey = USER_CACHE_PREFIX + username;

        // 先查 Redis
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        User cachedUser = (User) ops.get(cacheKey);
        if (cachedUser != null) {
            System.out.println("🔹 从 Redis 缓存获取用户：" + username);
            return cachedUser;
        }

        // 缓存不存在，从数据库查询
        User user = userMapper.findByUsername(username);
        if (user != null) {
            // 写入 Redis 缓存，过期时间 10 分钟
            ops.set(cacheKey, user, 10, TimeUnit.MINUTES);
        }
        return user;
    }

//    @Override
//    public User login(User user) {
//        // 根据用户名查询用户信息
//        User dbUser = userMapper.findByUsername(user.getUsername());
//        if (dbUser == null || !user.getPassword().equals(dbUser.getPassword())) {
//            throw new ServiceException("用户名或密码错误");
//        }
//
//        // 登录成功，生成 token
//        String token = TokenUtils.createToken(String.valueOf(dbUser.getId()), dbUser.getPassword());
//        dbUser.setToken(token);
//        return dbUser;
//    }
    @Override
    public int updateStatusByUsername(String username) {
        return userMapper.updateStatusByUsername(username);
    }

    @Override
    public User login(User user) {
        String cacheKey = USER_CACHE_PREFIX + user.getUsername();
        // 先查询数据库
        User dbUser = userMapper.findByUsername(user.getUsername());
        if (dbUser == null || !user.getPassword().trim().equals(dbUser.getPassword().trim())) {
            throw new ServiceException("用户名或密码错误");
        }

        // 生成 token
        String token = TokenUtils.createToken(String.valueOf(dbUser.getId()), dbUser.getPassword());
        dbUser.setToken(token);

        // 将用户信息存入 Redis，缓存时间 30 分钟
        redisTemplate.opsForValue().set(cacheKey, dbUser, 30, TimeUnit.MINUTES);

        userMapper.updateStatusByUsername(user.getUsername());

        System.out.println(dbUser);
        return dbUser;
    }

}
