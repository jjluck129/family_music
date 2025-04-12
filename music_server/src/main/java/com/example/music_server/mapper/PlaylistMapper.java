package com.example.music_server.mapper;

import com.example.music_server.entity.Playlist;
import com.example.music_server.entity.PlaylistSong;
import com.example.music_server.entity.Song;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlaylistMapper {

//    // 获取歌单列表
//    @Select("SELECT * FROM playlist")
//    @Results({
//            @Result(property = "imgUrl", column = "img_url"),
//    })
//    List<Playlist> getAllPlaylist();
//
//
//    // 插入一首新歌单
//    @Insert("INSERT INTO playlist (title,introduction,style)" +
//            "VALUES (#{title}, #{introduction}, #{style})")
//    void addPlaylist(Playlist playlist);
//
//    // 删除歌单
//    @Delete("DELETE FROM playlist WHERE id = #{id}")
//    void deletePlaylist(@Param("id") Integer id);
//
//    // 更新歌单信息
//    @Update("UPDATE playlist SET title = #{title}, introduction = #{introduction}, style = #{style}" +
//            "WHERE id = #{id}")
//    void updatePlaylist(Playlist playlist);
//
//    // 根据歌单ID查询
//    @Select("SELECT * FROM playlist WHERE id = #{playlistId}")
//    Playlist getPlaylisyById(@Param("playlistId") Integer playlistId);
//
//    //插入图片地址URL
//    @Update("UPDATE playlist SET img_url = #{imgUrl} WHERE id = #{id}")
//    int updatePlaylistPic(@Param("id") Integer id, @Param("imgUrl") String imgUrl);
//
//
//    /**
//     * 歌单内容
//     */
//    // 根据歌单ID获取对应的歌曲ID列表
//    @Select("SELECT * FROM playlist_song WHERE playlist_id = #{playlistId}")
//    @Results({
//            @Result(property = "songId", column = "song_id"),
//            @Result(property = "playlistId", column = "playlist_id"),
//    })
//    List<PlaylistSong> getSongsByPlaylistId(Integer playlistId);
//
//    // 添加歌单与歌曲的映射关系
//    @Insert("INSERT INTO playlist_song (playlist_id, song_id) VALUES (#{playlistId}, #{songId})")
//    void addPlaylistSong(PlaylistSong playlistSong);
//
//    // 根据歌单ID删除所有关联歌曲
//    @Delete("DELETE FROM playlist_song WHERE playlist_id = #{playlistId}")
//    void deleteSongsByPlaylistId(Integer playlistId);
//
//    // 根据歌单ID和歌曲ID删除单条映射记录
//    @Delete("DELETE FROM playlist_song WHERE playlist_id = #{playlistId} AND song_id = #{songId}")
//    @Results({
//            @Result(property = "songId", column = "song_id"),
//            @Result(property = "playlistId", column = "playlist_id"),
//    })
//    void deleteSongFromPlaylist(@Param("playlistId") Integer playlistId, @Param("songId") Integer songId);
//
//    @Select("select id from songs where song_name = #{songName}")
//    Integer getSongIdBySongName(String songName);
// 获取歌单列表
List<Playlist> getAllPlaylist();

    // 插入一首新歌单
    void addPlaylist(Playlist playlist);

    // 删除歌单
    void deletePlaylist(Integer id);

    // 更新歌单信息
    void updatePlaylist(Playlist playlist);

    // 根据歌单ID查询
    Playlist getPlaylisyById(Integer playlistId);

    //插入图片地址URL
    int updatePlaylistPic(Integer id, String imgUrl);

    // 根据歌单ID获取对应的歌曲ID列表
    List<PlaylistSong> getSongsByPlaylistId(Integer playlistId);

    // 添加歌单与歌曲的映射关系
    void addPlaylistSong(PlaylistSong playlistSong);

    // 根据歌单ID删除所有关联歌曲
    void deleteSongsByPlaylistId(Integer playlistId);

    // 根据歌单ID和歌曲ID删除单条映射记录
    void deleteSongFromPlaylist(Integer playlistId, Integer songId);

    // 根据歌曲名称获取歌曲ID
    Integer getSongIdBySongName(String songName);
}