// store.js
import { createStore } from 'vuex';

export default createStore({
  state: {
    currentSong: null,  // 当前播放的歌曲
    playlist: [],    // 播放列表
    isPlaying: false,   // 播放状态
    isShuffle: false,
    isLoop: false,
    // playMode: 'normal', //播放模式：'normal'（顺序播放）, 'random'（随机播放）, 'loop'（循环播放）
  },
  mutations: {
    // 设置歌曲
    setCurrentSong(state, song) {
      state.currentSong = song;
      state.isPlaying = true;  // 设置为播放状态
      console.log("当前歌曲",state.currentSong)
      // 检查歌曲是否已经在播放列表中
      const songExists = state.playlist.some(item => item.id === song.id);
      if (!songExists) {
        state.playlist.push(song);
      }
      if (state.playMode === 'loop' && !state.playlist.some(item => item.id === song.id)) {
        state.playlist.push(song);
      }
    },
    // 暂停
    pause(state) {
      state.isPlaying = false;
    },
    // 播放
    play(state) {
      state.isPlaying = true;
    },
    // 更新播放状态
    setPlayingState(state, isPlaying) {
      state.isPlaying = isPlaying;
    },
    // 更新播放列表
    setPlaylist(state, playlist) {
      state.playlist = playlist;
    },
    addToPlaylist(state, song) {
      // 如果歌单中不存在当前歌曲，就添加
      if (!state.playlist.some(item => item.id === song.id)) {
        state.playlist.push(song);
      }
    },
    // 播放下一首歌
    playNextSong(state) {
      const currentIndex = state.playlist.findIndex(song => song.id === state.currentSong.id);
      if (currentIndex < state.playlist.length - 1) {
        state.currentSong = state.playlist[currentIndex + 1];
      }
    },
    // 播放上一首歌
    previousSong(state){
      const currentIndex = state.playlist.findIndex(song => song.id === state.currentSong.id);
      if (currentIndex > 0){
        state.currentSong = state.playlist[currentIndex - 1];
      }
    },
    //清空播放列表
    clearPlaylist(state) {
      state.playlist = [];
    },
    setPlayMode(state, mode) {
      state.isShuffle = mode === 'shuffle';
      state.isLoop = mode === 'loop';
    },
  },
  actions: {
    playSong({ commit,state }, song) {
      commit('setCurrentSong', song); // 设置当前播放的歌曲
      if (state.playMode === 'random') {
        commit('shufflePlaylist');  // 随机播放时，打乱播放列表
      }
    },
    // 切换播放暂停
    togglePlayPause({ commit, state }) {
      commit('setPlayingState', !state.isPlaying);
    },
    nextSong({ commit, state }) {
      const playlist = state.playlist;
      if (playlist.length === 0) return; // 确保播放列表不为空

      let nextIndex;
      const currentIndex = playlist.findIndex(song => song.id === state.currentSong?.id);
      
      if (state.isShuffle) {
        // 随机播放，确保不会重复播放当前歌曲
        do {
          nextIndex = Math.floor(Math.random() * playlist.length);
        } while (nextIndex === currentIndex);
      } else {
        // 顺序播放，或循环播放
        nextIndex = (currentIndex + 1) % playlist.length;
      }
      
      commit('setCurrentSong', playlist[nextIndex]);
      commit('setPlaying', true); // 切换到下一首后自动播放
    },
    previousSong({ commit, state }) {
      const playlist = state.playlist;
      if (playlist.length === 0) return; // 确保播放列表不为空

      let prevIndex;
      const currentIndex = playlist.findIndex(song => song.id === state.currentSong?.id);
      
      if (state.isShuffle) {
        // 随机播放，确保不会重复播放当前歌曲
        do {
          prevIndex = Math.floor(Math.random() * playlist.length);
        } while (prevIndex === currentIndex);
      } else {
        // 顺序播放，或循环播放
        prevIndex = (currentIndex - 1 + playlist.length) % playlist.length;
      }
      
      commit('setCurrentSong', playlist[prevIndex]);
      commit('setPlaying', true); // 切换到上一首后自动播放
    },
    
    // 切换播放模式
    // togglePlayMode({ commit, state }) {
    //   let newMode = '';
    //   if (state.isShuffle) {
    //     newMode = 'loop';  // 如果当前是随机播放，切换到循环播放
    //   } else if (state.isLoop) {
    //     newMode = 'none';  // 如果当前是循环播放，切换到顺序播放
    //   } else {
    //     newMode = 'shuffle';  // 如果当前是顺序播放，切换到随机播放
    //   }
    //   commit('setPlayMode', newMode);
    //   // 更新播放列表顺序
    //   commit('setPlaylist', newMode === 'shuffle' ? this.shufflePlaylist(state.playlist) : state.playlist);
    // },
    togglePlayMode({ commit, state, dispatch }) {
      // 切换播放模式
      if (state.isShuffle) {
        commit('setPlayMode', 'order'); // 切换为顺序播放
        dispatch('restoreOrderPlaylist'); // 还原顺序播放列表
      } else {
        commit('setPlayMode', 'shuffle'); // 切换为随机播放
        dispatch('shufflePlaylist'); // 调用 shufflePlaylist 以打乱播放列表
      }
    },
    shufflePlaylist({ commit, state }) {
      const shuffled = [...state.playlist];
      for (let i = shuffled.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]]; // 交换
      }
      commit('setPlaylist', shuffled); // 更新播放列表为随机顺序
    },
    restoreOrderPlaylist({ commit, state }) {
      // 恢复播放列表为顺序
      // 假设 initialPlaylist 是初始顺序的播放列表
      commit('setPlaylist', state.initialPlaylist || [...state.playlist]);
    },
  },
  getters: {
    currentSong: (state) => state.currentSong,
    isPlaying: (state) => state.isPlaying,
    playlist: (state) => state.playlist,
  },
});
