<template>
  <div class="home-container">
    <div class="front-header">
        <div class="front-header-left">
            <button class="toggle-sidebar-btn" @click.stop="toggleSidebar">
                <i class="fas fa-bars"></i>
            </button>
            <span class="brand-title">Family Mood</span>
        </div>
        <div class="front-header-center">
            <!-- <input type="text" class="search-box" placeholder="搜索音乐、歌手..." /> -->
            <el-input
              v-model="searchQuery"
              placeholder="搜索歌曲、歌手或专辑"
              class="search-box"
              @keyup.enter="searchSongs"
              clearable
            >
            <el-button icon="el-icon-search" @click="searchSongs"></el-button>
          </el-input>
        </div>
        <div class="front-header-right">
          <div v-if="user.username" class="user-menu">
            <span
              class="username"
              @click="toggleDropdown"
            >
              <!-- {{ user.username }} -->
              <img :src="user.avatar" alt="" style="width: 50px; height: 50px; border-radius: 40px; top: 5px;">
              <!-- <i class="fas fa-caret-down"></i> -->
            </span>
            <div v-if="isDropdownVisible" class="dropdown-menu">
              <span class="dropdown-item"><router-link to="/homepage/userprofile" class="auth-mes">个人信息</router-link></span>
              <button @click="logout" class="dropdown-item">退出</button>
            </div>
          </div>
          <div v-else>
            <router-link to="/homepage/userregister" class="auth-link">注册</router-link>
            <span class="separator">|</span>
            <router-link to="/homepage/userlogin" class="auth-link">登录</router-link>
          </div>
        </div>
    </div>
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <div class="sidebar" :class="{ collapsed: !isSidebarOpen }" ref="sidebar">
        <!-- 主菜单 -->
        <nav class="menu">
          <ul>
            <li :class="{ active: activeMenu === 'discover' }" @click="setActive('discover')">
              <router-link to="/homepage/discovermusic">
                <i class="fas fa-music"></i>
                <span v-if="isSidebarOpen">发现音乐</span>
              </router-link>
            </li>
            <li :class="{ active: activeMenu === 'library' }" @click="setActive('library')">
              <router-link to="#">
                <i class="fas fa-headphones-alt"></i>
                <span v-if="isSidebarOpen">我的音乐</span>
              </router-link>
            </li>
            <li :class="{ active: activeMenu === 'vip' }" @click="setActive('vip')">
              <router-link to="#">
                <i class="fas fa-crown"></i>
                <span v-if="isSidebarOpen">VIP</span>
              </router-link>
            </li>
          </ul>
        </nav>
  
        <!-- 浏览项 -->
        <div class="browse-section">
          <h3 v-if="isSidebarOpen">Browse</h3>
          <ul>
            <li :class="{ active: activeMenu === 'artist' }" @click="setActive('artist');">
              <router-link to="/homepage/singerlist"><i class="fas fa-user" style="color: cornflowerblue;"></i> <span v-if="isSidebarOpen">歌手</span></router-link>
            </li>
            <li :class="{ active: activeMenu === 'album' }" @click="setActive('album')">
              <router-link to="#"><i class="fa-solid fa-record-vinyl" style="color: darkorchid;"></i> <span v-if="isSidebarOpen">专辑</span></router-link>
            </li>
            <li :class="{ active: activeMenu === 'playlist' }" @click="setActive('playlist')">
              <router-link to="#"><i class="fas fa-list" style="color:darkgreen ;"></i> <span v-if="isSidebarOpen">歌单</span></router-link>
            </li>
            <li :class="{ active: activeMenu === 'ranking' }" @click="setActive('ranking')">
              <router-link to="#"><i class="fas fa-chart-line" style="color: purple;"></i> <span v-if="isSidebarOpen">排行榜</span></router-link>
            </li>
            <li :class="{ active: activeMenu === 'radio' }" @click="setActive('radio')">
              <router-link to="#"><i class="fas fa-broadcast-tower" style="color: burlywood;"></i> <span v-if="isSidebarOpen">电台</span></router-link>
            </li>
            <li :class="{ active: activeMenu === 'movie' }" @click="setActive('movie')">
              <router-link to="/homepage/chatpage"><i class="fa-regular fa-comment" style="color: blueviolet;"></i> <span v-if="isSidebarOpen">会话</span></router-link>
            </li>
          </ul>
        </div>
        <div class="additional-section" v-if="isSidebarOpen">
                <ul>
                    <li><a href="#">下载</a></li>
                    <li><a href="#">音乐内容</a></li>
                    <li><a href="#">常见问题</a></li>
                    <li><a href="#">联系我们</a></li>
                    <li><a href="#">用户反馈</a></li>
                </ul>
                <div class="icon-row">
                    <i class="fas fa-download" aria-label="Download"></i>
                    <i class="fas fa-question-circle" aria-label="FAQ"></i>
                    <i class="fas fa-envelope" aria-label="Contact Us"></i>
                    <i class="fas fa-comments" aria-label="Feedback"></i>
                </div>
            </div>
            <div class="policy-section" v-if="isSidebarOpen">
                <ul>
                    <li><a href="#">Terms of Service</a></li>
                    <li><a href="#">User Agreement</a></li>
                    <li><a href="#">Privacy Policy</a></li>
                    <li><a href="#">Take-Down Policy</a></li>
                    <li><a href="#"></a></li>
                </ul>
                <p>©&nbsp;2024&nbsp;-&nbsp;2024&ensp;Asher. <br>All Rights Reserved</p>
            </div>
      </div>
      <div class="content">
        <router-view /> <!-- 显示子组件 -->
      </div>
    </div>
    <PlayList />
  </div>
</template>
<script> 
import PlayList from './Front/PlayList.vue';
export default {
  data() {
    return {
      user:{},
      activeMenu: 'discover', // 默认选中“发现音乐”
      isSidebarOpen: false,    // 侧边栏初始状态为打开
      isDropdownVisible:false,
      searchQuery:'',
    };
  },
  mounted() {
    const savedUser = JSON.parse(localStorage.getItem("family-user") || "{}");
    if (savedUser && savedUser.username) {
      this.user = savedUser; // 同步到组件的 user 数据
    }
    // 监听点击事件
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeMount() {
    document.removeEventListener("click", this.handleClickOutside);
  },
  methods: {
    toggleDropdown(){
      this.isDropdownVisible = !this.isDropdownVisible;
    },
    handleClickOutside(event) {
      const dropdown = this.$el.querySelector(".dropdown-menu");
      const username = this.$el.querySelector(".username");
      const sidebar = this.$refs.sidebar;
      if (
        this.isDropdownVisible && // 下拉菜单打开时
        ((!dropdown || !dropdown.contains(event.target)) && // 如果 dropdown 存在且点击不在其内
        (!username || !username.contains(event.target))) // 如果 username 存在且点击不在其内
      ) {
        this.isDropdownVisible = false;
      }
      if ( this.isSidebarOpen &&  (!sidebar || !sidebar.contains(event.target)) // 如果点击不在侧边栏内部
      ) {
        this.isSidebarOpen = false;
      }
    },
    setActive(menuItem) {
      this.activeMenu = menuItem; // 设置为当前点击的菜单项
    },
    toggleSidebar() {
      console.log('这是切换按钮');
      console.log(this.user);
      console.log(this.user.username);
      this.isSidebarOpen = !this.isSidebarOpen; // 切换侧边栏的显示状态
    },
    searchSongs() {
      if (this.searchQuery.trim()) {
        this.$router.push({ 
          path: "/homepage/searchresults", 
          query: { keyword: this.searchQuery.trim() } 
        });
      } else {
        this.$message.warning("请输入搜索内容！");
      }
    },
    logout(){
      localStorage.removeItem("family-user");
      this.user = {};
      this.isDropdownVisible = false;
      this.$router.push("/homepage/discovermusic")
    },
  },
  components:{
    PlayList
  }
};
</script>
<style scoped>
@font-face {
  font-family: 'AA后浪行楷'; /* 字体名称，可以根据需要自定义 */
  src: url('../assets/AA后浪行楷.TTF') format('truetype'); /* 根据实际路径调整 */
}

.front-header {
    display: flex;
    height: 60px;
    width: 100%;
    line-height: 60px;
    border-bottom: 1px solid #eee;
    background: rgb(17, 17, 17);
    border-top-left-radius: 10px;
    border-top-right-radius: 10px; 
}
.front-header-left {
    width: 350px;
    display: flex;
    align-items: center;
    padding-left: 30px;
}
/* 控制按钮样式 */
.toggle-sidebar-btn {
    position: absolute;
    top: 25px;
    left: 24px;
    background: none;
    border: none;
    color: rgb(124, 120, 120);
    font-size: 24px;
    cursor: pointer;
    z-index: 10;
}
.brand-title {
  font-family: 'AA后浪行楷', sans-serif; /* 使用 Family Mood 字体 */
  font-size: 26px; /* 设置字体大小 */
  color: #ffffff; /* 设置字体颜色 */
  margin-left: 50px; /* 与按钮之间的间距 */
}
.front-header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}
.search-box {
  width: 500px;
  left: 0;
  height: 55%;
  margin-top: 7px;
  margin-left: -100px;
  padding: 2px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
.front-header-right {
  width: 200px;
  padding-right: 20px;
  text-align: right;
}
.auth-link {
  color: #ffffff;
  text-decoration: none;
  margin-left: 10px;
}
.separator {
  color: #ffffff;
  margin: 0 5px;
}
.username {
  font-weight: bold;
  color: #ffffff;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
}
.username i {
  margin-left: 5px;
}

/* 下拉菜单样式 */
.dropdown-menu {
  width: 100px;
  position: absolute;
  top: 60px;
  right: 20px;
  background: #333;
  color: white;
  border: 1px solid #444;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  z-index: 100;
}

.dropdown-item {
  height: 40px;
  padding: 5px 0 20px 0;
  cursor: pointer;
  display: block;
  background: none;
  border: none;
  color: white;
  text-align: center;
  width: 100%;
}
.dropdown-item:hover {
  background: #444;
}
.auth-mes{
  color: #ffffff;
  text-decoration: none;
}


/* 侧边栏容器样式 */
.sidebar-container {
    display: flex;
    position: relative;
    /* height: 700px;
    margin-bottom: 0; */
    height: calc(100vh - 60px - 45px); /* 减去顶部栏和底部栏的高度 */
    margin-top: -1px;
    margin-bottom: 0;
    /* 与顶部栏连接 */
    overflow: hidden;
}



/* 侧边栏样式 */
.sidebar {
    width: 180px;
    margin-top: -1px;
    height: 100%;
    background-color: #1c1c1c;
    display: flex;
    flex-direction: column;
    padding: 20px;
    color: white;
    transition: width 0.3s ease-in-out, padding 0.4s ease-in-out;
    overflow-y: auto;
}
.sidebar::-webkit-scrollbar {
    width: 8px;  /* 设置垂直滚动条的宽度 */
    height: 6px; /* 水平滚动条的高度 */
}

.sidebar::-webkit-scrollbar-track {
    background: #1c1c1c; /* 轨道的背景色 */
}

.sidebar::-webkit-scrollbar-thumb {
    background-color: #a9aaa8; /* 滚动条的颜色 */
    border-radius: 10px; /* 圆角 */
}

.sidebar::-webkit-scrollbar-thumb:hover {
    background-color: #c8c9c8; /* 悬停时的滚动条颜色 */
}

/* 当侧边栏关闭时宽度缩小 */
.sidebar.collapsed {
    width: 30px;
    overflow-y: hidden;
}

.menu ul,
.browse-section ul {
list-style-type: none;
padding: 0;
margin: 0;
}

.menu li,
.browse-section li {
display: flex;
align-items: center;
padding: 15px 0;
font-size: 0.875rem;
font-weight: 700;
position: relative;
cursor: pointer;
transition: padding 0.4s ease-in-out;
}

.menu li a,
.browse-section li a {
color: #ffffff;
text-decoration: none;
display: flex;
align-items: center;
width: 100%;
transition: padding-left 0.4s ease-in-out;

}

.menu li.active span,
.browse-section li.active span{
    color: #1db954 !important;/* 激活项的文字颜色 */
}

.menu li.active::before,
.browse-section li.active::before {
content: '';
position: absolute;
left: -20px;
height: 50%;
width: 4px;
background-color: #1db954; /* 激活项的左侧竖线 */
}

.menu li i,
.browse-section li i {
margin-right: 10px;
}

.browse-section h3 {
font-size: 16px;
margin-bottom: 10px;
color: #a0a0a0;
}
.additional-section,
.policy-section {
    margin-top: 20px; /* 给两个盒子之间增加间距 */
    padding: 10px; /* 盒子内边距 */
}

.additional-section h3,
.policy-section h3 {
    font-size: 16px;
    color: #a0a0a0;
}

.additional-section ul,
.policy-section ul {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.additional-section li {
    display: flex;
    align-items: center;
    padding: 8px 0;
    font-size: 0.875rem;
    font-weight: 700;
    position: relative;
    cursor: pointer;
}

.policy-section li {
    display: flex;
    align-items: center;
    padding: 3px 0;
    font-size: 0.795rem;
    font-weight: 700;
    position: relative;
    cursor: pointer;
}

.additional-section li a {
    color: #bcbcbc;
    text-decoration: none;
    display: flex;
    align-items: center;
    width: 100%;
    padding-left: 3px; /* 左侧内边距 */
}

.policy-section li a {
    color: #959595;
    text-decoration: none;
    display: flex;
    align-items: center;
    width: 100%;
}

.policy-section p {
    color: #959595;
    font-size: 0.785rem;
    text-align: left; /* 确保文本左对齐 */
}

.icon-row {
    display: flex;
    justify-content: space-between;
    margin-top: 18px;
}

.icon-row i {
    color: #bcbcbc; /* 图标颜色 */
    font-size: 20px; /* 图标大小 */
}



/* 设置 content 的样式 */
.content {
  flex: 1; /* 占满剩余空间 */
  padding: 20px; /* 内边距 */
  /* height: 100%; */
  background-color: #f5f5f5; /* 内容区背景颜色 */
  overflow-y: auto; /* 允许竖向滚动 */
  overflow-x: hidden; /* 禁止横向滚动 */
  margin-bottom: 35px;
}

</style>