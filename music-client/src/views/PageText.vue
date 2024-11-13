<template>
    <div class="sidebar-container">
        <!-- 控制按钮 -->
        <button class="toggle-sidebar-btn" @click="toggleSidebar" aria-label="Toggle sidebar">
            <i class="fas fa-bars"></i>
        </button>

        <!-- 侧边栏 -->
        <div class="sidebar" :class="{ collapsed: !isSidebarOpen }">
            <!-- 主菜单 -->
            <nav class="menu">
                <ul>
                    <li :class="{ active: activeMenu === 'discover' }" @click="setActive('discover')">
                        <a href="#">
                            <i class="fas fa-music"></i>
                            <span v-if="isSidebarOpen">发现音乐</span>
                        </a>
                    </li>
                    <li :class="{ active: activeMenu === 'library' }" @click="setActive('library')">
                        <a href="#">
                            <i class="fas fa-headphones-alt"></i>
                            <span v-if="isSidebarOpen">我的音乐</span>
                        </a>
                    </li>
                    <li :class="{ active: activeMenu === 'vip' }" @click="setActive('vip')">
                        <a href="#">
                            <i class="fas fa-crown"></i>
                            <span v-if="isSidebarOpen">VIP</span>
                        </a>
                    </li>
                </ul>
            </nav>

            <!-- 浏览项 -->
            <div class="browse-section">
                <h3 v-if="isSidebarOpen">Browse</h3>
                <ul>
                    <li :class="{ active: activeMenu === 'artist' }" @click="setActive('artist')">
                        <a href="#"><i class="fas fa-user" style="color: cornflowerblue;"></i> <span v-if="isSidebarOpen">歌手</span></a>
                    </li>
                    <li :class="{ active: activeMenu === 'album' }" @click="setActive('album')">
                        <a href="#"><i class="fa-solid fa-record-vinyl" style="color: darkorchid;"></i> <span v-if="isSidebarOpen">专辑</span></a>
                    </li>
                    <li :class="{ active: activeMenu === 'playlist' }" @click="setActive('playlist')">
                        <a href="#"><i class="fas fa-list" style="color:darkgreen ;"></i> <span v-if="isSidebarOpen">歌单</span></a>
                    </li>
                    <li :class="{ active: activeMenu === 'ranking' }" @click="setActive('ranking')">
                        <a href="#"><i class="fas fa-chart-line" style="color: purple;"></i> <span v-if="isSidebarOpen">排行榜</span></a>
                    </li>
                    <li :class="{ active: activeMenu === 'radio' }" @click="setActive('radio')">
                        <a href="#"><i class="fas fa-broadcast-tower" style="color: burlywood;"></i> <span v-if="isSidebarOpen">电台</span></a>
                    </li>
                    <li :class="{ active: activeMenu === 'movie' }" @click="setActive('movie')">
                        <a href="#"><i class="fas fa-film" style="color: brown;"></i> <span v-if="isSidebarOpen">影片</span></a>
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

            <!-- 新增的盒子2 -->
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
    </div>
    <div class="front-bottom">
        <!-- 左边控制区 -->
        <div class="front-bottom-left">
            <!-- 旋转唱片 -->
            <div class="album-cover" :class="{ rotating: isPlaying }">
                <img src="@/assets/img/default.png" alt="Album Cover" class="album-image" /> <!-- 占位图，可以后续替换 -->
            </div>
            <!-- 控制按钮 -->
            <button class="control-btn" @click="toggleShuffle" :class="{ active: isShuffle }">
                <i class="fas fa-random"></i> <!-- 随机播放图标 -->
            </button>
            <button class="control-btn">
                <i class="fas fa-step-backward"></i> <!-- 上一首图标 -->
            </button>
            <button class="control-btn play-pause-btn" @click="togglePlay">
                <i :class="isPlaying ? 'fas fa-pause' : 'fas fa-play'"></i> <!-- 播放/暂停切换图标 -->
            </button>
            <button class="control-btn">
                <i class="fas fa-step-forward"></i> <!-- 下一首图标 -->
            </button>
            <button class="control-btn" @click="toggleLoop" :class="{ active: isLoop }">
                <i class="fas fa-sync"></i> <!-- 循环播放图标 -->
            </button>
        </div>

        <!-- 中间进度条 -->
        <div class="front-bottom-center">
            <!-- 暂时注释掉时间显示 -->
            <span class="current-time">00:00</span>
            <input type="range" class="progress-bar" min="0" max="100" v-model="progress" @input="updateProgress" 
                :style="{ background: `linear-gradient(to right, #1db954 ${progress}%, #888 ${progress}%)` }"/>
            <span class="total-time">00:00</span>
        </div>

        <!-- 右边音量控制 -->
        <div class="front-bottom-right">
            <button class="control-btn" @click="toggleMute">
                <i :class="isMuted ? 'fas fa-volume-mute' : 'fas fa-volume-up'"></i> <!-- 静音/音量图标切换 -->
            </button>
            <input type="range" class="volume-slider" min="0" max="100" v-model="volume" @input="updateVolume" :disabled="isMuted" 
                    :style="{ background: `linear-gradient(to right, #1db954 ${volume}%, #888 ${volume}%)` }"/>
            <button class="control-btn">
                <i class="fas fa-music"></i> <!-- 歌词显示按钮 -->
            </button>
            <button class="control-btn">
                <i class="fas fa-list"></i> <!-- 播放列表按钮 -->
            </button>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            activeMenu: 'discover', // 默认选中“发现音乐”
            isSidebarOpen: true,    // 侧边栏初始状态为打开
            isPlaying: false,
            isShuffle: false, // 是否启用随机播放
            isLoop: false,    // 是否启用循环播放
            progress: 0, // 当前进度
            volume: 25, // 默认音量值
            previousVolume: 50, // 用于存储取消静音时恢复的音量
        };
    },
    methods: {
        setActive(menuItem) {
            this.activeMenu = menuItem; // 设置为当前点击的菜单项
            console.log('Active Menu:', this.activeMenu); // 调试输出当前激活的菜单
        },
        toggleSidebar() {
            this.isSidebarOpen = !this.isSidebarOpen; // 切换侧边栏的显示状态
        },
        togglePlayPause() {
            this.isPlaying = !this.isPlaying;
        },
        toggleShuffle() {
            this.isShuffle = !this.isShuffle; // 切换随机播放状态
        },
        toggleLoop() {
            this.isLoop = !this.isLoop; // 切换循环播放状态
        },
        toggleMute() {
            if (this.isMuted) {
                this.isMuted = false;
                this.volume = this.previousVolume; // 恢复之前的音量
            } else {
                this.isMuted = true;
                this.previousVolume = this.volume; // 记录当前音量
                this.volume = 0;
            }
        },
        updateVolume(event) {
            this.volume = event.target.value;
            if (this.volume == 0) {
                this.isMuted = true;
            } else {
                this.isMuted = false;
                this.previousVolume = this.volume; // 更新取消静音后的恢复音量
            }
        },
        updateProgress(event) {
            this.progress = event.target.value;
        }
    },
};
</script>

<style scoped>
.sidebar-container {
    display: flex;
    position: relative;
    height: 700px;
}

.toggle-sidebar-btn {
    position: absolute;
    top: 20px;
    left: 10px;
    background: none;
    border: none;
    color: white;
    font-size: 24px;
    cursor: pointer;
    z-index: 10;
    transition: transform 0.3s ease; /* 为按钮添加过渡效果 */
}

.sidebar {
    width: 180px;
    height: 100%; 
    background-color: #1c1c1c;
    display: flex;
    flex-direction: column;
    padding: 20px;
    color: white;
    transition: width 0.4s ease-in-out, padding 0.4s ease-in-out; /* 添加宽度和内边距的过渡效果 */
    overflow-y: auto; /* 允许垂直滚动 */
}

/* 针对 WebKit 浏览器（Chrome, Safari） */
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

.sidebar.collapsed {
    width: 30px;
    overflow-y: hidden; /* 折叠时隐藏滚动条 */
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
    transition: padding 0.4s ease-in-out; /* 为列表项的内边距添加过渡效果 */
}

.menu li a,
.browse-section li a {
    color: #ffffff;
    text-decoration: none;
    display: flex;
    align-items: center;
    width: 100%;
    padding-left: 10%; /* 左侧内边距 */
    transition: padding-left 0.4s ease-in-out; /* 为链接的左侧内边距添加过渡效果 */
}

.menu li.active span,
.browse-section li.active span {
    color: #1db954 !important; /* 设置激活状态下的 span 颜色 */
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
    transition: opacity 0.4s ease-in-out; /* 为标题添加过渡效果 */
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



.front-bottom {
    margin-top: 100px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #1c1c1c;
    padding: 10px 20px;
    color: #aaa; /* 默认文本灰色 */
}

.front-bottom-left,
.front-bottom-center,
.front-bottom-right {
    display: flex;
    align-items: center;
}

.album-cover {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background-color: #444;
    margin-right: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

.album-image {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    object-fit: cover;
}

.rotating {
    animation: rotate 4s linear infinite;
}

@keyframes rotate {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}

.control-btn {
    background: none;
    border: none;
    color: #888; /* 默认按钮灰色 */
    font-size: 20px;
    margin: 0 5px;
    cursor: pointer;
    transition: color 0.2s; /* 平滑过渡 */
}

.control-btn:hover,
.control-btn.active {
    color: #fff; /* 鼠标悬停或选中时变为亮白 */
}

.play-pause-btn {
    font-size: 24px;
}

.front-bottom-center {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
}

.current-time,
.total-time {
    font-size: 0.875rem;
    color: #aaa; /* 时间显示为浅灰色 */
    margin: 0 10px;
}

.progress-bar {
    width: 60%;
    height: 5px;
    border-radius: 5px;
    /* background: linear-gradient(to right, #1db954 var(--progress-value, 0%), #888 var(--progress-value, 0%)); */
    appearance: none;
    outline: none;
}

.progress-bar::-webkit-slider-thumb {
    appearance: none;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: #1db954;
    cursor: pointer;
}

.volume-slider {
    width: 80px;
    height: 5px;
    border-radius: 5px;
    /* background: linear-gradient(to right, #1db954 var(--volume-value, 50%), #888 var(--volume-value, 50%)); */
    appearance: none;
    outline: none;
    margin-left: 5px;
}

.volume-slider::-webkit-slider-thumb {
    appearance: none;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: #1db954;
    cursor: pointer;
}

.active {
    color: #fff; /* 选中状态颜色为亮白 */
}
</style>
