const CosPlugin = require('cos-webpack')
const isProduction = process.env.NODE_ENV === 'production'
const fileName = `wish/${Date.now()}/`
const externals = {
  'vue': 'Vue',
  'vue-router': 'VueRouter',
  'axios': 'axios',
}

const cdn = {
  build: {
    js: [
      'https://lf9-cdn-tos.bytecdntp.com/cdn/expire-1-M/vue/2.6.11/vue.min.js',
      'https://lf26-cdn-tos.bytecdntp.com/cdn/expire-1-M/vue-router/3.5.3/vue-router.min.js',
      'https://lf3-cdn-tos.bytecdntp.com/cdn/expire-1-M/axios/0.26.0/axios.min.js',
    ]
  }
}

const cosPlugin = new CosPlugin({
  secretId: process.env.COS_SECRET_ID,
  secretKey: process.env.COS_SECRET_KEY,
  bucket: "wish-1309039959",
  region: "ap-shanghai",
  path: fileName
});

module.exports = {
  productionSourceMap: false,
  publicPath: isProduction ? `https://wish-1309039959.file.myqcloud.com/${fileName}dist` : '',
  chainWebpack: (config) => {
    config.plugin('html').tap(args => {
      if (isProduction) {
        args[0].cdn = cdn.build
      }

      return args
    })

    config.optimization.minimizer('terser').tap(args => {
      args[0].terserOptions.compress.drop_console = true

      return args
    })

    config.module
      .rule("images")
      .use("image-webpack-loader")
      .loader("image-webpack-loader")
      .options({
        mozjpeg: { progressive: true, quality: 65 },
        optipng: { enabled: false },
        pngquant: { quality: [0.65, 0.9], speed: 4 },
        gifsicle: { interlaced: false }
        // webp: { quality: 75 }
      });
  },

  configureWebpack: (config) => {
    if (process.env.NODE_ENV === 'production') {
      config.externals = externals
      config.plugins.push(cosPlugin)
    }
  }

}