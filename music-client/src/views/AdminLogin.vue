<template>
  <div class="login-container">
    <el-card class="box-card">
      <h2 class="title">管理员登录</h2>
      
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" placeholder="请输入密码" type="password"></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      
      <el-alert
        v-if="errorMessage"
        :title="errorMessage"
        type="error"
        show-icon
        class="error-alert"
        closable
      ></el-alert>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      errorMessage: '',
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    // 处理登录
    handleLogin() {
      // 发送登录请求
      console.log(this.loginForm)
      axios.post('/admin/login', this.loginForm)
        .then((response) => {
          if (response.status === 200 && response.data.token) {
            const token = response.data.token;
            // 存储 token
            localStorage.setItem('token', token);
            this.$message.success('登录成功！');
            // 跳转到管理员主页并传递用户名
            this.$router.push("/dashboard");
          } else {
            this.errorMessage = '用户名或密码错误';
          }
        })
        .catch((error) => {
          console.error('登录失败:', error);
          this.errorMessage = '登录失败，请检查网络连接';
        });
    },
  }
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/img/bg.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
.box-card {
  width: 400px;
  padding: 20px;
}
.title {
  text-align: center;
  margin-bottom: 20px;
}
.error-alert {
  margin-top: 20px;
}
</style>
