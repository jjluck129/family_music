import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios';
import '@fortawesome/fontawesome-free/css/all.min.css';



axios.defaults.baseURL = 'http://localhost:9090';
axios.defaults.withCredentials = true;
//element plus icon图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
app.use(ElementPlus)
app.config.globalProperties.$axios = axios;
app.use(store).use(router).mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
