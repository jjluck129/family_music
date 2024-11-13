package com.example.music_server.service.impl;

import com.example.music_server.entity.User;
import com.example.music_server.mapper.UserMapper;
import com.example.music_server.service.UserService;
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

}
