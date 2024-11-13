package com.example.music_server.controller;


import com.example.music_server.entity.Singer;
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
    public List<Singer> getAllSingers() {
        return singerService.getAllSingers();
    }

    // 根据ID获取歌手
    @GetMapping("/{id}")
    public Singer getSingerById(@PathVariable Integer id) {
        return singerService.getSingerById(id);
    }

    // 添加新歌手
    @PostMapping
    public String addSinger(@RequestBody Singer singer) {
        int result = singerService.addSinger(singer);
        return result > 0 ? "添加成功" : "添加失败";
    }

    // 更新歌手信息
    @PutMapping("/{id}")
    public String updateSinger(@PathVariable Integer id, @RequestBody Singer singer) {
        singer.setId(id);
        int result = singerService.updateSinger(singer);
        return result > 0 ? "更新成功" : "更新失败";
    }
    @PatchMapping("/{id}")
    public String updateSingerImg(@PathVariable Integer id,@RequestBody Singer singer){
        String imgUrl = singer.getImgUrl();
        System.out.println(singer.getImgUrl());
        int result = singerService.updateSingerImg(id,imgUrl);
        return result > 0 ? "更新成功" : "更新失败";
    }

    // 删除歌手
    @DeleteMapping("/{id}")
    public String deleteSingerById(@PathVariable Integer id) {
        int result = singerService.deleteSingerById(id);
        return result > 0 ? "删除成功" : "删除失败";
    }

    //批量删除歌手
    // 批量删除歌手
    @DeleteMapping
    public String deleteSingersByIds(@RequestBody List<Integer> ids) {
        int result = singerService.deleteSingersByIds(ids); // 这里需要在 service 中实现该方法
        return result > 0 ? "批量删除成功" : "批量删除失败";
    }

}