package com.example.music_server.service.impl;

import com.example.music_server.entity.Singer;
import com.example.music_server.mapper.SingerMapper;
import com.example.music_server.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SingerServiceImpl implements SingerService {

    @Autowired
    private SingerMapper singerMapper;

    @Override
    public List<Singer> getAllSingers() {
        return singerMapper.getAllSingers();
    }

    @Override
    public Singer getSingerById(Integer id) {
        return singerMapper.getSingerById(id);
    }

    @Override
    public int addSinger(Singer singer) {
        return singerMapper.insertSinger(singer);
    }

    @Override
    public int updateSinger(Singer singer) {
        return singerMapper.updateSinger(singer);
    }

    @Override
    public int updateSingerImg(Integer id,String imgUrl){ return singerMapper.updateSingerImg(id,imgUrl);}

    @Override
    public int deleteSingerById(Integer id) {
        return singerMapper.deleteSingerById(id);
    }

    @Override
    public int deleteSingersByIds(List<Integer> ids) {
        String idString = ids.stream()
                .map(String::valueOf) // 将每个 Integer 转换为 String
                .collect(Collectors.joining(",")); // 用逗号连接起来
        return singerMapper.deleteSingersByIds(idString); // 调用 Mapper 层的方法
    }
}
