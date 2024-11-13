<template>
    <div class="register-container">
      <h2>注册</h2>
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-button type="primary" @click="handleRegister">注册</el-button>
        <el-button type="text" @click="goToLogin">去登录</el-button>
      </el-form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'UserRegister',
    data() {
      return {
        registerForm: {
          username: '',
          password: '',
          confirmPassword: '',
          email: '',
        },
        registerRules: {
          username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
          password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
          confirmPassword: [
            { required: true, message: '请确认密码', trigger: 'blur' },
            { validator: (rule, value, callback) => {
                if (value !== this.registerForm.password) {
                  callback(new Error('两次输入的密码不一致'));
                } else {
                  callback();
                }
              },
            },
          ],
          email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        },
      };
    },
    methods: {
      async handleRegister() {
        this.$refs.registerForm.validate(async (valid) => {
          if (valid) {
            try {
              await axios.post('/api/users/register', this.registerForm);
              this.$message.success('注册成功！请登录');
              this.$router.push({ path: '/homepage/userlogin' }); // 注册成功后跳转到登录页面
            } catch (error) {
              this.$message.error(error || '注册失败');
            }
          }
        });
      },
      goToLogin() {
        this.$router.push({ path: '/homepage/userlogin' });
      },
    },
  };
  </script>
  
  <style scoped>
  .register-container {
    width: 300px;
    margin: 100px auto;
    text-align: center;
  }
  </style>
  