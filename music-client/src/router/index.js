import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'HomeManager',
    redirect:'homepage',
    component: () => import('../views/HomeManager.vue'),
    children: [
      { path: 'testpage', name: 'TestPage', meta: { name: '测试页面' }, component: () => import('../views/BackState/TestPage.vue') },
      { path: 'dashboard', name: 'DashBoard', meta: { name: '仪表盘' }, component: () => import('../views/BackState/DashBoard.vue') },
      { path: 'userinfo', name: 'UserInfo', meta: { name: '用户信息' }, component: () => import('../views/BackState/UserInfo.vue') },
      { path: 'singerinfo', name: 'SingerInfo', meta: { name: '歌手信息' }, component: () => import('../views/BackState/SingerInfo.vue') },
      { path: 'songsinfo', name: 'SongsInfo', meta: { name: '歌曲信息' }, component: () => import('../views/BackState/SongsInfo.vue') },
      { path: 'songlist', name: 'SongList', meta: { name: '歌单信息' }, component: () => import('../views/BackState/SongList.vue') },
      { path: 'playlist', name: 'PlatList', meta: { name: '歌单歌曲' }, component: () => import('../views/BackState/PlayList.vue') },
      { path: 'collectinfo', name: 'CollectInfo', meta: { name: '收藏信息' }, component: () => import('../views/BackState/CollectInfo.vue') },
      { path: 'commentinfo', name: 'CommentInfo', meta: { name: '评论列表' }, component: () => import('../views/BackState/CommentInfo.vue') },
      
    ]
  },
  {
    path:'/homepage',
    name: 'HomePage',
    component: () => import('../views/HomePage.vue'),
    children:[
      // { path: '',  redirect: 'discovermusic' },
      { path: 'discovermusic', name: 'DiscoverMusic', meta: { name: '发现音乐' }, component: () => import('../views/Front/DiscoverMusic.vue') },
      { path: 'singerlist', name: 'SingerList', meta: { name: '歌手' }, component: () => import('../views/Front/SingerList.vue')},
      { path: 'singerdetail/:singerId', name: 'SingerDetail', component:() =>import('../views/Front/SingerDetail.vue')},
      { path: 'playlist', name: 'PlayList', meta: { name: '播放列表' }, component: () => import('../views/Front/PlayList.vue')},
      { path: 'searchresults', name: 'SearchResults', meta: { name: '搜索结果' }, component: () => import('../views/Front/SearchResults.vue') },
      { path: 'albumlist', name: 'AlbumList', meta: { name: '专辑页面' }, component: () => import('../views/Front/AlbumList.vue') },
      { path: 'albumdetail/:albumName', name: 'AlbumDetail', meta: { name: '专辑详情' }, component: () => import('../views/Front/AlbumDetail.vue') },
      { path: 'userprofile', name: 'UserProfile', meta: { name: '用户信息' }, component: () => import('../views/Front/UserProfile.vue') },
      { path: 'chatpage', name: 'ChatPage', meta: { name: '会话页面' }, component: () => import('../views/Front/ChatPage.vue') },
      { path: 'likemusic', name: 'LikeMusic', meta: { name: '用户喜欢' }, component: () => import('../views/Front/LikeMusic.vue') },
      { path: 'userlogin', name: 'UserLogin', meta: { name: '用户登录' }, component: () => import('../views/UserLogin.vue') },
      { path: 'userregister', name: 'UserRegister', meta: { name: '用户注册' }, component: () => import('../views/UserRegister.vue') },
      
    ]
  },
 
  { path: '/pagetext', name: 'PageText', meta: { name: '测试' }, component: () => import('../views/PageText.vue') },
  { path: '/adminlogin', name: 'AdminLogin', meta: { name: '管理员登录' }, component: () => import('../views/AdminLogin.vue') },

  

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
