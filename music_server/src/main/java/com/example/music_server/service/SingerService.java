package com.example.music_server.service;

import com.example.music_server.entity.Singer;

import java.util.List;

public interface SingerService {
    List<Singer> getAllSingers();     // 获取所有歌手
    Singer getSingerById(Integer id); // 根据ID获取歌手
    int addSinger(Singer singer);     // 添加歌手
    int updateSinger(Singer singer);    // 更新歌手信息
    int updateSingerImg(Integer id, String imgUrl); // 更新歌手图片
    int deleteSingerById(Integer id);    // 删除歌手
    int deleteSingersByIds(List<Integer> ids); // 删除多个歌手
}
