package com.example.music_server.controller;

import com.example.music_server.common.Result;
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
    public Result getAllPlaylist() {
        List<Playlist> playlists = playlistService.getAllPlaylist();
        return Result.success(playlists);
    }

    // 添加歌曲
    @PostMapping("/add")
    public Result addPlaylist(@RequestBody Playlist playlist) {
        playlistService.addPlaylist(playlist);
        return Result.success("歌单添加成功");
    }

    // 删除歌曲
    @DeleteMapping("/delete/{id}")
    public Result deletePlaylist(@PathVariable Integer id) {
        playlistService.deletePlaylist(id);
        return Result.success("歌单删除成功");
    }

    // 更新歌曲
    @PutMapping("/update")
    public Result updatePlaylist(@RequestBody Playlist playlist) {
        playlistService.updatePlaylist(playlist);
        return Result.success("歌单更新成功");
    }

    //更新图片
    @PutMapping("/updatePlaylistPic/{id}")
    public Result  updatePlaylistPic(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String imgUrl = request.get("imgUrl");

        if (imgUrl == null || imgUrl.isEmpty()) {
            return Result.error("400", "图片 URL 不能为空");
        }

        boolean updated = playlistService.updatePlaylistPic(id, imgUrl);

        if (updated) {
            return Result.success("图片 URL 更新成功");
        } else {
            return Result.error("500", "图片 URL 更新失败");
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
    public Result  addSongToPlaylist(@PathVariable Integer playlistId, @RequestBody Map<String, String> request) {
        String songName = request.get("songName");
        if (songName == null || songName.isEmpty()) {
            return Result.error("400", "歌曲名不能为空");
        }

        Integer songId = playlistService.getSongIdBySongName(songName);
        if (songId == null) {
            return Result.error("404", "歌曲未找到");
        }

        playlistService.addSongToPlaylist(playlistId, songId);
        return Result.success("歌曲添加到歌单成功");
    }

    // 从歌单中删除歌曲
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public Result  deleteSongFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer songId) {
        playlistService.deleteSongFromPlaylist(playlistId, songId);
        return Result.success("歌曲从歌单中删除成功");
    }
}