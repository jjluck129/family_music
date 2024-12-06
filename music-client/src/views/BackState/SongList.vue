<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/songlist' }">歌单信息</el-breadcrumb-item>
  </el-breadcrumb>
  <div class="container">
    <!-- 操作工具栏 -->
    <div class="handle-box">
      <el-button @click="deleteAll" :disabled="multipleSelection.length === 0" size="mini">批量删除</el-button>
      <el-input v-model="searchWord" placeholder="筛选关键词" clearable></el-input>
      <el-button type="primary" @click="centerDialogVisible = true">添加歌单</el-button>
    </div>

    <!-- 歌曲列表表格 -->
    <el-table
      :data="filteredData"
      border
      size="small"
      height="550px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center"></el-table-column>
      <el-table-column label="ID" prop="id" width="60" align="center"></el-table-column>
      <el-table-column label="歌单图片" width="130" align="center">
        <template v-slot="scope">
          <img :src="scope.row.imgUrl" class="song-cover" />
          <el-upload
            action="http://localhost:9090/upload"
            :data="{type: 'playlistImg'}"
            :show-file-list="false"
            :on-success="(response) => handleImgSuccess(response, scope.row.id)"
            :before-upload="beforeImgUpload"
          >
            <el-button size="mini">更新图片</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="200"></el-table-column>
      <el-table-column label="简介">
        <template v-slot="scope">
          <p class="song-intro">{{ scope.row.introduction }}</p>
        </template>
      </el-table-column>
      <el-table-column label="风格" prop="style" width="100"></el-table-column>
      <el-table-column label="内容" width="120" align="center">
        <template v-slot="scope">
          <el-button @click="goContentPage(scope.row.id)" size="mini">内容</el-button>
        </template>
      </el-table-column>
      <el-table-column label="评论" width="120" align="center">
        <template v-slot="scope">
          <el-button @click="goCommentPage(scope.row.id)" size="mini">评论</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template v-slot="scope">
          <el-button @click="editRow(scope.row)" size="mini">编辑</el-button>
          <el-button type="danger" @click="deleteRow(scope.row.id)" size="mini">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="pagination"
      background
      layout="total, prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="filteredData.length"
      @current-change="handleCurrentChange"
    ></el-pagination>

    <!-- 添加歌单对话框 -->
    <el-dialog title="添加歌单" v-model="centerDialogVisible">
      <el-form label-width="80px" :model="registerForm">
        <el-form-item label="歌单名" prop="title">
          <el-input v-model="registerForm.title"></el-input>
        </el-form-item>
        <el-form-item label="歌单介绍" prop="introduction">
          <el-input type="textarea" v-model="registerForm.introduction"></el-input>
        </el-form-item>
        <el-form-item label="风格" prop="style">
          <el-input v-model="registerForm.style"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addSongList">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑歌单" v-model="editVisible">
      <el-form :model="editForm">
        <el-form-item label="标题">
          <el-input v-model="editForm.title"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input type="textarea" v-model="editForm.introduction"></el-input>
        </el-form-item>
        <el-form-item label="风格">
          <el-input v-model="editForm.style"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveEdit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchWord: '',
      currentPage: 1,
      pageSize: 5,
      multipleSelection: [],
      playlists:[],
      centerDialogVisible: false,
      editVisible: false,
      registerForm: {
        title: '',
        introduction: '',
        style: ''
      },
      editForm: {
        title: '',
        introduction: '',
        style: ''
      },
    };
  },
 
  computed: {
    filteredData() {
      console.log(this.playlists)
      if (!Array.isArray(this.playlists)) {
        return []; // 如果 playlists 不是数组，返回空数组
      }
      if (this.searchWord) {
        return this.playlists.filter((item) => {
          return (
            item.title.includes(this.searchWord) ||
            item.introduction.includes(this.searchWord) ||
            item.style.includes(this.searchWord)
          );
        });
      }
      return this.playlists.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    }
  },
  mounted(){
   this.loadPlaylistData();
  },
  methods: {
    loadPlaylistData(){
      this.$request.get('/playlist').then(response =>{
        console.log(response);
        this.playlists = response.data;
      }).catch(error =>{
        console.error("获取歌单失败",error);
        this.$message.error('无法加载歌单数据，请稍后再试');
      })
    },
    // 添加歌单
    addSongList() {
      this.$request.post('/playlist/add', this.registerForm).then(() => {
        this.$message.success('歌单添加成功');
        this.loadPlaylistData();
        this.centerDialogVisible = false;
      }).catch(error => {
        console.error('添加歌单失败', error);
        this.$message.error('添加歌单失败，请检查输入数据');
      });
    },
    // 编辑歌单
    editRow(row) {
      this.editForm = { ...row };
      this.editVisible = true;
    },
    saveEdit() {
      this.$request.put('/playlist/update', this.editForm).then(() => {
        this.$message.success('歌单更新成功');
        this.loadPlaylistData();
        this.editVisible = false;
      }).catch(error => {
        console.error('更新歌单失败', error);
        this.$message.error('更新歌单失败，请稍后再试');
      });
    },
    // 删除单行
    deleteRow(id) {
      this.$confirm('此操作将永久删除该歌单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request
          .delete(`/playlist/delete/${id}`)
          .then(() => {
            this.$message.success('歌单删除成功');
            this.loadPlaylistData();
          })
          .catch((error) => {
            console.error('删除歌单失败', error);
            this.$message.error('删除歌单失败，请稍后再试');
          });
      });
    },

    // 批量删除
    deleteAll() {
      // const ids = this.multipleSelection.map(item => item.id);
      // this.$request.delete(`/playlist/deleteBatch`, { data: ids }).then(() => {
      //   this.loadPlaylistData();
      //   this.multipleSelection = [];
      // }).catch(error => {
      //   console.error('批量删除失败', error);
      // });
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请至少选择一项进行删除');
        return;
      }

      this.$confirm('此操作将永久删除所选歌单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map((item) => item.id);
        this.$request
          .delete('/playlist/deleteBatch', { data: ids })
          .then(() => {
            this.$message.success('批量删除成功');
            this.loadPlaylistData();
            this.multipleSelection = [];
          })
          .catch((error) => {
            console.error('批量删除失败', error);
            this.$message.error('批量删除失败，请稍后再试');
          });
      });
    },
    // 处理选择变化
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 页面变化
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    // 图片更新处理
    handleImgSuccess(response, playlistId) {
      const imageUrl = response.url;
      this.$request.put(`/playlist/updatePlaylistPic/${playlistId}`, { imgUrl: imageUrl })
        .then(() => {
          this.$message.success('图片更新成功');
          this.loadPlaylistData();
        })
        .catch((error) => {
          console.error('图片URL更新失败', error);
          this.$message.error('图片更新失败');
        });
    },
    // beforeImgUpload(file) {
    //   // 图片上传前的处理
    // },
    //内容详情
    goContentPage(playlistId){
      this.$router.push({
        path: '/playlist',
        query: {playlistId}
      });
    },

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
.song-cover {
  width: 80px;
  height: 80px;
  object-fit: cover;
}
.song-intro {
  max-height: 100px;
  overflow-y: auto;
}
.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
