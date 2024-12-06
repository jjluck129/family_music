<template>
  <el-breadcrumb class="crumbs" separator="/">
    <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.name" :to="{ path: item.path, query: item.query }">
      {{ item.name }}
    </el-breadcrumb-item>
  </el-breadcrumb>

  <div class="container">
    <div class="handle-box">
      <el-button @click="deleteAll">批量删除</el-button>
      <el-input v-model="searchWord" placeholder="筛选关键词"></el-input>
      <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
    </div>
    <el-table height="550px" border size="small" :data="filteredTableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
      <el-table-column label="歌曲图片" width="120" align="center">
        <template v-slot="scope">
          <el-image
            style="width: 80px; height: 80px"
            :src="scope.row.picUrl ? scope.row.picUrl : ''"
            :alt="scope.row.songName"
            fit="cover"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="歌手-歌曲" align="center">
        <template v-slot="scope">
          {{ scope.row.singerName }} - {{ scope.row.songName }}
        </template>
      </el-table-column>
      <el-table-column label="专辑" prop="albumName" align="center"></el-table-column>
      <el-table-column label="歌词" width="200">
        <template v-slot="scope">
          <p style="height: 100px; overflow: auto; white-space: pre-wrap;">
            {{ scope.row.lyrics }}
          </p>
        </template>
      </el-table-column>
      <el-table-column label="资源更新" width="150" align="center">
        <template v-slot="scope">
          <el-upload :action="uploadImageUrl" :data="{ type: 'songImg' }" :show-file-list="false" :on-success="(res) => handleImageUploadSuccess(res, scope.row.id)" >
            <el-button>更新图片</el-button>
          </el-upload>
          <el-upload :action="uploadMp3Url" :data="{ songId: scope.row.id }" :show-file-list="false" :on-change="(file) => handleFileChange(file, scope.row.id)"  :on-success="handleUploadSuccess" :auto-upload="false">
            <el-button>更新歌曲</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column label="评论" width="90" align="center">
        <el-button @click="goCommentPage(scope.row.id)">评论</el-button>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center" >
        <template v-slot="scope">
          <el-button @click="editRow(scope.row)">编辑</el-button>
          <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- 添加歌曲 -->
  <el-dialog title="添加歌曲" v-model="centerDialogVisible">
    <el-form label-width="80px" :model="registerForm">
      <el-form-item prop="singerName" label="歌手名字">
        <el-input v-model="registerForm.singerName" disabled></el-input> <!-- 歌手名字由父页面传递，不可编辑 -->
      </el-form-item>
      <el-form-item prop="songName" label="歌曲名字">
        <el-input v-model="registerForm.songName"></el-input>
      </el-form-item>
      <el-form-item prop="albumName" label="专辑名字">
        <el-input v-model="registerForm.albumName"></el-input>
      </el-form-item>
      <el-form-item prop="lyrics" label="歌词">
        <el-input type="textarea" v-model="registerForm.lyrics"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSong">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 编辑歌曲 -->
  <el-dialog title="编辑歌曲" v-model="editDialogVisible">
    <el-form label-width="80px" :model="editForm">
      <el-form-item label="歌曲名字" prop="songName">
        <el-input v-model="editForm.songName"></el-input>
      </el-form-item>
      <el-form-item label="专辑名字" prop="albumName">
        <el-input v-model="editForm.albumName"></el-input>
      </el-form-item>
      <el-form-item label="歌词" prop="lyrics">
        <el-input type="textarea" v-model="editForm.lyrics"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="updateSong">保存</el-button>
    </template>
  </el-dialog>

  <!-- 删除确认框 -->
  <el-dialog :delVisible="delVisible" @confirm="confirmDelete" @cancelRow="delVisible = false"></el-dialog>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem("family-user") || "{}"),
      breadcrumbList: [
        { name: '歌手管理', path: '/singerInfo' },
        { name: '歌曲信息' },
      ],
      searchWord: '',
      singerId: null,  // 从父页面传递的歌手ID
      singerName: '',  // 从父页面传递的歌手名字
      tableData: [],
      selectedRows: [],
      centerDialogVisible: false,
      editDialogVisible: false,
      delVisible: false,
      registerForm: {
        singerId: null,
        singerName: '',
        songName: '',
        albumName: '',
        lyrics: '',
      },
      editForm: {
        id: null,
        songName: '',
        albumName: '',
        lyrics: '',
      },
      currentRow: null,
      mp3File: null,  // 存储上传的文件
      uploadMp3Url: 'http://localhost:9090/songs/uploadMp3',  // 后端MP3上传地址
      uploadImageUrl: 'http://localhost:9090/upload', // 上传图片的后端接口地址
    };
  },
  computed: {
    filteredTableData() {
      if (this.searchWord) {
        return this.tableData.filter(row => row.songName.includes(this.searchWord));
      }
      return this.tableData;
    },
  },
  mounted() {
    // 获取父页面传递的歌手ID和名字，并加载对应的歌曲列表
    this.singerId = this.$route.query.singerId;
    this.singerName = this.$route.query.singerName;
    this.registerForm.singerId = this.singerId;
    this.registerForm.singerName = this.singerName;
    this.loadTableData();
  },
  methods: {
    // 加载表格数据
    loadTableData() {
      this.$request.get(`/songs/list/${this.singerId}`).then(response => {
        if(response.code === '200'){
          this.tableData = response.data;
        }else{
          this.$message.error(response.msg || '加载失败');
        }
      });
    },
    // 添加歌曲
    saveSong() {
      this.$request.post('/songs/add', this.registerForm).then(response  => {
        if (response.code === '200') {
          this.$message.success('添加成功');
          this.centerDialogVisible = false;
          this.registerForm.songName = '';
          this.registerForm.albumName = '';
          this.registerForm.lyrics = '';
          this.loadTableData(); // 重新加载表格数据
        }else {
          this.$message.error(response.msg || '添加失败');
        }
      }).catch(error => {
        this.$message.error('添加失败');
        console.error(error);
      });
    },
     // 处理图片上传成功后的逻辑
     handleImageUploadSuccess(response, songId) {
      if (response.url) {
        // 上传成功后调用后端接口更新图片路径
        this.$request.put(`/songs/updateSongPic/${songId}`, { picUrl: response.url })
          .then(res => {
            console.log(res);
            if (res.code === '200') {
              this.$message.success('图片更新成功');
              this.loadTableData();  // 刷新表格数据
            } else {
              this.$message.error(res.msg || '图片更新失败');
            }
          })
          .catch(() => {
            this.$message.error('图片更新失败');
          });
      }
    },
    //上传MP3
    handleFileChange(file,songId) {
      // const uploadMp3Url = 'http://localhost:9090/songs/uploadMp3';
      const mp3File = file.raw; // 确保获取原始文件对象
      const formData = new FormData();
      formData.append("mp3", mp3File);
      formData.append("songId", songId);
      // 发送请求
      this.$request.post('http://localhost:9090/songs/uploadMp3', formData,{
        headers: {
              'token': this.user.token  // 从本地存储或全局状态中获取 token
          }
      })
      .then(response => {
          if (response.url) {
              this.handleUploadSuccess(response);  // 上传成功，处理成功回调
          } else {
              this.$message.error('上传失败');
          }
        })
      .catch(error => {
        console.error("上传失败:", error);
        this.$message.error("上传失败");
      });
    },
    handleUploadSuccess(response) {
      if (response.url) {
        this.$message.success("MP3文件上传成功");
        console.log("MP3 文件可通过以下 URL 访问:", response.url);
        this.loadTableData();  // 刷新表格数据以显示最新的 MP3 信息
      } else {
        this.$message.error("上传成功，但未返回文件 URL");
      }
    },
    // 编辑歌曲
    editRow(row) {
      console.log(row);
      this.editForm = { ...row }; // 复制一份当前行的数据到编辑表单
      this.editDialogVisible = true;
    },
    updateSong() {
      this.$request.put('/songs/update', this.editForm).then(res => {
        if(res.code === '200'){
          this.$message.success('更新成功');
          this.editDialogVisible = false;
          this.loadTableData();
        }else {
          this.$message.error(res.msg || '更新失败');
        }
      });
    },
    // 删除单首歌曲
    deleteRow(id) {
      this.$request.delete(`/songs/delete/${id}`).then(response  => {
        if (response.code === '200') {
          this.$message.success('删除成功');
          this.loadTableData();
        } else {
          this.$message.error(response.msg || '删除失败');
        }
      }).catch(error => {
        this.$message.error('删除失败');
        console.error(error);
      });
    },
    // 批量删除
    deleteAll() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请至少选择一首歌曲进行删除');
        return;
      }
      this.delVisible = true;
    },
    confirmDelete() {
      const ids = this.selectedRows.map(row => row.id);
      this.$request.post('/songs/deleteBatch', { ids }).then(response  => {
        if (response.code === '200') {
          this.$message.success('批量删除成功');
          this.delVisible = false;
          this.loadTableData();
        } else {
          this.$message.error(response.msg || '批量删除失败');
        }
      });
    },
    // 处理表格多选
    handleSelectionChange(val) {
      this.selectedRows = val;
    },
  },
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
}
.handle-box .el-input {
  width: 200px;
  margin-right: 10px;
}
</style>
