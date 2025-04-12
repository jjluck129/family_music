package com.example.music_server.mapper;

import com.example.music_server.entity.Song;
import org.apache.ibatis.annotations.*;

import javax.websocket.server.ServerEndpoint;
import java.util.List;

@Mapper
public interface SongMapper {
    // 根据歌手ID获取歌曲列表
    List<Song> getSongsBySingerId(Integer singerId);


    // 插入一首新歌曲
    void addSong(Song song);

    // 删除歌曲
    void deleteSong(Integer id);

    // 更新歌曲信息
    void updateSong(Song song);

    // 根据歌曲ID查询
    Song getSongById(Integer songId);

    //插入图片地址URL
    int updateSongPic(Integer id, String picUrl);

    //更新MP地址
    int updateSongMp3(Integer id, String mp3Url);

    //搜索歌曲
    List<Song> searchSongs(String keyword);

    //获取专辑
    List<Song> getAllAlbums();

    //根据专辑获取歌曲
    List<Song> getSongsByAlbum(String albumName);

    //根据id批量获取歌曲
    List<Song> getSongsByIds(@Param("ids") List<Integer> ids);
}