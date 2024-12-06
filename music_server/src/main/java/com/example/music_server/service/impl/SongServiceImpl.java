package com.example.music_server.service.impl;

import com.example.music_server.entity.Song;
import com.example.music_server.mapper.SongMapper;
import com.example.music_server.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private SongMapper songMapper;

    @Override
    public List<Song> getSongsBySingerId(Integer singerId) {
        List<Song> songs = songMapper.getSongsBySingerId(singerId);
        return songs;
    }


    @Override
    public void addSong(Song song) {
        songMapper.addSong(song);
    }

    @Override
    public void deleteSong(Integer songId) {
        songMapper.deleteSong(songId);
    }

    @Override
    public void updateSong(Song song) {
        songMapper.updateSong(song);
    }

    @Override
    public Song getSongById(Integer songId) {
        return songMapper.getSongById(songId);
    }

    @Override
    public boolean updateSongPic(Integer id, String picUrl){
        int rowUpdated = songMapper.updateSongPic(id,picUrl);
        return rowUpdated > 0;
    }

    @Override
    public boolean updateSongMp3(Integer id, String mp3Url) {
        return songMapper.updateSongMp3(id, mp3Url) > 0;
    }

    @Override
    public List<Song> searchSongs(String keyword){
        return songMapper.searchSongs("%" + keyword + "%");
    }

}