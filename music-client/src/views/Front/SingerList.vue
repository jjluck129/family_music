<template>
    <div class="singer-list">
      <el-row :gutter="20" class="singer-container">
        <!-- 遍历歌手数据并展示 -->
        <el-col :span="6" v-for="singer in singers" :key="singer.id" class="singer-item" @click="goToSingerDetail(singer.id)">
          <el-card shadow="hover">
            <img :src="singer.imgUrl" alt="歌手图片" class="singer-image" />
            <div class="singer-info">
              <h3 class="singer-name">{{ singer.name }}</h3>
              <p class="singer-genre">{{ singer.genger }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <router-view></router-view>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'SingerList',
    data(){
      return{
        singers:[],
      }
    },
    mounted(){
      this.loadAllSingers();
    },
    methods: {
      loadAllSingers(){
        axios.get(`/singers`).then(response => {
          this.singers = response.data;
        })
      },
      goToSingerDetail(id) {
        this.$router.push({ name: 'SingerDetail', params: { singerId: id } });
      },
    }
  };
  </script>
  
  <style scoped>
  .singer-list {
    padding: 20px;
  }
  
  .singer-container {
    margin-top: 20px;
  }
  
  .singer-item {
    margin-bottom: 20px;
    cursor:pointer;
  }
  
  .singer-image {
    width: 100%;
    height: auto;
    border-radius: 8px;
  }
  
  .singer-info {
    text-align: center;
    margin-top: 10px;
    
  }
  
  .singer-name {
    font-size: 18px;
    font-weight: bold;
    margin: 0;
  }
  
  .singer-genre {
    font-size: 14px;
    color: #888;
  }
  </style>
  