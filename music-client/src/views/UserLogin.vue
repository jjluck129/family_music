<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">登录</h2>
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            class="login-input"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            class="login-input"
          />
        </el-form-item>
        <el-button type="primary" class="login-button" @click="handleLogin">登录</el-button>
        <el-button type="text" class="register-button" @click="goToRegister">去注册</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script>
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
        if (valid) {
          try {
            const response = await this.$request.post('/users/login', this.loginForm);
            if (response.code === '200') {
              // localStorage.setItem('family-user', JSON.stringify(response.data));
              this.$store.dispatch('login', response.data);
              this.$message.success('登录成功');
              this.$router.push("/homepage/discovermusic");
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
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 130px;
  background-color: #f4f7fc;
}

.login-card {
  width: 350px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: #ffffff;
}

.login-title {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.login-input {
  margin-bottom: 15px;
}

.login-button {
  width: 100%;
  margin-bottom: 10px;
}
.el-button+.el-button {
    margin-left: 1px;
}
.register-button {
  width: 100%;
  color: #409eff;
  text-align: center;
}

.register-button:hover {
  text-decoration: underline;
}
</style>
