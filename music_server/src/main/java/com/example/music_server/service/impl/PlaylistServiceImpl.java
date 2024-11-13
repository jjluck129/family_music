package com.example.music_server.service.impl;

import com.example.music_server.entity.Playlist;
import com.example.music_server.entity.PlaylistSong;
import com.example.music_server.entity.Song;
import com.example.music_server.mapper.PlaylistMapper;
import com.example.music_server.mapper.SongMapper;
import com.example.music_server.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private SongMapper songMapper;

    @Override
    public List<Playlist> getAllPlaylist() {
        return playlistMapper.getAllPlaylist();
    }

    @Override
    public void addPlaylist(Playlist playlist) {
        playlistMapper.addPlaylist(playlist);
    }

    @Override
    public void deletePlaylist(Integer playlistId) {
        playlistMapper.deletePlaylist(playlistId);
    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        playlistMapper.updatePlaylist(playlist);
    }

    @Override
    public Playlist getPlaylisyById(Integer playlistId) {
        return playlistMapper.getPlaylisyById(playlistId);
    }

    @Override
    public boolean updatePlaylistPic(Integer id, String imgUrl){
        int rowUpdated = playlistMapper.updatePlaylistPic(id,imgUrl);
        return rowUpdated > 0;
    }

    /**
     * 歌单内容
     */
    @Override
    public List<Song> getSongsByPlaylistId(Integer playlistId) {
        // 获取歌单中所有歌曲ID
        List<PlaylistSong> playlistSongs = playlistMapper.getSongsByPlaylistId(playlistId);
        System.out.println(playlistSongs.toString());

        // 根据每个歌曲ID查询歌曲详细信息
        List<Song> songs = new ArrayList<>();
        for (PlaylistSong playlistSong : playlistSongs) {
            Song song = songMapper.getSongById(playlistSong.getSongId());
            if (song != null) {
                songs.add(song);
            }
        }
        System.out.println(songs);
        return songs;
    }

    @Override
    public void addSongToPlaylist(Integer playlistId, Integer songId) {
        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setPlaylistId(playlistId);
        playlistSong.setSongId(songId);
        playlistMapper.addPlaylistSong(playlistSong);
    }

    @Override
    public void deleteSongFromPlaylist(Integer playlistId, Integer songId) {
        playlistMapper.deleteSongFromPlaylist(playlistId, songId);
    }

    @Override
    public Integer getSongIdBySongName(String songName){
        return playlistMapper.getSongIdBySongName(songName);
    }
}