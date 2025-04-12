package com.example.music_server.mapper;

import com.example.music_server.entity.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    // 插入新消息
    @Insert("INSERT INTO chat_message (from_user, to_user, message, status, create_time) " +
            "VALUES (#{fromUser}, #{toUser}, #{message}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertMessage(ChatMessage message);

    // 获取用户的未读消息
    @Select("SELECT id, from_user as fromUser, to_user as toUser, message, status, create_time " +
            "FROM chat_message WHERE to_user = #{username} AND status = 0 ORDER BY create_time ASC")
    List<ChatMessage> getUnreadMessages(@Param("username") String username);

    // 标记消息为已读
    @Update("UPDATE chat_message SET status = 1 " +
            "WHERE from_user = #{withUser} AND to_user = #{user} AND status = 0")
    int markMessagesAsRead(@Param("user") String user, @Param("withUser") String withUser);

    @Select("SELECT id, from_user as fromUser, to_user as toUser, message, status, create_time " +
            "FROM chat_message " +
            "WHERE (from_user = #{user} AND to_user = #{withUser}) " +
            "   OR (from_user = #{withUser} AND to_user = #{user}) " +
            "ORDER BY create_time ASC")
    List<ChatMessage> getChatHistoryBetween(@Param("user") String user, @Param("withUser") String withUser);

    @Select("SELECT * FROM chat_message WHERE (from_user = #{user1} AND to_user = #{user2}) OR (from_user = #{user2} AND to_user = #{user1}) ORDER BY timestamp ASC")
    List<ChatMessage> getChatHistory(@Param("user1") String user1, @Param("user2") String user2);

}
