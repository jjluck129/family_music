package com.example.music_server.mapper;

import com.example.music_server.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // 获取所有用户信息
    @Select("SELECT * FROM user")
    List<User> getAlluser();

    // 获取用户总数
    @Select("SELECT COUNT(*) FROM user WHERE username LIKE CONCAT('%', #{searchWord}, '%') OR email LIKE CONCAT('%', #{searchWord}, '%')")
    int count(@Param("searchWord") String searchWord);

    // 根据ID查找用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Integer id);

    // 添加用户
    @Insert("INSERT INTO user (username, password, gender, telephone, email, birth, introduction, location, avatar, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{gender}, #{telephone}, #{email}, #{birth}, #{introduction}, #{location}, #{avatar}, #{createTime}, #{updateTime})")
    void addUser(User user);

    // 更新用户信息
    @Update("UPDATE user SET username = #{username}, password = #{password}, gender = #{gender}, telephone = #{telephone}, email = #{email}, " +
            "birth = #{birth}, introduction = #{introduction}, location = #{location}, avatar = #{avatar}, update_time = NOW() WHERE id = #{id}")
    void update(User user);

    // 更新用户头像
    @Update("UPDATE user SET avatar = #{avatar}, update_time = NOW() WHERE id = #{id}")
    void updateAvatar(@Param("id") Integer id, @Param("avatar") String avatar);

    // 删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteById(Integer id);

    // 批量删除用户
    @Delete("<script>" +
            "DELETE FROM user WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteBatch(@Param("ids") List<Integer> ids);

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int existsByUsername(String username);

    @Select("SELECT * FROM user WHERE username = #{username};")
    User findByUsername(String username);
}

