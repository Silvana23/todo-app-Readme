import { createRouter, createWebHistory } from 'vue-router'

import Home from '@/views/HomeView.vue'
import Authentication from '@/views/AuthenticationView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Home
    },
    {
      path: '/authentication',
      component: Authentication
    }
  ]
})

export default router
