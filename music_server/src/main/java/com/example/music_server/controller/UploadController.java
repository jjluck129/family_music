package com.example.music_server.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final String uploadDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img"; // 存储图片的目录

    // 将 PostMapping 的路径修改为根路径
    @PostMapping
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 根据类型选择上传目录
        String uploadDir;
        if ("singerImg".equals(type)) {
            uploadDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\singerImg";
        } else if ("songImg".equals(type)) {
            uploadDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\songImg";
        } else if ("playlistImg".equals(type)) {
          uploadDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\playlistImg";
        } else if ("userImg".equals(type)) {
            uploadDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\userImg";
        }else {
            throw new RuntimeException("无效的上传类型");
        }


        // 确保目录存在
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // 创建目录
        }

        String fileName = file.getOriginalFilename();
        File dest = new File(uploadDir, fileName);

        // 保存文件
        file.transferTo(dest);

        // 返回图片访问路径
        Map<String, Object> response = new HashMap<>();
        // 返回完整路径
        response.put("url", "http://localhost:9090//img/" + (type.equals("singerImg") ? "singerImg/" : type.equals("songImg") ? "songImg/" : type.equals("playlistImg") ? "playlistImg/" : "userImg/") + fileName);
        return response;
    }

}
