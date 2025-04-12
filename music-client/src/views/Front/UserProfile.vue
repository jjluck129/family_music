<template>
    <div class="user-profile">
      <el-card class="user-profile-card" shadow="hover">
        <!-- 头像部分 -->
        <div class="avatar-section">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/upload"
            :data="{ type: 'userImg' }"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <el-avatar 
              :size="120" 
              :src="form.avatar" 
              class="user-avatar"
              style="cursor: pointer;"
            ></el-avatar>
          </el-upload>
          <span class="upload-tip">点击头像修改</span>
        </div>
  
        <!-- 用户信息表单 -->
        <el-form :model="form" ref="userForm" label-width="100px" class="user-form">
          <el-form-item label="名称：" prop="username">
            <el-input v-model="form.username" placeholder="请输入您的名称"></el-input>
          </el-form-item>
  
          <el-form-item label="性别：" prop="gender">
            <el-radio-group v-model="form.gender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
  
          <el-form-item label="手机号：" prop="telephone">
            <el-input v-model="form.telephone" placeholder="请输入您的手机号"></el-input>
          </el-form-item>
  
          <el-form-item label="邮箱：" prop="email">
            <el-input v-model="form.email" placeholder="请输入您的邮箱"></el-input>
          </el-form-item>
  
          <el-form-item label="出生日期：" prop="birth">
            <el-date-picker
              v-model="form.birth"
              type="date"
              placeholder="选择出生日期"
              style="width: 100%;"
            ></el-date-picker>
          </el-form-item>
  
          <el-form-item label="个人简介：" prop="introduction">
            <el-input
              v-model="form.introduction"
              type="textarea"
              placeholder="请输入您的个人简介"
            ></el-input>
          </el-form-item>
  
          <el-form-item>
            <el-button type="primary" @click="submitForm">保存修改</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </template>
  
  <script>
  export default {
    name: "UserProfile",
    data() {
      return {
        form: {
          username: "",
          gender: "",
          telephone: "",
          email: "",
          birth: null,
          avatar: "",
          introduction: "",
        },
      };
    },
    created() {
      // 初始化表单数据，从 localStorage 获取用户信息
      const userInfo = JSON.parse(localStorage.getItem("family-user")) || {};
      this.form = {
        username: userInfo.username || "",
        gender: userInfo.gender || "",
        telephone: userInfo.telephone || "",
        email: userInfo.email || "",
        birth: userInfo.birth || null,
        avatar: userInfo.avatar || "",
        introduction: userInfo.introduction || "",
      };
    },
    methods: {
      // 提交表单
      submitForm() {
        // 提交表单修改
        const userInfo = JSON.parse(localStorage.getItem("family-user")) || {};
        const updatedUserInfo = {
          ...userInfo,  
          username: this.form.username, 
          gender: this.form.gender, 
          telephone: this.form.telephone, 
          email: this.form.email,
          birth: this.form.birth,
          introduction: this.form.introduction, 
        };
        const token = userInfo.token; 
        localStorage.setItem("family-user", JSON.stringify({ ...updatedUserInfo, token }));

        const userId = userInfo.id;
        this.$request
          .put(`/users/update/${userId}`, updatedUserInfo)
          .then(() => {
            // 更新成功后，保存到 localStorage
            const updatedData = { ...userInfo, ...updatedUserInfo };
            localStorage.setItem("family-user", JSON.stringify(updatedData));
            
            // 提示用户修改成功
            this.$message.success("修改成功！");
          })
          .catch(() => {
            this.$message.error("更新失败，请重试！");
          });
      },
      // 重置表单
      resetForm() {
        const userInfo = JSON.parse(localStorage.getItem("family-user")) || {};
        this.form = {
          username: userInfo.username || "",
          gender: userInfo.gender || "",
          telephone: userInfo.telephone || "",
          email: userInfo.email || "",
          birth: userInfo.birth || null,
          avatar: userInfo.avatar || "",
          introduction: userInfo.introduction || "",
        };
      },
      // 上传成功后处理头像
      handleAvatarSuccess(response) {
        // 假设后端返回的头像地址在 response.url
        const newAvatarUrl = response.url;
        const userId = JSON.parse(localStorage.getItem("family-user")).id;
        console.log(newAvatarUrl);
        // 更新表单中的头像地址
        this.form.avatar = newAvatarUrl;

        // 调用后端接口更新数据库
        this.$request
            .put(`/users/updateAvatar/${userId}`, { avatar: newAvatarUrl })
            .then(() => {
            this.$message.success("头像上传并更新成功！");
            // 同步更新到 localStorage
            const userInfo = JSON.parse(localStorage.getItem("family-user")) || {};
            userInfo.avatar = newAvatarUrl;
            localStorage.setItem("family-user", JSON.stringify(userInfo));
            })
            .catch((error) => {
            console.error("更新头像失败：", error);
            this.$message.error("头像更新失败，请重试！");
            });
        },
      // 上传前校验
      beforeAvatarUpload(file) {
        const isJPG = file.type === "image/jpeg" || file.type === "image/png";
        const isLt2M = file.size / 1024 / 1024 < 2;
  
        if (!isJPG) {
          this.$message.error("上传图片格式只能为 JPG 或 PNG！");
        }
        if (!isLt2M) {
          this.$message.error("上传图片大小不能超过 2MB！");
        }
        return isJPG && isLt2M;
      },
    },
  };
  </script>
  
  <style scoped>
  .user-profile {
    max-width: 600px;
    margin: 20px auto;
  }
  .user-profile-card {
    padding: 20px;
  }
  .avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
  }
  .user-avatar {
    border: 2px solid #f0f0f0;
    transition: transform 0.2s;
  }
  .user-avatar:hover {
    transform: scale(1.1);
  }
  .upload-tip {
    margin-top: 10px;
    font-size: 12px;
    color: #909399;
  }
  .user-form {
    margin-top: 20px;
  }
  </style>
  