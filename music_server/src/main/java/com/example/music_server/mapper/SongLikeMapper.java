package com.example.music_server.mapper;

import com.example.music_server.entity.SongLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SongLikeMapper {
    // 获取用户喜欢的歌曲 ID
    List<Integer> getLikedSongIdsByUserId(@Param("userId") Integer userId);
    // 判断是否喜欢
    boolean isLiked(@Param("userId") Integer userId, @Param("songId") Integer songId);
    int insertSongLike(SongLike songLike);
    int deleteSongLike(@Param("userId") Integer userId, @Param("songId") Integer songId);
    List<SongLike> selectSongLikesByUserId(@Param("userId") Integer userId);
    SongLike selectSongLike(@Param("userId") Integer userId, @Param("songId") Integer songId);
}
