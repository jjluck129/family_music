package com.example.music_server.mapper;

import com.example.music_server.entity.Singer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SingerMapper {

    // 获取所有歌手
    @Select("SELECT * FROM singer")
    List<Singer> getAllSingers();

    // 根据ID获取歌手
    @Select("SELECT * FROM singer WHERE id = #{id}")
    Singer getSingerById(Integer id);

    // 添加新歌手
    @Insert("INSERT INTO singer(name, gender, imgUrl, birth, location, description) " +
            "VALUES(#{name}, #{gender}, #{imgUrl}, #{birth}, #{location}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSinger(Singer singer);

    // 更新歌手信息
    @Update("UPDATE singer SET name=#{name}, gender=#{gender}, imgurl=#{imgUrl}, " +
            "birth=#{birth}, location=#{location}, description=#{description} WHERE id=#{id}")
    int updateSinger(Singer singer);

    @Update("UPDATE singer SET imgurl=#{imgUrl} WHERE id=#{id}")
    int updateSingerImg(Integer id, String imgUrl);
    // 删除歌手
    @Delete("DELETE FROM singer WHERE id = #{id}")
    int deleteSingerById(Integer id);

    //根据歌手id查询信息
    @Select("SELECT * FROM singer WHERE id = #{id}")
    Singer findById(Integer id);

//    @Delete({
//            "DELETE FROM singer WHERE id IN",
//            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
//            "#{id}",
//            "</foreach>"
//    })
//    int deleteSingersByIds(@Param("ids") List<Integer> ids);
    @Delete("DELETE FROM singer WHERE id IN (#{ids})")
    int deleteSingersByIds(@Param("ids") String ids); // 注意需要传入 ids 的字符串形式



}
