import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './App.vue'
import router from './router'
import { initAxios } from './services/axios'
import lottie from 'lottie-web'
import paperPlane from './assets/lotties/paperplane.json'
import { initSdk } from './services/sdk'

Vue.use(VueRouter)

const loadingContainer = document.querySelector('#loading')
const loadingLottie = lottie.loadAnimation({
  container: loadingContainer,
  renderer: "canvas",
  loop: true,
  autoplay: true,
  animationData: paperPlane,
})

let canRender = false

Vue.prototype.hideLoading = () => {
  setTimeout(() => {
    loadingLottie.pause()
    loadingContainer.classList.add('hide')
  }, 200)
}



initAxios(Vue)
  .then(() => {
    if ('fonts' in document) {
      document.fonts.ready.then(renderVue)
    } else if (canRender) {
      renderVue()
    } else {
      window.addEventListener('load', renderVue)
    }
  })

function renderVue() {
  const app = new Vue({
    render: h => h(App),
    router: router
  })

  app.$mount('#app')

  try {
    initSdk(app)
  } catch (err) {
    console.error(err)
  }

  if (!app.isInWechat) {
    return app.$router.push('/wechat')
  }

  if (!app.userInfo.initialized && !window.location.hash.includes('story')) {
    app.$router.push('/story')
  }

  return app
}

if (!('fonts' in document)) {
  window.addEventListener('load', () => {
    canRender = true
  })
}
