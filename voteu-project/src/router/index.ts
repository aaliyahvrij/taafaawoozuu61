import { createRouter, createWebHistory } from 'vue-router'
import {
  HomeView,
  AboutView,
  HowView,
  LoginView,
  RegisterView,
  ForumView,
  FilterView,
  CompareView,
} from '@/views'

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
      component: AboutView,
    },
    {
      path: '/how',
      name: 'how',
      component: HowView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/forum',
      name: 'forum',
      component: ForumView,
    },
    {
      path: '/filter',
      name: 'filter',
      component: FilterView,
    },
    {
      path: '/compare',
      name: 'compare',
      component: CompareView,
    },
  ],
})

export default router
