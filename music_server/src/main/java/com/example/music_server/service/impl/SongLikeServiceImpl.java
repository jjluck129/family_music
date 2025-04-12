package com.example.music_server.service.impl;

import com.example.music_server.entity.Song;
import com.example.music_server.entity.SongLike;
import com.example.music_server.mapper.SongLikeMapper;
import com.example.music_server.mapper.SongMapper;
import com.example.music_server.service.SongLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SongLikeServiceImpl implements SongLikeService {
    @Autowired
    private SongLikeMapper songLikeMapper;

    @Resource
    private SongMapper songMapper;

    @Override
    public List<Song> getLikedSongsByUserId(Integer userId) {
        List<Integer> songIds = songLikeMapper.getLikedSongIdsByUserId(userId);
        if (songIds.isEmpty()) {
            return null;
        }
        return songMapper.getSongsByIds(songIds);
    }

    @Override
    public boolean isLiked(Integer userId, Integer songId) {
        return songLikeMapper.isLiked(userId, songId);
    }

    @Override
    public int addSongLike(SongLike songLike) {
        return songLikeMapper.insertSongLike(songLike);
    }

    @Override
    public int removeSongLike(Integer userId, Integer songId) {
        return songLikeMapper.deleteSongLike(userId, songId);
    }

    @Override
    public List<SongLike> getSongLikesByUserId(Integer userId) {
        return songLikeMapper.selectSongLikesByUserId(userId);
    }

    @Override
    public SongLike getSongLike(Integer userId, Integer songId) {
        return songLikeMapper.selectSongLike(userId, songId);
    }
}
