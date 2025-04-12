package com.example.music_server.entity;

public class SongLike {
    private Integer id;
    private Integer songId;
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SongLike{" +
                "id=" + id +
                ", songId=" + songId +
                ", userId=" + userId +
                '}';
    }
}
