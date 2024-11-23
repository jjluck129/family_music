package com.example.music_server.service.impl;

import com.example.music_server.entity.User;
import com.example.music_server.exception.ServiceException;
import com.example.music_server.mapper.UserMapper;
import com.example.music_server.service.UserService;
import com.example.music_server.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }


    @Override
    public User login(User user) {
        // 根据用户名查询用户信息
        User dbUser = userMapper.findByUsername(user.getUsername());
        if (dbUser == null || !user.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }

        // 登录成功，生成 token
        String token = TokenUtils.createToken(String.valueOf(dbUser.getId()), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }
}
