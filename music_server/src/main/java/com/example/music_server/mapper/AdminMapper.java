package com.example.music_server.mapper;

import com.example.music_server.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    // 根据用户名查询用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    Admin findByUsername(String username);

    // 根据用户ID查询用户（如果需要）
    @Select("SELECT * FROM users WHERE id = #{id}")
    Admin findById(Integer id);
}
