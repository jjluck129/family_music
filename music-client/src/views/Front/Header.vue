<template>
    <header class="header">
      <h1>FamilyMood</h1>
      <input type="text" placeholder="Search for songs, artists..." />
    </header>
  </template>
  
  <script>
  export default {
    name: 'Header'
  };
  </script>
  
  <style scoped>
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    background-color: #282c34;
    color: white;
  }
  input {
    padding: 10px;
    border: none;
    border-radius: 5px;
    width: 300px;
  }
  </style>
  