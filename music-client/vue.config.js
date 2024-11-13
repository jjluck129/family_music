const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  devServer:{
    proxy :{
      '/api':{
        target:'http://localhost:9090',
        changeOrigin:true,
        pathRewrite:{'^/api':''}
      }
    }, 
    client: {
      overlay: {
        warnings: false, // 不显示警告
        errors: true     // 显示错误
      }
    },
  },
 
}