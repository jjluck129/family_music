package com.example.music_server.mapper;

import com.example.music_server.entity.Singer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SingerMapper {
    List<Singer> getAllSingers();    // 获取所有歌手
    Singer getSingerById(Integer id);    // 根据ID获取歌手
    int insertSinger(Singer singer);    // 添加新歌手
    int updateSinger(Singer singer);    // 更新歌手信息
    int updateSingerImg(Integer id, String imgUrl);    // 更新歌手图片地址
    int deleteSingerById(Integer id);    // 删除歌手
    Singer findById(Integer id);    // 根据歌手id查询信息
    int deleteSingersByIds(String ids);    // 删除多个歌手（需要传入 ids 的字符串形式）

}
