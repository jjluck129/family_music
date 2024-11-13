package com.example.music_server.entity;

import java.io.Serializable;

public class Song implements Serializable {
    private Integer id;          // 歌曲ID
    private Integer singerId;    // 歌手ID
    private String singerName; // 歌手名
    private String songName;  // 歌曲名
    private String albumName; // 专辑名
    private String lyrics;    // 歌词
    private String picUrl;    // 歌手图片地址
    private String mp3Url;     // MP3文件路径

    // Getters and Setters
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getSingerId() { return singerId; }

    public void setSingerId(Integer singerId) { this.singerId = singerId; }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", singerId=" + singerId +
                ", singerName='" + singerName + '\'' +
                ", songName='" + songName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", mp3Path='" + mp3Url + '\'' +
                '}';
    }
}