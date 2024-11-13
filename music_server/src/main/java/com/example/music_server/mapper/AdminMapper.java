package com.example.music_server.mapper;

import com.example.music_server.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    // 根据用户名查询管理员信息
    @Select("SELECT * FROM admin WHERE name = #{name}")
    Admin selectAdminByUsername(@Param("name") String name);
}
