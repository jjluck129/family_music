import axios from "axios";
import router from "@/router";

//创建一个新的axios对象
const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL,
    timeout: 30000,
    withCredentials: true  // 确保请求包含跨域的 cookie
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';        // 设置请求头格式
    if (config.data instanceof FormData) {
        delete config.headers['Content-Type'];
    }

    let user = JSON.parse(localStorage.getItem("family-user") || '{}')  // 获取缓存的用户信息
    console.log("正在获取本地存储的用户信息...", user);
    console.log("User token: ", user.token);
    config.headers['token'] = user.token  // 设置请求头
    console.log("已将token设置到请求头...", config.headers['token']);

    return config
}, error => {
    console.error('request error: ' + error) // for debug
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;

        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401') {
            router.push('/homepage/userlogin')
        }
        return res;
    },
    error => {
        console.error('response error: ' + error) // for debug
        return Promise.reject(error)
    }
)

export default request