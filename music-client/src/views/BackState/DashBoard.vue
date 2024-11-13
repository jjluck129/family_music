<template>
  <div>
    <h2>仪表盘</h2>
    <p>这里是仪表盘的内容。</p>
    <h3>歌手列表</h3>
    <ul>
      <li v-for="singer in singers" :key="singer.id">{{ singer.name }}</li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      singers: []  // 用于存储歌手列表
    };
  },
  mounted() {
    // 组件加载时发送请求
    axios.get('/api/singers')
      .then(response => {
        this.singers = response.data;  // 更新 singers 数据
        console.log('Singers:', this.singers);
      })
      .catch(error => {
        console.error('Error fetching singers:', error);
      });
  }
};
</script>

<style scoped>
/* 您的样式可以在这里添加 */
</style>
