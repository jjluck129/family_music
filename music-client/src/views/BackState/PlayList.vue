<template>
    <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/songlist' }">歌单信息</el-breadcrumb-item>
        <el-breadcrumb-item>
        <a href="/platlist">歌单介绍</a>
        </el-breadcrumb-item>
    </el-breadcrumb>
    <div class="container">
      <!-- 操作工具栏 -->
      <div class="handle-box">
        <el-button @click="deleteSelected" :disabled="multipleSelection.length === 0" size="mini">批量删除</el-button>
        <el-input v-model="searchWord" placeholder="筛选关键词" clearable></el-input>
        <el-button type="primary" @click="addSongDialogVisible = true">添加歌曲</el-button>
      </div>
  
      <!-- 歌曲列表表格 -->
      <el-table
        :data="filteredData"
        border
        size="small"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column label="ID" prop="id" width="60" align="center"></el-table-column>
        <el-table-column label="歌手-歌曲" prop="songName" align="left">
            <template v-slot="scope">
                {{ scope.row.singerName }} - {{ scope.row.songName }}
            </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template v-slot="scope">
            <el-button type="danger" @click="deleteSong(scope.row.id)" size="mini">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 添加歌曲对话框 -->
      <el-dialog title="添加歌曲" v-model="addSongDialogVisible">
        <el-form label-width="80px" :model="addSongForm">
          <el-form-item label="歌曲">
            <el-input v-model="addSongForm.songName"></el-input>
          </el-form-item>
        </el-form>
        <span class="dialog-footer">
          <el-button @click="addSongDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addSong">确定</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        searchWord: '',
        multipleSelection: [],
        addSongDialogVisible: false,
        playlistId:'',
        addSongForm: {
          songName: ''
        },
        songs: [] // 歌曲数据
      };
    },
    computed: {
      filteredData() {
        // 根据搜索关键词过滤数据
        if (this.searchWord) {
          return this.songs.filter((song) =>
            song.songName.includes(this.searchWord)
          );
        }
        return this.songs;
      }
    },
    mounted() {
      this.playlistId = this.$route.query.playlistId;
      this.loadSongs();
    },
    methods: {
      // 获取歌曲数据
      loadSongs() {
        this.$request.get(`/playlist/${this.playlistId}/songs`).then((response) => {
          this.songs = response.data;
          console.log(this.songs)
        }).catch((error) => {
          console.error('获取歌曲数据失败', error);
        });
      },
      // 批量删除歌曲
      deleteSelected() {
        const ids = this.multipleSelection.map(item => item.id);
        this.$request.delete(`/playlist/${this.playlistId}/songs/deleteBatch`, { data: ids }).then(() => {
          this.loadSongs();
          this.multipleSelection = [];
        }).catch((error) => {
          console.error('批量删除歌曲失败', error);
        });
      },
      // 删除单首歌曲
      deleteSong(id) {
        this.$request.delete(`/playlist/${this.playlistId}/songs/delete/${id}`).then(() => {
          this.$message.success('歌曲删除成功');
          this.loadSongs();
        }).catch((error) => {
          console.error('删除歌曲失败', error);
          this.$message.error('删除歌曲失败，请稍后重试');
        });
      },
      // 添加歌曲
      addSong() {
        console.log(this.playlistId)
        this.$request.post(`/playlist/${this.playlistId}/songs/add`, this.addSongForm).then(() => {
          this.loadSongs();
          this.addSongDialogVisible = false;
          this.addSongForm = { id: '', songName: '' }; // 清空表单
        }).catch((error) => {
          console.error('添加歌曲失败', error);
        });
      },
      // 处理多选
      handleSelectionChange(val) {
        this.multipleSelection = val;
      }
    }
  };
  </script>
  
  <style scoped>
  .container {
    padding: 20px;
  }
  .handle-box {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 10px;
  }
  </style>
  