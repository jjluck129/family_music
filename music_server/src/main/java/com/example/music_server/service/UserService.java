package com.example.music_server.service;

import com.example.music_server.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAlluser();
    int count(String searchWord);
    void addUser(User user);
    void updateUser(User user);
    void updateAvatar(Integer id, String avatar);
    void deleteUser(Integer id);
    void deleteUsers(List<Integer> ids);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
