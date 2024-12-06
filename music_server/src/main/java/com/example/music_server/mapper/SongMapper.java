package com.example.music_server.mapper;

import com.example.music_server.entity.Song;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SongMapper {

    // 根据歌手ID获取歌曲列表
    @Select("SELECT * FROM songs WHERE singer_id = #{singerId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "singerId", column = "singer_id"),
            @Result(property = "singerName", column = "singer_name"),
            @Result(property = "songName", column = "song_name"),
            @Result(property = "albumName", column = "album_name"),
            @Result(property = "lyrics", column = "lyrics"),
            @Result(property = "picUrl", column = "pic_url"),
            @Result(property = "mp3Url", column = "mp3_url")
    })
    List<Song> getSongsBySingerId(@Param("singerId") Integer singerId);


    // 插入一首新歌曲
    @Insert("INSERT INTO songs (singer_id, singer_name, song_name, album_name, lyrics, pic_url) " +
            "VALUES (#{singerId}, #{singerName}, #{songName}, #{albumName}, #{lyrics}, #{picUrl})")
    void addSong(Song song);

    // 删除歌曲
    @Delete("DELETE FROM songs WHERE id = #{id}")
    void deleteSong(@Param("id") Integer id);

    // 更新歌曲信息
    @Update("UPDATE songs SET song_name = #{songName}, album_name = #{albumName}, lyrics = #{lyrics}, pic_url = #{picUrl} " +
            "WHERE id = #{id}")
    void updateSong(Song song);

    // 根据歌曲ID查询
    @Select("SELECT id AS id, singer_id AS singerId, singer_name AS singerName, song_name AS songName, album_name AS albumName, lyrics, pic_url AS picUrl, mp3_url AS mp3Url FROM songs WHERE id = #{songId}")
    Song getSongById(@Param("songId") Integer songId);

    //插入图片地址URL
    @Update("UPDATE songs SET pic_url = #{picUrl} WHERE id = #{id}")
    int updateSongPic(@Param("id") Integer id,@Param("picUrl") String picUrl);

    //更新MP地址
    @Update("UPDATE songs SET mp3_url = #{mp3Url} WHERE id = #{id}")
    int updateSongMp3(@Param("id") Integer id, @Param("mp3Url") String mp3Url);

    //搜索歌曲
    @Select("SELECT * FROM songs WHERE song_name LIKE #{keyword} OR singer_name LIKE #{keyword} OR album_name LIKE #{keyword}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "singerId", column = "singer_id"),
            @Result(property = "singerName", column = "singer_name"),
            @Result(property = "songName", column = "song_name"),
            @Result(property = "albumName", column = "album_name"),
            @Result(property = "lyrics", column = "lyrics"),
            @Result(property = "picUrl", column = "pic_url"),
            @Result(property = "mp3Url", column = "mp3_url")
    })
    List<Song> searchSongs(@Param("keyword") String keyword);
}