package com.example.music_server.service;

import com.example.music_server.entity.Playlist;
import com.example.music_server.entity.Song;

import java.util.List;

public interface PlaylistService {

    // 获取歌单列表
    List<Playlist> getAllPlaylist();

    // 添加一首新歌
    void addPlaylist(Playlist playlist);

    // 删除歌曲
    void deletePlaylist(Integer playlistId);

    // 更新歌曲
    void updatePlaylist(Playlist playlist);

    // 根据歌曲ID获取歌曲详情
    Playlist getPlaylisyById(Integer playlistId);

    boolean updatePlaylistPic(Integer id, String imgUrl);

    /**
     * 歌单内容
     */
    // 根据歌单ID获取歌曲详情
    List<Song> getSongsByPlaylistId(Integer playlistId);

    // 向歌单中添加歌曲
    void addSongToPlaylist(Integer playlistId, Integer songId);

    // 从歌单中移除歌曲
    void deleteSongFromPlaylist(Integer playlistId, Integer songId);

    Integer getSongIdBySongName(String songName);
}