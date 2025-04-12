<template>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>
        <router-link to="/homepage/albumlist">专辑列表</router-link>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="songs">{{ songs.albumName }}</el-breadcrumb-item>
    </el-breadcrumb>
  
    <div class="album-detail">
      <div v-if="!songs" class="loading">
        <el-spin size="large"></el-spin>
        <p>加载中...</p>
      </div>
      <div v-if="songs" class="album-info">
        <div class="album-avatar">
          <img :src="picurl" alt="歌手头像" />
        </div>
        <!-- <div class="album-details">
          <p><strong>姓名:</strong> {{ singer?.name }}</p>
          <p><strong>性别:</strong> {{ singer?.gender }}</p>
          <p><strong>地区:</strong> {{ singer?.location }}</p>
          <p><strong>生日:</strong> {{ new Date(singer?.birth).getMonth() + 1 }}月{{ new Date(singer?.birth).getDate() }}日</p>
          <p><strong>简介:</strong> {{ singer?.description }}</p>
        </div> -->
      </div>
  
      <div v-if="songs" class="songs">
        <h3>专辑歌曲</h3>
        <ul>
          <li
            v-for="song in songs"
            :key="song.id"
            class="song-item-container"
            @mouseenter="hoveredSongId = song.id"
            @mouseleave="hoveredSongId = null" 
          >
            <div class="song-item">
              <span class="song-name">{{ song?.songName }}</span>
  
              <!-- 悬浮显示的操作按钮 -->
              <div v-if="hoveredSongId === song.id" class="song-icons">
                <i class="fa fa-play-circle icon-btn" @click="playSong(song)"></i>
  
                <!-- 添加按钮，使用 el-dropdown -->
                <el-dropdown  @command="handleAddCommand(song)" placement="bottom">
                  <i class="fa fa-plus-circle icon-btn"></i>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="playlist" :data-song="song">添加到歌曲列表</el-dropdown-item>
                      <el-dropdown-item command="favorites" :data-song="song">添加到收藏</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                  
                </el-dropdown>
  
                <!-- 分享按钮，使用 el-dropdown -->
                <el-dropdown trigger="click" @command="handleShareCommand" placement="bottom-end">
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="share1" :data-song="song">分享选项 1</el-dropdown-item>
                      <el-dropdown-item command="share2" :data-song="song">分享选项 2</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                  <i class="fa fa-share-alt icon-btn"></i>
                </el-dropdown>
              </div>
  
              <span class="song-album">{{ song?.albumName }}</span>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'AlbumDetail',
    data() {
      return {
        picurl:'',
        songs: [],
        hoveredSongId: null,
      };
    },
    mounted() {
      const albumName = this.$route.params.albumName;
      console.log(albumName);
      console.log(this.$route.params);
      this.loadAlbumDetail(albumName);
    },
    methods: {
        loadAlbumDetail(albumName) {
        this.$request.get(`/songs/albumtosong/${albumName}`).then((response) => {
          this.songs = response.data;
            this.picurl=this.songs[0].picUrl
          console.log(this.songs[0].picUrl);
        });
      },
      playSong(song) {
        console.log('播放歌曲:', song);
        this.$store.dispatch('playSong', song);
      },
      handleAddCommand(command, event) {
        const song = event.target.dataset.song;
        if (command === 'playlist') {
          this.addToPlaylist(song);
        } else if (command === 'favorites') {
          this.addToFavorites(song);
        }
      },
      handleShareCommand(command, event) {
        const song = event.target.dataset.song;
        console.log(`分享歌曲 ${song}，选项：${command}`);
      },
      addToPlaylist(song) {
        this.$store.commit('addToPlaylist', song);
        console.log('添加到歌曲列表:', song);
      },
      addToFavorites(song) {
        console.log('添加到收藏:', song);
      },
    },
  };
  </script>
  
  <style scoped>
  .album-detail {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
  }
  
  .album-info {
    display: flex;
    justify-content: flex-start;
    width: 80%;
    margin-bottom: 40px;
  }
  
  .album-avatar img {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #ddd;
  }
  
  .album-details {
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
  
  /* 包裹歌曲行和按钮的容器 */
  .song-item-container {
    padding: 10px 0;
    display: flex;
    align-items: center;
    position: relative;
  }
  
  .song-item {
    display: flex;
    width: 100%;
    align-items: center;
  }
  
  /* 歌名左对齐 */
  .song-name {
    flex: 1;
    text-align: left;
  }
  
  /* 专辑名样式，左对齐 */
  .song-album {
    text-align: left;
    flex: 1;
    padding-left: 10px;
  }
  
  /* 悬停出现的图标按钮 */
  .song-icons {
    display: flex;
    gap: 10px;
    position: absolute;
    right: 100px;
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
  