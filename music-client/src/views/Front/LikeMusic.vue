<template>
    <div class="liked-songs">
      <h2 class="section-title"><i class="fa-solid fa-heart" style="color: #fb1d04;"></i>喜欢歌曲</h2>
      
      <!-- 显示喜欢的歌曲 -->
      <el-row :gutter="20">
        <el-col :span="6" v-for="song in likedSongs" :key="song.id">
          <el-card class="song-card">
            <img :src="song.picUrl" class="song-cover" />
            <div class="song-info">
              <h3>{{ song.songName }}</h3>
              <p>{{ song.singerName }}</p>
              <div class="button-group">
                <el-button type="danger" size="mini" @click="removeLike(song.id)">取消喜欢</el-button>
                <el-button type="primary" size="mini" @click="shareSong(song)">分享</el-button>
                <el-button type="success" size="mini" @click="playSong(song)">播放</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  export default {
    name: "LikedSongs",
    data() {
      return {
        userId: JSON.parse(localStorage.getItem("family-user") || "{}").id,
        likedSongs: [],
      };
    },
    mounted(){
      this.loadLikedSongs();
    },
    methods: {
      loadLikedSongs(){
        this.$request.get(`/songlike/user/${this.userId}`).then(response => {
          console.log(response.data)
          if (response.code === "200") {
            this.likedSongs = response.data; // 直接使用返回的完整歌曲数据
          } else {
            this.$message.error("获取喜欢的歌曲失败");
          }
        }).catch(() => {
          this.$message.error("获取数据出错");
        });
      },
      // 移除喜欢
      removeLike(songId) {
        this.$request.delete(`/songlike/remove`,{ params: { userId: this.userId,songId } })
            .then(res =>{
              if(res.code ==="200"){
                this.$message.success("删除成功");
                this.likedSongs = this.likedSongs.filter(song => song.id !== songId);
              }
            })
      },
      // 分享功能
      shareSong(song) {
        this.$message({
          type: "info",
          message: `分享了歌曲：${song.title} - ${song.artist}`,
        });
      },
      // 播放歌曲功能
      playSong(song) {
        this.$message({
          type: "success",
          message: `正在播放：${song.title} - ${song.artist}`,
        });
        this.$store.dispatch('playSong', song);
      },
    },
  };
  </script>
  
  <style scoped>
  .liked-songs {
    padding: 20px;
  }
  
  .section-title {
    font-size: 24px;
    margin-bottom: 20px;
    color: #333;
  }
  
  .song-card {
    cursor: pointer;
    transition: transform 0.3s;
    border-radius: 10px;
  }
  .song-card:hover {
    transform: translateY(-5px);
  }
  .song-cover {
    width: 100%;
    height: 180px;
    object-fit: cover;
    border-radius: 10px;
  }
  .song-info {
    padding: 10px;
    text-align: center;
  }
  .song-info h3 {
    margin: 10px 0;
    font-size: 18px;
  }
  .song-info p {
    font-size: 14px;
    color: #777;
  }
  
  .button-group {
    margin-top: 10px;
    display: flex;
    justify-content: space-around;
  }
  
  .el-button {
    width: 80px;
    font-size: 12px;
    margin: 5px;
  }
  </style>
  