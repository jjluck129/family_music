<template>
    <div class="album-list">
        <el-row :gutter="20" class="album-container">
        <!-- 遍历歌手数据并展示 -->
        <el-col :span="4" v-for="album in albums" :key="album.albumName" class="album-item" @click="goToAlbumDetail(album.albumName)">
            <el-card shadow="hover">
            <img :src="album.picUrl" alt="歌手图片" class="album-image" />
            <div class="singer-info">
                <h3 class="album-name">{{ album.albumName }}</h3>
                <p class="album-genre"></p>
            </div>
            </el-card>
        </el-col>
        </el-row>
        <router-view></router-view>
    </div>
</template>
  
<script>
export default {
    name: 'AlbumList',
    data(){
        return{
            albums:[],
        }
    },
    mounted(){
        this.loadAllAlbums();
    },
    methods: {
        loadAllAlbums(){
            this.$request.get('/songs/albums').then(response => {
            this.albums = response.data;
            })
        },
        goToAlbumDetail(name) {
            console.log(name);
            this.$router.push({ name: 'AlbumDetail', params: { albumName: name } });
      },
    }
  };
  </script>
  
  <style scoped>
  .album-list {
    padding: 20px;
  }
  
  .album-container {
    margin-top: 20px;
  }
  
  .album-item {
    margin-bottom: 20px;
    cursor:pointer;
  }
  
  .album-image {
    width: 100%;
    height: auto;
    border-radius: 8px;
  }
  
  .singer-info {
    text-align: center;
    margin-top: 10px;
    
  }
  
  .album-name {
    font-size: 18px;
    font-weight: bold;
    margin: 0;
  }
  
  .album-genre {
    font-size: 14px;
    color: #888;
  }
  </style>
  