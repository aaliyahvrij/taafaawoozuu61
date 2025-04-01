import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/how',
      name: 'how',
      //Todo: add how page view
      component: () => import('../'),
    },
    {
      path: '/filter',
      name: 'filter',
      component: () => import('../views/FilterView.vue'),
    },
    {
      path: '/compare',
      name: 'compare',
      //Todo: add compare view
      component: () => import('../'),
    },
    {
      path: '/forum',
      name: 'forum',
      //Todo: add forum view
      component: () => import('../'),
    },
  ],
})

export default router
