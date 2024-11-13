<template>
    <div class="music-list">
      <h2>Trending Songs</h2>
      <div class="songs">
        <div class="song" v-for="song in songs" :key="song.id">
          <img :src="song.imageUrl" alt="Album Art" />
          <div class="song-details">
            <h3>{{ song.title }}</h3>
            <p>{{ song.artist }}</p>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'MusicList',
    data() {
      return {
        songs: [
          { id: 1, title: 'Song 1', artist: 'Artist 1', imageUrl: 'https://via.placeholder.com/100' },
          { id: 2, title: 'Song 2', artist: 'Artist 2', imageUrl: 'https://via.placeholder.com/100' },
          { id: 3, title: 'Song 3', artist: 'Artist 3', imageUrl: 'https://via.placeholder.com/100' },
          { id: 4, title: 'Song 4', artist: 'Artist 4', imageUrl: 'https://via.placeholder.com/100' },
          { id: 5, title: 'Song 5', artist: 'Artist 5', imageUrl: 'https://via.placeholder.com/100' }
        ]
      };
    }
  };
  </script>
  
  <style scoped>
  .music-list {
    padding: 20px;
  }
  .songs {
    display: flex;
    flex-wrap: wrap;
  }
  .song {
    width: 200px;
    margin: 10px;
    display: flex;
    align-items: center;
    background-color: #fff;
    padding: 10px;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  .song img {
    width: 100px;
    height: 100px;
    margin-right: 10px;
    border-radius: 5px;
  }
  .song-details h3 {
    margin: 0;
  }
  .song-details p {
    margin: 5px 0 0;
    font-size: 14px;
    color: #666;
  }
  </style>
  