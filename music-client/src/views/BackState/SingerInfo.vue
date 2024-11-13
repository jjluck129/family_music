<template>
  <div class="singer-info">
    <!-- 搜索框与操作按钮 -->
    <div class="handle-box">
      <el-input v-model="searchQuery" placeholder="筛选歌手" class="search-input"></el-input>
      <el-button type="primary" @click="showAddSingerDialog">添加歌手</el-button>
      <el-button type="danger" @click="deleteSelected" :disabled="!multipleSelection.length">批量删除</el-button>
    </div>

    <!-- 歌手列表表格 -->
    <el-table ref="multipleTable" :data="filteredSingers" border  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"></el-table-column>
      <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
      <el-table-column label="歌手图片" width="130" align="center">
        <template #default="scope">
          <div>
            <img v-if="scope.row.imgUrl" :src="scope.row.imgUrl" alt="歌手头像" class="singer_pic"/>
            <el-button @click="showChangePicDialog(scope.row.id)">更改图片</el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="歌手" width="120" align="center"></el-table-column>
      <el-table-column prop="gender" label="性别" width="80" align="center"></el-table-column>
      <el-table-column prop="birth" label="出生" width="120" align="center">
        <template #default="scope">
          <p>
            {{
              new Date(scope.row.birth).getFullYear() + '-' +
              String(new Date(scope.row.birth).getMonth() + 1).padStart(2, '0') + '-' +
              String(new Date(scope.row.birth).getDate()).padStart(2, '0')
            }}
          </p>
        </template>
      </el-table-column>
      <el-table-column prop="location" label="地区" width="120" align="center"></el-table-column>
      <el-table-column prop="description" label="简介">
        <template #default="scope">
          <p class="singer-intro">{{ scope.row.description }}</p>
        </template>
      </el-table-column>
      <el-table-column label="歌曲管理" width="120" align="center">
        <template #default="scope">
          <el-button size="mini" @click="manageSongs(scope.row.id,scope.row.name)">管理歌曲</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template #default="scope">
          <el-button size="mini" type="primary" @click="showEditSingerDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteSinger(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination background layout="prev, pager, next" :total="singers.length" :page-size="pageSize" v-model:current-page="currentPage" @current-change="handlePageChange" />

    <!-- 添加歌手对话框 -->
    <el-dialog title="添加歌手" v-model="addSingerDialogVisible">
      <el-form :model="newSinger" ref="newSingerForm" label-width="100px">
        <el-form-item label="歌手名字" prop="name">
          <el-input v-model="newSinger.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="newSinger.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
            <el-option label="组合" value="组合"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上传头像" prop="imgUrl">
          <el-upload
            action="http://localhost:9090/upload"  
            :data="{type: 'singerImg'}"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="生日" prop="birth">
          <el-date-picker v-model="newSinger.birth" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="籍贯" prop="location">
          <el-input v-model="newSinger.location"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" v-model="newSinger.description"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button @click="addSingerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitNewSinger">提交</el-button>
      </div>
    </el-dialog>

    <!-- 更改图片对话框 -->
    <el-dialog title="更改图片" v-model="changePicDialogVisible">
      <el-form-item label="上传头像" prop="imgUrl">
          <el-upload
            ref="singerImgUpload"
            action="http://localhost:9090/upload"  
            :data="{type: 'singerImg'}"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <div class="dialog-footer">
        <el-button @click="changePicDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitNewPic">提交</el-button>
      </div>
    </el-dialog>

    <!-- 编辑歌手对话框 -->
    <el-dialog title="编辑歌手" v-model="editSingerDialogVisible">
      <el-form :model="currentSinger" ref="editSingerForm" label-width="100px">
        <el-form-item label="歌手名字" prop="name">
          <el-input v-model="currentSinger.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="currentSinger.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
            <el-option label="组合" value="组合"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生日" prop="birth">
          <el-date-picker v-model="currentSinger.birth" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="籍贯" prop="location">
          <el-input v-model="currentSinger.location"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" v-model="currentSinger.description"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button @click="editSingerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditSinger">提交</el-button>
      </div>
    </el-dialog>

    <!-- 删除确认框 -->
    <el-dialog :delVisible="delVisible" @confirm="confirmDelete" @cancelRow="delVisible = false"></el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      searchQuery: "", // 搜索关键词
      multipleSelection: [], // 已选择的行
      currentPage: 1, // 当前页
      pageSize: 10, // 每页显示条数
      singers: [],
      newSinger: {
        name: '',
        gender: '',
        imgUrl: '',
        birth: '',
        location: '',
        description: ''
      },
      currentSinger: { // 用于编辑歌手时存储当前歌手信息
        id: '',
        name: '',
        gender: '',
        birth: '',
        location: '',
        description: ''
      },
      picSinger:{
        imgUrl: ''
      },
      addSingerDialogVisible: false,
      changePicDialogVisible: false,
      editSingerDialogVisible: false,
      delVisible: false
    };
  },
  computed: {
    // 根据搜索关键词过滤歌手
    filteredSingers() {
      if (!this.searchQuery) {
        return this.paginatedSingers;
      }
      return this.singers.filter((singer) =>
        singer.name.includes(this.searchQuery)
      );
    },
    // 分页后的歌手数据
    paginatedSingers() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      console.log(this.singers.slice(start, end));
      return this.singers.slice(start, end);
    },
  },
  mounted() {
    this.fetchSingers();
  },
  methods: {
    // 获取歌手列表
    fetchSingers() {
      axios.get('/singers').then(response => {
        console.log(response.data);
        this.singers = response.data;
      }).catch(error => {
        console.error('获取歌手失败', error);
      });
    },
    // 显示添加歌手弹窗
    showAddSingerDialog() {
      this.addSingerDialogVisible = true;
    },
    // 添加歌手
    submitNewSinger() {
      axios.post('/singers', this.newSinger).then(() => {
        this.$message.success('添加成功');
        this.addSingerDialogVisible = false;
        this.fetchSingers();
        this.newSinger = {
          name: '',
          gender: '',
          imgUrl: '',
          birth: '',
          location: '',
          description: ''
        };
        // this.$refs.singerImgUpload.clearFiles();
      }).catch(error => {
        this.$message.error('添加失败');
        console.error('添加歌手失败', error);
      });
    },
    
    // 上传成功时处理
    handleUploadSuccess(response) {
      if (response && response.url) {
        if(this.addSingerDialogVisible == true){
          this.newSinger.imgUrl = response.url;  // 返回图片的 URL
        }else{
          this.picSinger.imgUrl = response.url;
          console.log(response.url)
        }
        this.$message.success('图片上传成功');
      } else {
        this.$message.error('上传成功但未返回 URL');
      }
    },
    // 上传失败时处理
    handleUploadError() {
      this.$message.error('图片上传失败');
    },
    
    // 歌曲管理
    manageSongs(singerId, singerName) {
      this.$router.push({
        path: '/songsinfo',
        query: { singerId, singerName } // 传递歌手ID和姓名
      });
    },
    showChangePicDialog(singerId){
      this.diaId = singerId;
      this.changePicDialogVisible = true;
    },
    submitNewPic(){
      console.log(this.diaId);
      axios.patch(`/singers/${this.diaId}`,this.picSinger).then(() => {
        this.$message.success('更新成功');
        this.changePicDialogVisible = false;
        this.fetchSingers();
      }).catch(() => {
        this.$message.error('更新失败');
      });
    },

    //编辑歌手
    showEditSingerDialog(singer) {
      this.currentSinger = { ...singer };
      this.editSingerDialogVisible = true;
    },
    submitEditSinger() {
      axios.put(`/singers/${this.currentSinger.id}`, this.currentSinger).then(() => {
        this.$message.success('编辑成功');
        this.editSingerDialogVisible = false;
        this.fetchSingers();
      }).catch(error => {
        this.$message.error('编辑失败');
        console.error('编辑歌手失败', error);
      });
    },

    // 删除歌手
    deleteSinger(singer) {
      this.$confirm(`确定要删除歌手 ${singer.name} 吗？`, '警告', {
        type: 'warning'
      }).then(() => {
        axios.delete(`/singers/${singer.id}`).then(() => {
          this.$message.success('删除成功');
          this.fetchSingers();
        }).catch(error => {
          this.$message.error('删除失败');
          console.error('删除歌手失败', error);
        });
      });
    },
    // 批量删除
    deleteSelected() {
      if (this.multipleSelection.length) {
        this.delVisible = true;
      } else {
        this.$message.warning('请至少选择一位歌手进行删除');
      }
    },
    // 确认批量删除
    confirmDelete() {
      // 获取选中的歌手 ID
      const ids = this.multipleSelection.map(row => row.id);
      
      // 确认删除操作
      this.$confirm("确定要删除选中的歌手吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          // 调用批量删除的 API
          axios.delete(`/singers`, { data: { ids } }) // 发送 DELETE 请求
            .then(() => {
              this.$message.success('批量删除成功');
              this.fetchSingers(); // 刷新歌手列表
            })
            .catch(error => {
              this.$message.error('批量删除失败');
              console.error('删除失败', error);
            });
        })
        .catch(() => {
          this.$message.info('取消删除');
        });
      this.delVisible = false; // 关闭删除确认对话框
    },
    
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
  },
};
</script>

<style scoped>
.singer-info {
  padding: 20px;
}
.handle-box {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}
.singer_pic{
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.singer-intro{
  max-height: 100px;
  overflow-y: auto;
}
.search-input {
  width: 200px;
  margin-right: 10px;
}
.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
