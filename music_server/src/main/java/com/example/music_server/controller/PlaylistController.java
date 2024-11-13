package com.example.music_server.controller;

import com.example.music_server.entity.Playlist;
import com.example.music_server.entity.Song;
import com.example.music_server.service.PlaylistService;
import com.example.music_server.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    // 获取歌单列表
    @GetMapping
    public List<Playlist> getAllPlaylist() {
        return playlistService.getAllPlaylist();
    }

    // 添加歌曲
    @PostMapping("/add")
    public String addPlaylist(@RequestBody Playlist playlist) {
        playlistService.addPlaylist(playlist);
        return "添加成功";
    }

    // 删除歌曲
    @DeleteMapping("/delete/{id}")
    public String deletePlaylist(@PathVariable Integer id) {
        playlistService.deletePlaylist(id);
        return "删除成功";
    }

    // 更新歌曲
    @PutMapping("/update")
    public String updatePlaylist(@RequestBody Playlist playlist) {
        playlistService.updatePlaylist(playlist);
        return "更新成功";
    }

    //更新图片
    @PutMapping("/updatePlaylistPic/{id}")
    public ResponseEntity<String> updateSongPic(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String imgUrl = request.get("imgUrl");

        if (imgUrl == null || imgUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("图片 URL 不能为空");
        }

        boolean updated = playlistService.updatePlaylistPic(id, imgUrl);

        if (updated) {
            return ResponseEntity.ok("图片 URL 更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("图片 URL 更新失败");
        }
    }

    /**
     * 歌单内容
     */
    // 获取歌单中的所有歌曲
    @GetMapping("/{playlistId}/songs")
    public List<Song> getSongsByPlaylist(@PathVariable Integer playlistId) {
//        System.out.println(playlistId);
        List<Song> a = playlistService.getSongsByPlaylistId(playlistId);
        System.out.println(a.toString());
        return a;
    }

    // 添加歌曲到歌单
    @PostMapping("/{playlistId}/songs/add")
    public void addSongToPlaylist(@PathVariable Integer playlistId, @RequestBody Map<String, String> request) {
        String songName = request.get("songName");
        Integer songId = playlistService.getSongIdBySongName(songName);
        playlistService.addSongToPlaylist(playlistId, songId);
    }

    // 从歌单中删除歌曲
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public void deleteSongFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer songId) {
        playlistService.deleteSongFromPlaylist(playlistId, songId);
    }
}