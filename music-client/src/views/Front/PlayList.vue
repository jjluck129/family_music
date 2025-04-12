<template>
  <div class="front-bottom">
    <!-- 左边控制区 -->
    <div class="front-bottom-left">
      <!-- 旋转唱片 -->
      <div class="album-cover" :class="{ rotating: isPlaying }">
        <img v-if="currentSong && currentSong.picUrl" :src="currentSong.picUrl" alt="Album Cover" class="album-image" />
      </div>
      <!-- 控制按钮 -->
      <button class="control-btn" @click="togglePlayMode" :class="{ active: isShuffle}">
        <i :class="playModeIconClass"></i> <!-- 随机播放图标 -->
      </button>
      <button class="control-btn" @click="previousSong">
        <i class="fas fa-step-backward"></i> <!-- 上一首图标 -->
      </button>
      <button class="control-btn play-pause-btn" @click="togglePlayPause">
        <i :class="isPlaying ? 'fas fa-pause' : 'fas fa-play'"></i> <!-- 播放/暂停切换图标 -->
      </button>
      <button class="control-btn" @click="nextSong">
        <i class="fas fa-step-forward"></i> <!-- 下一首图标 -->
      </button>
      <button class="control-btn" @click="toggleLoop" :class="{ active: isLoop }">
        <i class="fas fa-sync"></i> <!-- 循环播放图标 -->
      </button>
    </div>

    <!-- 中间进度条 -->
    <div class="front-bottom-center">
      <span class="current-time">{{ currentTime }}</span>
      <input
        type="range"
        class="progress-bar"
        min="0"
        :max="totalTime"
        :value="progress"
        @mousedown="startDrag"
        @mouseup="endDrag"
        @input="onProgressBarChange"
        :style="{ background: `linear-gradient(to right, #1db954 ${progress}%, #888 ${progress}%)` }"
      />
      <span class="total-time">{{ totalTime }}</span>
    </div>

    <!-- 右边音量控制 -->
    <div class="front-bottom-right">
      <button class="control-btn" @click="toggleMute">
        <i :class="isMuted ? 'fas fa-volume-mute' : 'fas fa-volume-up'"></i> <!-- 静音/音量图标切换 -->
      </button>
      <input
        type="range"
        class="volume-slider"
        min="0"
        max="100"
        v-model="volume"
        @input="updateVolume"
        :disabled="isMuted"
        :style="{ background: `linear-gradient(to right, #1db954 ${volume}%, #888 ${volume}%)` }"
      />
      <button class="control-btn" @click="toggleLyricsDisplay">
        <i class="fas fa-music"></i> <!-- 歌词显示按钮 -->
      </button>
      <button class="control-btn" @click="togglePlaylist">
        <i class="fas fa-list"></i> <!-- 播放列表按钮 -->
      </button>
      <!-- 播放列表弹窗 -->
      <div v-if="isPlaylistOpen" class="playlist-modal"  ref="playlist"> 
        <div class="playlist-content">
          <button class="close-btn" @click="togglePlaylist">×</button>
          <h3>播放列表</h3>
          <ul>
            <li v-for="song in playList" :key="song.id">
              <span class="playlist-content-songname">{{ song.songName }}</span>
              <span class="playlist-content-singername">{{ song.singerName }}</span>
              <span class="playlist-content-delete">
                <!-- <button @click="deletePlayItem(index)" class="playlist-content-delete"> -->
                  <i class="fa-regular fa-trash-can" @click="removeSong(song.id)"></i>
                <!-- </button> -->
              </span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!-- 播放音频 -->
    <audio 
        ref="audio" 
        :src="currentSong ? currentSong.mp3Url : ''" 
        @timeupdate="handleTimeUpdate" 
        @ended="handleSongEnd" 
        :autoplay="isPlaying" 
        :volume="volume / 100"
        @play="onPlay" @pause="onPause"
    ></audio>
    <!-- 歌词显示区域 -->
    <div v-if="showLyrics" class="lyrics-container" ref="lyrics">
      <div
        v-for="(line, index) in displayedLyrics"
        :key="index"
        :ref="'lyricItem' + index"
        :class="{
          'current-lyric': line.isCurrent,
          'passed-lyric': !line.isCurrent && line.isPassed,
        }"
        class="lyric-item"
      >
        {{ line.text }}
        <span v-if="line.isCurrent && line.timeToNext" class="time-to-next">({{ line.timeToNext }}s)</span>
      </div>
    </div>
  </div>
  
</template>

<script>
export default {
  data() {
    return {
      progress: 0, // 当前进度
      volume: 35, // 默认音量值
      isMuted: false, // 静音状态
      currentTime: '00:00', // 当前播放时间
      totalTime: '00:00', // 总时长
      isDragging: false, // 是否正在拖动
      displayedLyrics: [], // 用于存储当前显示的歌词
      showLyrics: false,
      playList:[],
      isPlaylistOpen: false,
      isKeydownListenerAdded: false
    };
  },
  computed: {
    currentSong() {
      return this.$store.getters.currentSong; // 从 Vuex 获取当前歌曲信息
    },
    isPlaying() {
      return this.$store.getters.isPlaying; // 获取播放状态
    },
    audioDuration() {
      return this.$refs.audio ? this.$refs.audio.duration : 0;
    },
    isShuffle() {
      return this.$store.state.isShuffle;
    },
    isLoop() {
      return this.$store.state.isLoop;
    },
    // 动态计算播放模式的图标
    playModeIconClass() {
      if (this.isShuffle) {
        return 'fas fa-random'; // 随机播放图标
      } else {
        return 'fas fa-list-ol'; // 顺序播放图标
      }
    },
  },
  mounted() {
    this.setTotalTime();
    window.addEventListener('keydown', (event) => this.handleKeydown(event));
  },
  beforeUnmount() {
   
    // 组件销毁时移除事件监听器
    window.removeEventListener('keydown', (event) => this.handleKeydown(event));
  },
  watch:{
    currentSong(newSong) {
    if (newSong) {
      this.setTotalTime();
      this.setPlayList();
    }
  }
  },
  methods: {
    togglePlayMode() {
      this.$store.dispatch('togglePlayMode');
    },
    togglePlayPause() {
      // 播放/暂停
      const audio = this.$refs.audio;
      if (this.isPlaying) {
        audio.pause();
      } else {
        audio.play();
      }
      this.isPlaying = !this.isPlaying; // 切换播放状态
    },
    handleKeydown(event) {
      if (event.code === 'Space') {
        this.togglePlayPause();
      }
    },
    onPlay() {
      this.$store.commit('play');  // 播放时更新状态
    },
    onPause() {
      this.$store.commit('pause'); // 暂停时更新状态
    },
    toggleLoop() {
      this.$store.commit('setPlayMode', this.isLoop ? 'none' : 'loop');
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
        this.previousVolume = this.volume;
      }
    },
    
    handleSongEnd() {
      if (this.isLoop) {
        this.$refs.audio.play(); // 循环播放时重新播放当前歌曲
      } else {
        this.nextSong(); // 播放完毕，自动播放下一首（如果有）
      }
    },
    nextSong() {
      this.$store.dispatch("nextSong");
    },
    previousSong() {
      this.$store.dispatch("previousSong");
    },
    
    togglePlaylist() {
      if(this.showLyrics) this.showLyrics = !this.showLyrics
      this.isPlaylistOpen = !this.isPlaylistOpen;
      console.log(this.playList)
    },
    toggleLyricsDisplay() {
      if(this.isPlaylistOpen) this.isPlaylistOpen = !this.isPlaylistOpen;
      this.showLyrics = !this.showLyrics; // 切换歌词显示状态
    },
    // updateProgress() {
    //   const audio = this.$refs.audio;
    //   this.progress = audio.currentTime / audio.duration * 100;
    //   audio.currentTime = (this.progress / 100) * audio.duration;
    // },
    setPlayList(){
      this.playList = this.$store.getters.playlist;
    },

    // 进度拖动
    startDrag() {
      this.isDragging = true;
    },
    endDrag() {
      this.isDragging = false;
      const audio = this.$refs.audio;
      audio.currentTime = (this.progress / 100) * audio.duration;
    },
    onProgressBarChange(event) {
      this.progress = event.target.value;
    },
    // 获取歌词内的时间总长
    setTotalTime() {
      if (this.currentSong) {
        const lyrics = this.currentSong.lyrics;
        this.totalTime = this.extractTotalTime(lyrics);
      }
    },
    extractTotalTime(lyrics) {
      if (!lyrics) return "00:00";
      const timePattern = /\[(\d{2}):(\d{2})\.(\d+)\]/g;  //  [mm:ss.xxx] 时间戳
      const timestamps = lyrics.match(timePattern);

      if (!timestamps) return "00:00";
      const lastTimestamp = timestamps[timestamps.length - 1];// 最后一个时间戳
      const formattedTimestamp = lastTimestamp.slice(1, -1); // 例如 "03:49.834"
      const [minutes, seconds] = formattedTimestamp.split(":");
      return `${minutes}:${seconds.split(".")[0]}`; // 返回 "03:49" 格式
    },
    //更新进度条和时间
    handleTimeUpdate() {
      if (!this.isDragging) {
      const audio = this.$refs.audio;
      this.progress = (audio.currentTime / audio.duration) * 100;
      const minutes = Math.floor(audio.currentTime / 60);
      const seconds = Math.floor(audio.currentTime % 60);
      this.currentTime = `${minutes}:${seconds < 10 ? '0' + seconds : seconds}`;
      this.updateLyrics(audio.currentTime);}
    },
    updateLyrics(currentTime) {
      const lyrics = this.currentSong.lyrics;
      const timePattern = /\[(\d{2}):(\d{2})\.(\d+)\](.*)/g;
      const matches = [...lyrics.matchAll(timePattern)];

      // 解析并生成当前显示的歌词行
      const parsedLyrics = matches
        .map(match => {
          const minutes = parseInt(match[1], 10);
          const seconds = parseInt(match[2], 10);
          const milliseconds = Math.round(parseInt(match[3], 10) / 10); // 四舍五入到两位数

          const roundedMilliseconds = milliseconds.toString().padStart(2, '0'); // 补齐两位
          const timestamp = parseFloat(`${minutes * 60 + seconds}.${roundedMilliseconds}`);
          const text = match[4].trim();

          // 过滤掉空行或无效歌词
          if (!text || isNaN(timestamp)) return null;

          return { text, timestamp };
        })
        .filter(line => line !== null); // 移除空行

      // 更新显示的歌词状态
      const displayed = parsedLyrics.map((line, index) => {
        const nextTimestamp = parsedLyrics[index + 1]?.timestamp || Infinity;
        const isCurrent = currentTime >= line.timestamp && currentTime < nextTimestamp;
        const isPassed = currentTime > line.timestamp;

        return {
          ...line,
          isCurrent,
          isPassed,
        };
      });

      this.displayedLyrics = displayed;

      this.$nextTick(() => {
        // 获取当前歌词行的索引
        const currentLyricIndex = this.displayedLyrics.findIndex(line => line.isCurrent);
        if (currentLyricIndex !== -1) {
          // 使用 `Array.isArray` 检查 `this.$refs['lyricItem' + currentLyricIndex]`
          const currentLyric = this.$refs['lyricItem' + currentLyricIndex];
          
          // 如果是数组，取第一个元素
          const element = Array.isArray(currentLyric) ? currentLyric[0] : currentLyric;
          
          // 确保 `element` 是一个 DOM 元素，并滚动到该元素
          if (element && element.scrollIntoView) {
            element.scrollIntoView({ behavior: 'smooth', block: 'center' });
          }
        }
      });
    },
    removeSong(songId) {
      this.$store.dispatch('removeSong',songId);
    }

  },
};
</script>
<style scoped>
.front-bottom {
    /* position: fixed;
    bottom: 0;
    left: 0.67cap;
    right: 0.67cap;
    width: 96%; */
    position: relative;
    margin-top: -41px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #1c1c1c;
    padding: 10px 20px;
    color: #aaa;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px; 
}

.front-bottom-left,
.front-bottom-center,
.front-bottom-right {
    display: flex;
    align-items: center;
    z-index: 99;
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

.playlist-modal {
  position: fixed;
  bottom: 0;
  right: 2%;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 20px;
  z-index: 10000;
  max-height: 50%;
  overflow-y: auto;
  margin-bottom: 78px;
    width: 50%;

}

.playlist-content {
  background-color: #222;
  padding: 20px;
  border-radius: 10px;
}

.playlist-content h3 {
  margin: 0;
  padding-bottom: 10px;
}

.playlist-content ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.playlist-content li {
  padding: 10px 0;
  border-bottom: 1px solid #444;
}

.playlist-content .close-btn {
  background: none;
  border: none;
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  position: absolute;
  top: 10px;
  right: 10px;
}

.playlist-content .close-btn:hover {
  color: #f00;
}

.playlist-content-songname{
  width: 130px;
  position: absolute;
  left: 26%;
}

.playlist-content-singername{
  position: relative;
  left: 30px;
}
.playlist-content-delete{
  position: absolute;
  left: 66%;
  padding: 2px;
  cursor: pointer;
}

.lyrics-container {
  position: fixed;
  bottom: 0;
  right: 23%;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 20px;
  z-index: 100;
  max-height: 50%;
  overflow-y: auto;
  margin-bottom: 78px;
  width: 50%;
}

.lyric-item {
  font-size: 18px;
  padding: 8px;
  line-height: 1.6;
  transition: color 0.3s ease;
}

.current-lyric {
  color: greenyellow;
  font-weight: bold;
}

.passed-lyric {
  color: green;
}

.lyrics-container::-webkit-scrollbar {
    width: 6px;
}
.lyrics-container::-webkit-scrollbar-track {
  background-color: #f0f0f0;
}
.lyrics-container::-webkit-scrollbar-thumb {
    background-color: green;
}
</style>