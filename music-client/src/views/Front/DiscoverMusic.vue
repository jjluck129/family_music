<template>
  <div class="discover-music">
    <!-- 特色推荐模块 -->
    <div class="featured-section">
      <h2 class="section-title">特色推荐</h2>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in featuredItems" :key="item.id">
          <el-card class="featured-card" @click="goToDetail(item)">
            <img :src="item.image" class="featured-image" />
            <div class="card-info">
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 专辑墙模块 -->
    <div class="album-wall">
      <h2 class="section-title">热门专辑</h2>
      <el-row :gutter="15">
        <el-col :span="4" v-for="album in albums" :key="album.albumName">
          <div class="album-item">
            <img :src="album.picUrl" class="album-cover" />
            <div class="album-title">{{ album.albumName }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "DiscoverMusic",
  data() {
    return {
      // 特色推荐数据
      featuredItems: [
        {
          id: 1,
          title: "今日推荐歌单",
          description: "根据你的喜好量身推荐",
          image: "",
        },
        {
          id: 2,
          title: "热门电台",
          description: "收听热门主播的电台节目",
          image: "",
        },
        {
          id: 3,
          title: "新歌速递",
          description: "最新歌曲抢先听",
          image: "",
        },
      ],

      // 专辑墙数据
      albums: [],
    };
  },
  mounted(){
    this.loadRandomImages();
    this.loadAllAlbum();
  },
  methods: {
    loadRandomImages(){
      this.featuredItems.forEach(item => {
        // 使用 Math.random() 保证每次刷新时图片不同，同时可加随机参数避免缓存
        item.image = `https://picsum.photos/300/200?random=${Math.floor(Math.random() * 1000)}`;
      });
    },
    loadAllAlbum(){
      this.$request.get('/songs/albums').then(res =>{
          console.log(res)
          if(res.code === '200'){
              const allAlbums = res.data
              const shuffled = allAlbums.sort(() => Math.random() - 0.5);
              this.albums = shuffled.slice(0, 12);
          }
      })
    }, 
    goToDetail(item) {
      this.$message({ type: "info", message: `你点击了${item.title}` });
    },
  },
};
</script>

<style scoped>
.discover-music {
  padding: 20px;
}

/* 特色推荐模块样式 */
.featured-section {
  margin-bottom: 40px;
}
.section-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}
.featured-card {
  cursor: pointer;
  transition: transform 0.3s;
}
.featured-card:hover {
  transform: translateY(-5px);
}
.featured-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
}
.card-info {
  padding: 14px;
  text-align: center;
}
.card-info h3 {
  margin: 10px 0 5px;
  font-size: 18px;
}
.card-info p {
  font-size: 14px;
  color: #777;
}

/* 专辑墙样式 */
.album-wall {
  margin-top: 40px;
}
.album-item {
  text-align: center;
  margin-bottom: 20px;
}
.album-cover {
  width: 100%;
  height: 100%; /* 保证宽高一致 */
  max-width: 150px; /* 限制最大宽度 */
  max-height: 150px; /* 限制最大高度 */
  border-radius: 8px;
  object-fit: cover;
  transition: transform 0.3s;
  margin: 0 auto;
  }
.album-cover:hover {
  transform: scale(1.05);
}
.album-title {
  margin-top: 10px;
  font-size: 14px;
  color: #555;
}
</style>
