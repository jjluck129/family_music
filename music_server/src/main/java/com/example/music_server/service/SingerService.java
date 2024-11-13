package com.example.music_server.service;

import com.example.music_server.entity.Singer;

import java.util.List;

public interface SingerService {
    // 获取所有歌手
    List<Singer> getAllSingers();

    // 根据ID获取歌手
    Singer getSingerById(Integer id);

    // 添加歌手
    int addSinger(Singer singer);

    // 更新歌手信息
    int updateSinger(Singer singer);
    int updateSingerImg(Integer id, String imgUrl);

    // 删除歌手
    int deleteSingerById(Integer id);

    int deleteSingersByIds(List<Integer> ids);


}
