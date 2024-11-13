package com.example.music_server.service;

import com.example.music_server.entity.Song;

import java.util.List;

public interface SongService {

    // 根据歌手ID获取该歌手的所有歌曲
    List<Song> getSongsBySingerId(Integer singerId);

    // 添加一首新歌
    void addSong(Song song);

    // 删除歌曲
    void deleteSong(Integer songId);

    // 更新歌曲
    void updateSong(Song song);

    // 根据歌曲ID获取歌曲详情
    Song getSongById(Integer songId);

    boolean updateSongPic(Integer id, String picUrl);

    boolean updateSongMp3(Integer id, String mp3Url);
}