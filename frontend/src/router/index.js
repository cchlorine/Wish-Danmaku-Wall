import VueRouter from 'vue-router'

import Home from '../pages/Home'
import Compose from '../pages/Compose'
import Ranking from '../pages/Ranking'
import Story from '../pages/Story'
import Landing from '../pages/Landing'
import Wechat from '../pages/Wechat'

export default new VueRouter({
  routes: [
    {
      path: '/',
      component: Landing,
    },
    {
      path: '/home',
      component: Home,
    },
    {
      path: '/myWish',
      component: Compose
    },
    {
      path: '/rankingList',
      component: Ranking
    },
    {
      path: '/story',
      component: Story
    },
    {
      path: '/wechat',
      component: Wechat,
    },
    {
      path: '*',
      redirect: Landing,
    }
  ]
})
