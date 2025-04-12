package com.example.music_server.controller;

import com.example.music_server.common.Result;
import com.example.music_server.entity.Song;
import com.example.music_server.entity.SongLike;
import com.example.music_server.service.SongLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songlike")
public class SongLikeController {
    @Autowired
    private SongLikeService songLikeService;

    /**
     * 添加喜欢记录
     */
    @PostMapping("/add")
    public Result addSongLike(@RequestBody SongLike songLike) {
        int res = songLikeService.addSongLike(songLike);
        if (res > 0) {
            return Result.success();
        }
        return Result.error();
    }

    /**
     * 获取用户喜欢的歌曲（完整）
     */
    // 获取用户喜欢的歌曲（完整信息）
    @GetMapping("/user/{userId}")
    public Result getLikedSongs(@PathVariable Integer userId) {
        List<Song> likedSongs = songLikeService.getLikedSongsByUserId(userId);
        System.out.println(Result.success(likedSongs));
//        System.out.println(likedSongs);
        return Result.success(likedSongs);
    }


    /**
     * 取消喜欢
     */
    @DeleteMapping("/remove")
    public Result removeSongLike(@RequestParam("userId") Integer userId,
                                 @RequestParam("songId") Integer songId) {
        int res = songLikeService.removeSongLike(userId, songId);
        if (res > 0) {
            return Result.success();
        }
        return Result.error();
    }


    /**
     * 根据用户ID和歌曲ID检查是否已喜欢
     */
    @GetMapping("/check")
    public Result getSongLike(@RequestParam("userId") Integer userId,
                              @RequestParam("songId") Integer songId) {
        boolean songLike = songLikeService.isLiked(userId, songId);
        if (songLike) {
            return Result.success();
        }
        return Result.error();
    }
}
