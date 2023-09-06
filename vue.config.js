const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  /* 关闭语法检查 */
  lintOnSave: false,
  // //放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录。
  assetsDir: 'public',
  // 部署应用包时的基本 URL。
  publicPath: '/',
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        // 是否跨域
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    host: 'localhost',
    port: 8080
  }
})
