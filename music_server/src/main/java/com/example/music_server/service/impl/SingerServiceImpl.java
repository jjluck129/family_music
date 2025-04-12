package com.example.music_server.service.impl;

import com.example.music_server.entity.Singer;
import com.example.music_server.mapper.SingerMapper;
import com.example.music_server.service.SingerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SingerServiceImpl implements SingerService {
    private static final Logger log = LoggerFactory.getLogger(SingerServiceImpl.class);
    @Autowired
    private SingerMapper singerMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String SINGER_CACHE_PREFIX = "singer:";  // Redis Key 前缀

    @Override
    public List<Singer> getAllSingers() {
        return singerMapper.getAllSingers();
    }

//    @Override
//    public Singer getSingerById(Integer id) {
//        return singerMapper.getSingerById(id);
//    }
    @Override
    public Singer getSingerById(Integer id) {
        String cacheKey = SINGER_CACHE_PREFIX + id;

        // 先查询 Redis
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Singer cachedSinger = (Singer) ops.get(cacheKey);
        if (cachedSinger != null) {
            log.info("✅ 从 Redis 缓存获取歌手信息: {}", id);
            return cachedSinger;
        }

        // Redis 缓存未命中，查询数据库
        log.info("❌ Redis 缓存未命中，查询数据库: {}", id);
        Singer singer = singerMapper.getSingerById(id);
        if (singer != null) {
            // 将数据存入 Redis，设置 10 分钟过期
            ops.set(cacheKey, singer, 10, TimeUnit.MINUTES);
            log.info("🔄 歌手信息存入 Redis 缓存: {}", id);
        }
        return singer;
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
