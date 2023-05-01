import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from "axios";

axios.defaults.baseURL = 'http://localhost:8888'

const app = createApp(App)
// 全局注册el-icon
for (const [name, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(name, component);
}
app.use(createPinia()).use(router).use(ElementPlus).mount('#app')
