<template>
    <div class="login-container">
      <h2>登录</h2>
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-button type="primary" @click="handleLogin">登录</el-button>
        <el-button type="text" @click="goToRegister">去注册</el-button>
      </el-form>
    </div>
  </template>
  
  <script>
// import axios from 'axios';
  export default {
    name: 'UserLogin',
    data() {
      return {
        loginForm: {
          username: '',
          password: '',
        },
        loginRules: {
          username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
          password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        },
      };
    },
    methods: {
      handleLogin() {
        this.$refs.loginForm.validate(async (valid) => {
          console.log(this.loginForm)
          if (valid) {
            try {
              const response = await this.$request.post('/users/login', this.loginForm);
              console.log(response);
              if (response.code === '200') {
              localStorage.setItem('family-user', JSON.stringify(response.data));
              this.$message.success('登录成功')
              this.$router.push("/homepage/discovermusic")
              } else {
                this.$message.error(response.data.msg);
              }
            } catch (error) {
              this.$message.error('登录失败，请重试');
            }
          }
        });
      },
      goToRegister() {
        this.$router.push({ path: '/homepage/userregister' });
      },
    },
  };
  </script>
  
  <style scoped>
  .login-container {
    width: 300px;
    margin: 100px auto;
    text-align: center;
  }
  </style>
  