const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  devServer:{
    port: 8080,
    client: {
      overlay: {
        warnings: false, // 不显示警告
        errors: true     // 显示错误
      }
    },
  },
 
}