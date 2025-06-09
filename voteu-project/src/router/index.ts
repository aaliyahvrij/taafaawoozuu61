import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from "@/views/AboutView.vue";
import HowView from "@/views/HowView.vue";
import ForumView from "@/views/ForumView.vue";
import FilterView from "@/views/FilterView.vue";
import CompareView from "@/views/CompareView.vue";
import AuthView from '@/views/AuthView.vue'
import login from '@/components/login.vue'
import register from '@/components/register.vue'
import AdminView from '@/views/AdminView.vue'

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
      path:'/how',
      name:'how',
      component: HowView,
    },
    {
      path:'/forum',
      name:'forum',
      component: ForumView,
    },
    {
      path:'/filter',
      name:'filter',
      component: FilterView,
    },
    {
      path:'/compare',
      name:'compare',
      component: CompareView,
    },
    {
      path:'/auth',
      name:'auth',
      component: AuthView,
    },

    {
      path:'/login',
      name:'login',
      component: login,
    },

    {
      path:'/register',
      name:'register',
      component: register,
    },

    {
      path:'/admin',
      name: 'admin',
      component: AdminView,
    },
  ],
})

export default router
