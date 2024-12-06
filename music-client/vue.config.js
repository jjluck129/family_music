const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

// module.exports = {
//   devServer: {
//       proxy: {
//           '/ws': {
//               target: 'http://localhost:9090',  // 后端 WebSocket 服务器地址
//               ws: true,  // 启用 WebSocket 代理
//               changeOrigin: true,
//           }
//       }
//   }
// }