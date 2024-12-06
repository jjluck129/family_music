package com.example.music_server.controller;

import com.example.music_server.common.Result;
import com.example.music_server.entity.Song;
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
@RequestMapping("/songs")
public class SongController {

    private static final String MP3_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\song";
    @Autowired
    private SongService songService;

    // 根据歌手ID获取歌曲列表
    @GetMapping("/list/{singerId}")
    public Result getSongsBySingerId(@PathVariable Integer singerId) {
        List<Song> songs = songService.getSongsBySingerId(singerId);
        return Result.success(songs);
    }

    // 添加歌曲
    @PostMapping("/add")
    public Result  addSong(@RequestBody Song song) {
        songService.addSong(song);
        return Result.success("添加成功");
    }

    // 删除歌曲
    @DeleteMapping("/delete/{id}")
    public Result deleteSong(@PathVariable Integer id) {
        songService.deleteSong(id);
        return Result.success("删除成功");
    }

    // 更新歌曲
    @PutMapping("/update")
    public Result  updateSong(@RequestBody Song song) {
        songService.updateSong(song);
        return Result.success("更新成功");
    }

    // 上传MP3文件
    @PostMapping("/uploadMp3")
    public ResponseEntity<Map<String, String>> uploadMp3(
            @RequestParam("mp3") MultipartFile mp3File,
            @RequestParam("songId") Integer songId) {
        System.out.println("Received songId: " + songId); // 打印 songId
        System.out.println("Received mp3 file: " + mp3File.getOriginalFilename()); // 打印文件名
        if (mp3File.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "上传文件不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 保存文件
            File destFile = new File(MP3_PATH);
            if(!destFile.exists()){
                destFile.mkdirs();
            }
            String fileName = mp3File.getOriginalFilename();
            File dest = new File(destFile,fileName);
            mp3File.transferTo(dest);

            // 更新数据库中的 MP3 文件路径
            String mp3Url = "http://localhost:9090//song/" + fileName;
            boolean rowsUpdated = songService.updateSongMp3(songId, mp3Url);

            //返回相对路径供前端访问
            Map<String, String> response = new HashMap<>();
            if (rowsUpdated) {
//                response.put("message", "MP3文件上传成功");
                response.put("url", mp3Url);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "更新数据库失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "文件上传失败"+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/updateSongPic/{id}")
    public ResponseEntity<String> updateSongPic(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String picUrl = request.get("picUrl");

        if (picUrl == null || picUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("图片 URL 不能为空");
        }

        boolean updated = songService.updateSongPic(id, picUrl);

        if (updated) {
            return ResponseEntity.ok("图片 URL 更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("图片 URL 更新失败");
        }
    }

//    搜索歌曲
    @GetMapping("/search")
    public Result searchSongs(@RequestParam("keyword") String keyword){
        List<Song> songs = songService.searchSongs(keyword);
        System.out.println(songs);
        return Result.success(songs);
    }

}