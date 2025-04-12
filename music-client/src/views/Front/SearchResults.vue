<template>
    <div class="search-results">
      <h2>搜索结果：{{ keyword }}</h2>
      <el-table :data="songs" v-loading="loading" style="width: 100%">
        <el-table-column prop="songName" label="歌曲名" />
        <el-table-column prop="singerName" label="歌手" />
        <el-table-column prop="albumName" label="专辑" />
        <el-table-column label="操作" align="center" width="200">
            <template #default="scope">
                <i 
                    class="fa fa-play-circle icon-btn play-btn" 
                    @click="playSong(scope.row)" 
                    title="播放"
                ></i>
                <el-dropdown @command="handleAddCommand" placement="bottom">
                    <i 
                    class="fa fa-plus-circle icon-btn add-btn" 
                    title="添加"
                    ></i>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item :command="['playlist', scope.row]">添加到播放列表</el-dropdown-item>
                            <el-dropdown-item :command="['favorite', scope.row]">添加到喜欢</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
                <i 
                    class="fa fa-share-alt icon-btn share-btn" 
                    @click="shareSong(scope.row)" 
                    title="分享"
                ></i>
            </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
  
<script>
export default {
name: "SearchResults",
data() {
    return {
    keyword: "", // 搜索关键词
    songs: [], // 搜索结果
    loading: false, // 加载状态
    };
},
watch: {
    // 当路由变化时，重新搜索
    "$route.query.keyword": "loadSearchResults",
},
created() {
    this.loadSearchResults(); // 初始化时获取搜索结果
},
methods: {
    loadSearchResults() {
        this.keyword = this.$route.query.keyword || ""; // 从路由中获取搜索关键词
        if (!this.keyword) return;

        this.loading = true;
        this.$request
            .get(`/songs/search`, { params: { keyword: this.keyword } }) // 调用后端 API
            .then((response) => {
                console.log(response);
                this.songs = response.data; // 假设后端返回的是一个歌曲列表
            })
            .catch((error) => {
                this.$message.error("搜索失败：" + (error.response?.data || error.message));
            })
            .finally(() => {
                this.loading = false;
            });
        },
    playSong(song) {
        this.$store.dispatch('playSong', song);
    },
    handleAddCommand([command,song]){
        console.log(song);
        console.log(command);
        const user = JSON.parse(localStorage.getItem("family-user") || "null");
        if (!user) {
        this.$message.warning("请先登录");
        return;
        }
        const userId = user.id;
            if (command === "playlist") {
                this.$store.commit("addToPlaylist", song);
                this.$message.success(`已将歌曲 "${song.songName}" 添加到播放列表`);
            } else if (command === "favorite") {
                // 调用后端接口处理收藏功能
                this.$request
                    .get(`/songlike/check`, { params: { userId: userId, songId: song.id } })
                    .then(response => {
                        if (response.code === "200") {
                            this.$message.info("你已添加至喜欢");
                        } else {
                        const likeData = { songId: song.id, userId: userId };
                        this.$request
                            .post("/songlike/add", likeData)
                            .then(resp => {
                                console.log(resp)
                            if (resp.code === "200") {
                                this.$message.success("喜欢成功");
                            } else {
                                this.$message.error(resp.msg || "添加喜欢失败");
                            }
                            })
                            .catch(err => {
                                this.$message.error("添加喜欢出错");
                                console.error(err);
                            });
                        }
                    })
                    .catch(error => {
                        this.$message.error("检查喜欢状态出错");
                        console.error(error);
                    });
            }
        
    }
},
};
</script>

<style scoped>
.search-results {
    padding: 20px;
}
.icon-btn {
    font-size: 18px;
    color: #606266;
    cursor: pointer;
    margin-right: 10px;
    transition: color 0.3s;
}

.icon-btn:hover {
    color: #409eff; /* 鼠标悬停高亮色 */
}

.play-btn {
    color: #67c23a;
}

.add-btn {
    color: #e6a23c;
}

.share-btn {
    color: #f56c6c;
}
</style>
  