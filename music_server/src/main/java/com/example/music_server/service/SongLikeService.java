package com.example.music_server.service;

import com.example.music_server.entity.Song;
import com.example.music_server.entity.SongLike;

import java.util.List;

public interface SongLikeService {
    List<Song> getLikedSongsByUserId(Integer userId);
    boolean isLiked(Integer userId, Integer songId);
    int addSongLike(SongLike songLike);
    int removeSongLike(Integer userId, Integer songId);
    List<SongLike> getSongLikesByUserId(Integer userId);
    SongLike getSongLike(Integer userId, Integer songId);
}
