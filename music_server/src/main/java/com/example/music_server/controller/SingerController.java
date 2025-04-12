package com.example.music_server.controller;


import com.example.music_server.common.Result;
import com.example.music_server.entity.Singer;
import com.example.music_server.entity.Song;
import com.example.music_server.service.SingerService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/singers")
public class SingerController {

    @Autowired
    private SingerService singerService;

    // 获取所有歌手
    @GetMapping
    public Result getAllSingers() {
        List<Singer> list = singerService.getAllSingers();
        return Result.success(list);
    }

    // 根据ID获取歌手
    @GetMapping("/{id}")
    public Result getSingerById(@PathVariable Integer id) {
        Singer singer = singerService.getSingerById(id);
        return Result.success(singer);
    }

    // 添加新歌手
    @PostMapping
    public Result  addSinger(@RequestBody Singer singer) {
        int result = singerService.addSinger(singer);
        if (result > 0) {
            return Result.success("添加成功");  // 返回添加成功的响应
        } else {
            return Result.error("500", "添加失败");  // 返回添加失败的错误响应
        }
    }

    // 更新歌手信息
    @PutMapping("/{id}")
    public Result  updateSinger(@PathVariable Integer id, @RequestBody Singer singer) {
        singer.setId(id);
        int result = singerService.updateSinger(singer);
        if (result > 0) {
            return Result.success("更新成功");  // 返回更新成功的响应
        } else {
            return Result.error("500", "更新失败");  // 返回更新失败的错误响应
        }
    }
    @PatchMapping("/{id}")
    public Result  updateSingerImg(@PathVariable Integer id,@RequestBody Singer singer){
        String imgUrl = singer.getImgUrl();
        System.out.println(singer.getImgUrl());
        int result = singerService.updateSingerImg(id,imgUrl);
        if (result > 0) {
            return Result.success("更新成功");  // 返回更新成功的响应
        } else {
            return Result.error("500", "更新失败");  // 返回更新失败的错误响应
        }
    }

    // 删除歌手
    @DeleteMapping("/{id}")
    public Result  deleteSingerById(@PathVariable Integer id) {
        int result = singerService.deleteSingerById(id);
        if (result > 0) {
            return Result.success("删除成功");  // 返回删除成功的响应
        } else {
            return Result.error("500", "删除失败");  // 返回删除失败的错误响应
        }
    }

    //批量删除歌手
    // 批量删除歌手
    @DeleteMapping
    public Result  deleteSingersByIds(@RequestBody List<Integer> ids) {
        int result = singerService.deleteSingersByIds(ids); // 这里需要在 service 中实现该方法
        if (result > 0) {
            return Result.success("批量删除成功");  // 返回批量删除成功的响应
        } else {
            return Result.error("500", "批量删除失败");  // 返回批量删除失败的错误响应
        }
    }

}