<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="register-title">注册</h2>
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            class="register-input"
          />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择角色">
            <el-option label="会员" value="member"></el-option>
            <el-option label="管理员" value="admin"></el-option>
          </el-select>
        </el-form-item>
        <el-button type="primary" class="register-button" @click="handleRegister">注册</el-button>
        <el-button type="text" class="login-button" @click="goToLogin">去登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserRegister',
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        role: 'member',
      },
      registerRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.password) {
                callback(new Error('两次输入的密码不一致'));
              } else {
                callback();
              }
            },
          },
        ],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }],
      },
    };
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          this.$request
            .post('/users/register', this.registerForm)
            .then((response) => {
              if (response.code == '200') {
                this.$message.success('注册成功！请登录');
                this.$router.push({ path: '/homepage/userlogin' });
              } else {
                this.$message.error('注册失败，用户名已存在');
              }
            })
            .catch((error) => {
              console.log('注册请求发生错误：', error);
            });
        } else {
          console.log('请完善表单信息，确保符合校验规则');
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
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 130px;
  background-color: #f4f7fc;
}

.register-card {
  width: 350px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: #ffffff;
}

.register-title {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.register-input {
  margin-bottom: 15px;
}

.register-button {
  width: 100%;
  margin-bottom: 10px;
}
.el-button+.el-button {
    margin-left: 1px;
}
.login-button {
  width: 100%;
  color: #409eff;
  text-align: center;
}

.login-button:hover {
  text-decoration: underline;
}
</style>
