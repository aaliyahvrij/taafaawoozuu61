import { createRouter, createWebHistory } from 'vue-router'
import {
  HomeView,
  AboutView,
  HowView,
  ForumView,
  FilterView,
  ComparisonView,
  RegistrationView,
  LoginView,
} from '@/views'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'About',
      component: AboutView,
    },
    {
      path: '/how',
      name: 'How',
      component: HowView,
    },
    {
      path: '/forum',
      name: 'Forum',
      component: ForumView,
    },
    {
      path: '/filter',
      name: 'Filter',
      component: FilterView,
    },
    {
      path: '/comparisin',
      name: 'Comparison',
      component: ComparisonView,
    },
    {
      path: '/registration',
      name: 'Registration',
      component: RegistrationView,
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginView,
    },
  ],
})

export default router
