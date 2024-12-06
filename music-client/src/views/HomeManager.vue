<template>
    <div id="home-manager">
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-button type="text" @click="toggleCollapse" class="toggle-button">
            <i :class="collapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
          </el-button>
          <img src="@/assets/img/familymood.png" alt="" style="width: 50px; height: 50px;">
          <span class="title">Family Mood</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{ username }} <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
  
      <!-- 页面主体 -->
      <el-container class="content">
        <!-- 侧边栏 -->
        <el-aside :width="collapse ? '64px' : '200px'" class="aside">
        <el-menu :collapse="collapse" :collapse-transition="false" router>
            <el-menu-item index="/dashboard">
            <template #title>
              <el-icon><Odometer /></el-icon>
                <span v-if="!collapse">仪表盘</span>
            </template>
            </el-menu-item>

            <el-sub-menu index="1">
            <template #title>
                <el-icon><Service /></el-icon>
                <span v-if="!collapse">音乐管理</span>
            </template>
                <el-menu-item index="/singerinfo">歌手列表</el-menu-item>
                <el-menu-item index="/songsinfo">歌曲列表(从歌手页面进入)</el-menu-item>
                <el-menu-item index="/songlist">歌单列表</el-menu-item>
                <el-menu-item index="/testpage">测试</el-menu-item>

            </el-sub-menu>
            <el-sub-menu index="2">
            <template #title>
                <el-icon><Avatar /></el-icon>
                <span v-if="!collapse">用户管理</span>
            </template>
                <el-menu-item index="/UserInfo">用户列表</el-menu-item>
                <el-menu-item index="/CollectInfo">收藏信息</el-menu-item>
                <el-menu-item index="/CommentInfo">评论列表</el-menu-item>
            </el-sub-menu>
        </el-menu>
        </el-aside>

        <!-- 内容区域 -->
        <el-main class="main">
            <router-view></router-view>
        </el-main>
       </el-container>
    </div>
  </template>
  
  <script>
  export default {
    name: "HomeManager",
    data() {
      return {
        user:JSON.parse(localStorage.getItem('family-user') || '{}'),
        username: '',
        collapse: false, // 控制侧边栏是否折叠
      };
    },
    mounted(){
      this.username = this.$route.query.name;
    },
    methods: {
      toggleCollapse() {
        this.collapse = !this.collapse; // 切换折叠状态
      },
      handleCommand(command) {
        if (command === 'logout') {
          this.$message('退出登录');
          // 处理退出登录逻辑
        }
      }
    }
  };
  </script>
  
  <style scoped>
  #home-manager {
    height: 100vh;
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #f5f5f5;
    position: relative;
    z-index: 1;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    border-bottom: 2px solid #eaeaea; /* 表头底部边框 */
    color: black;
    height: 60px;
  }
  
  .title {
    font-size: 18px;
    font-weight: bold;
    margin-left: 20px;
  }
  
  .toggle-button {
    margin-left: 10px; /* 使按钮与标题分开 */
    font-size: 18px; /* 增大图标的字体大小 */
  }
  
  .content {
    height: calc(100vh - 60px); /* 扣除顶部导航的高度 */
    display: flex;
  }
  
  .aside {
    background-color: #2e3d4d;
    color: #fff;
    transition: width 0.3s; /* 侧边栏宽度变化时的动画 */
  }
  
  .main {
    flex: 1;
    padding: 20px;
    background-color: #f5f5f5;
    overflow-y: auto;
  }
  
  .el-main {
    padding: 20px;
    background-color: #f5f5f5;
  }
  
  .el-menu {
    height: 100%;
  }
  
  .el-menu-item {
    font-size: 14px;
  }
  </style>
  