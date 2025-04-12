<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item>
      <router-link to="/homepage/singerlist">歌手列表</router-link>
    </el-breadcrumb-item>
    <el-breadcrumb-item v-if="singer">{{ singer.name }}</el-breadcrumb-item>
  </el-breadcrumb>

  <div class="singer-detail">
    <div v-if="!singer" class="loading">
      <el-spin size="large"></el-spin>
      <p>加载中...</p>
    </div>
    <div v-if="singer" class="singer-info">
      <div class="singer-avatar">
        <img :src="singer.imgUrl" alt="歌手头像" />
      </div>
      <div class="singer-details">
        <p><strong>姓名:</strong> {{ singer.name }}</p>
        <p><strong>性别:</strong> {{ singer.gender }}</p>
        <p><strong>地区:</strong> {{ singer.location }}</p>
        <p>
          <strong>生日:</strong>
          {{ new Date(singer.birth).getMonth() + 1 }}月{{ new Date(singer.birth).getDate() }}日
        </p>
        <p><strong>简介:</strong> {{ singer.description }}</p>
      </div>
    </div>

    <div v-if="singer" class="songs">
      <h3>代表歌曲</h3>
      <ul>
        <li v-for="song in songs" :key="song.id" class="song-item-container">
          <div class="song-item">
            <span class="song-name">{{ song.songName }}</span>
            <div class="song-icons">
              <i class="fa fa-play-circle icon-btn" @click="playSong(song)"></i>
              <el-dropdown trigger="click" @command="(cmd) => handleAddCommand(song, cmd)" placement="bottom">
                <i class="fa fa-plus-circle icon-btn"></i>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="playlist">添加到歌曲列表</el-dropdown-item>
                    <el-dropdown-item command="favorites">添加到收藏</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-dropdown trigger="click" @command="(cmd) => handleShareCommand(song, cmd)" placement="bottom-end">
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="share1">分享选项 1</el-dropdown-item>
                    <el-dropdown-item command="share2">分享选项 2</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
                <i class="fa fa-share-alt icon-btn"></i>
              </el-dropdown>
            </div>
            <span class="song-album">{{ song.albumName }}</span>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SingerDetail',
  data() {
    return {
      singer: null,
      songs: [],
    };
  },
  mounted() {
    const singerId = this.$route.params.singerId;
    this.loadSingerDetail(singerId);
  },
  methods: {
    loadSingerDetail(id) {
      this.$request.get(`/singers/${id}`).then((response) => {
        this.singer = response.data;
      });
      this.$request.get(`/songs/list/${id}`).then((response) => {
        this.songs = response.data;
      });
    },
    playSong(song) {
      this.$store.dispatch('playSong', song);
    },
    // 直接传入 song 对象和命令参数
    handleAddCommand(song, command) {
      if (command === 'playlist') {
        this.addToPlaylist(song);
      } else if (command === 'favorites') {
        this.addToFavorites(song);
      }
    },
    handleShareCommand(song, command) {
      console.log(`分享歌曲 ${song.songName}，选项：${command}`);
    },
    addToPlaylist(song) {
      this.$store.commit('addToPlaylist', song);
      console.log('添加到歌曲列表:', song);
    },
    addToFavorites(song) {
      console.log('添加到收藏:', song);
      const userId = JSON.parse(localStorage.getItem("family-user") || "").id;
      this.$request
        .get(`/songlike/check`, { params: { userId: userId, songId: song.id } })
        .then(response => {
            if (response.code === "200") {
              this.$message.info("你已添加至喜欢");
            } else {
            const likeData = { songId: song.id, userId: userId };
            this.$request
                .post("/songlike/add", likeData)
                .then(res => {
                    console.log(res)
                if (res.code === "200") {
                    this.$message.success("添加喜欢成功");
                } else {
                    this.$message.error(res.data.msg || "添加喜欢失败");
                }
                });
            }
        })
    },
  },
};
</script>

<style scoped>
.singer-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.singer-info {
  display: flex;
  justify-content: flex-start;
  width: 80%;
  margin-bottom: 40px;
}

.singer-avatar img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ddd;
}

.singer-details {
  flex: 1;
  margin-left: 20px;
  font-size: 16px;
  text-align: left;
}

.songs {
  width: 80%;
  margin-top: 40px;
}

.songs ul {
  list-style-type: none;
  padding: 0;
}

/* 歌曲项容器 */
.song-item-container {
  padding: 10px 0;
  position: relative;
}

/* 歌曲项内容 */
.song-item {
  display: flex;
  align-items: center;
}

/* 歌名 */
.song-name {
  flex: 1;
  text-align: left;
}

/* 专辑名 */
.song-album {
  flex: 1;
  padding-left: 10px;
  text-align: left;
}

/* 操作按钮区域：始终存在，只是默认透明 */
.song-icons {
  display: flex;
  gap: 10px;
  position: absolute;
  right: 100px;
  opacity: 0;
  transition: opacity 0.3s;
}

/* 鼠标悬停整个歌曲项时显示操作按钮 */
.song-item-container:hover .song-icons {
  opacity: 1;
}

.icon-btn {
  font-size: 20px;
  color: #409eff;
  cursor: pointer;
  transition: color 0.3s;
}

.icon-btn:hover {
  color: #66b1ff;
}

.loading {
  text-align: center;
  padding: 20px;
}
</style>
