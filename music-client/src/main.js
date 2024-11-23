import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// import axios from 'axios';
import request from './utils/request'
import '@fortawesome/fontawesome-free/css/all.min.css';
//element plus icon图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'



const app = createApp(App)
app.use(ElementPlus)
// app.config.globalProperties.$axios = axios;
app.config.globalProperties.$request = request;
app.config.globalProperties.$baseUrl = process.env.VUE_APP_BASEURL;
app.use(store).use(router).mount('#app')

// 自动注册 ElementPlus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
