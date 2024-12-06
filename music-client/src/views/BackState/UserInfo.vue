<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/usermanagement' }">用户管理</el-breadcrumb-item>
  </el-breadcrumb>
  <div class="container">
    <!-- 操作工具栏 -->
    <div class="handle-box">
      <el-button @click="deleteAll" :disabled="multipleSelection.length === 0" size="mini">批量删除</el-button>
      <el-input v-model="searchWord" placeholder="搜索用户" clearable></el-input>
      <el-button type="primary" @click="addDialogVisible = true">添加用户</el-button>
    </div>

    <!-- 用户列表表格 -->
    <el-table
      :data="filteredData"
      border
      size="small"
      height="550px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center"></el-table-column>
      <el-table-column label="ID" prop="id" width="60" align="center"></el-table-column>
      <el-table-column label="头像" width="130" align="center">
        <template v-slot="scope">
          <img :src="scope.row.avatar || '@/assets/img/default.png'" class="user-avatar" />
          <el-upload
            action="http://localhost:9090/upload"
            :data="{type: 'userImg'}"
            :show-file-list="false"
            :on-success="(response) => handleAvatarSuccess(response, scope.row.id)"
          >
            <el-button size="mini">更新头像</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="150" align="center"></el-table-column>
      <el-table-column label="性别" prop="gender" width="80" align="center"></el-table-column>
      <el-table-column label="手机号" prop="telephone" width="120" align="center"></el-table-column>
      <el-table-column label="邮箱" prop="email" width="140" align="center"></el-table-column>
      <el-table-column label="生日" prop="birth" width="100" :formatter="formatDate" align="center"></el-table-column>
      <el-table-column label="自我介绍" prop="introduction" align="center"></el-table-column>
      <el-table-column label="归属地" prop="location" width="150" align="center"></el-table-column>
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

    <!-- 添加用户对话框 -->
    <el-dialog title="添加用户" v-model="addDialogVisible">
      <el-form label-width="80px" :model="addForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="editForm.password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="telephone">
          <el-input v-model="addForm.telephone"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="addForm.gender" placeholder="请选择">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑用户对话框 -->
    <el-dialog title="编辑用户" v-model="editDialogVisible">
      <el-form :model="editForm">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="editForm.password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.telephone"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="editForm.gender" placeholder="请选择">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
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
      users: [],
      addDialogVisible: false,
      editDialogVisible: false,
      addForm: {
        username: '',
        password: '',
        email: '',
        telephone: '',
        gender: ''
      },
      editForm: {
        id: '',
        username: '',
        email: '',
        telephone: '',
        gender: ''
      },
    };
  },
 
  computed: {
    filteredData() {
      const filteredUsers = Array.isArray(this.users) ? this.users : [];
      if (this.searchWord) {
        return this.users.filter((user) => {
          return (
            user.username.includes(this.searchWord) ||
            user.email.includes(this.searchWord) ||
            user.telephone.includes(this.searchWord)
          );
        });
      }
      return filteredUsers.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    }
  },
  mounted(){
    this.loadUserData();
  },
  methods: {
    loadUserData(){
      this.$request.get('/users').then(response => {
        this.users = response.data;
      }).catch(error => {
        console.error("获取用户数据失败", error);
      });
    },
    // 添加用户
    addUser() {
      this.$request.post('/users/add', this.addForm).then(() => {
        this.addDialogVisible = false;
        this.loadUserData();
      }).catch(error => {
        console.error('添加用户失败', error);
      });
    },
    // 编辑用户
    editRow(row) {
      this.editForm = { ...row };
      this.editDialogVisible = true;
    },
    saveEdit() {
      this.$request.put(`/users/update/${this.editForm.id}`, this.editForm).then(() => {
        this.loadUserData();
        this.editDialogVisible = false;
      }).catch(error => {
        console.error('更新用户失败', error);
      });
    },
    // 删除单个用户
    deleteRow(id) {
      this.$request.delete(`/users/delete/${id}`).then(() => {
        this.loadUserData();
      }).catch(error => {
        console.error('删除用户失败', error);
      });
    },
    // 批量删除
    deleteAll() {
      const ids = this.multipleSelection.map(user => user.id);
      this.$request.delete(`/users/deleteBatch`, { data: ids }).then(() => {
        this.loadUserData();
        this.multipleSelection = [];
      }).catch(error => {
        console.error('批量删除失败', error);
      });
    },
    // 处理选择变化
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 页码变化
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    // 头像更新处理
    handleAvatarSuccess(response, userId) {
      const avatarUrl = response.url;
      console.log(avatarUrl);
      this.$request.put(`/users/updateAvatar/${userId}`, { avatar: avatarUrl })
        .then(() => {
          this.$message.success('头像更新成功');
          this.loadUserData();
        })
        .catch((error) => {
          console.error('头像更新失败', error);
          this.$message.error('头像更新失败');
        });
    },
    formatDate(row, column, cellValue) {
      const date = new Date(cellValue);
      return date.toLocaleDateString();
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
.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}
.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
