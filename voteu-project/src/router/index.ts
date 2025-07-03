import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import HowView from "@/views/HowView.vue";
import ForumView from "@/views/ForumView.vue";
import FilterView from "@/views/FilterView.vue";
import CompareView from "@/views/CompareView.vue";
import AuthView from '@/views/AuthView.vue'
import login from '@/components/login.vue'
import register from '@/components/register.vue'
import PostDetailView from '@/views/PostDetailView.vue'
import AdminView from '@/views/AdminView.vue'
import { authService } from '@/services/AuthService.ts'
import ProfileView from '@/views/ProfileView.vue'
import PollingStationsOverview from '@/views/PollingStationsOverview.vue'
import PollingStationDetail from '@/views/PollingStationDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
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
      path: '/post/:id',
      name: 'postdetail',
      component: PostDetailView,
    },
    {
      path:'/filter',
      name:'filter',
      component: FilterView,
    },
    {
      path:'/pollingstations',
      name:'pollingstations',
      component: PollingStationsOverview
    },
    {
      path:'/pollingstations/:id',
      name:'pollingstation-detail',
      component: PollingStationDetail
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
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      beforeEnter: (to, from, next) => {
        const role = authService.getUserRole();
        if (role === 'USER') next();
        else next('/');
      }
    },

    {
      path:'/admin',
      name: 'admin',
      component: AdminView,
      beforeEnter: (to, from, next) => {
        const role = authService.getUserRole();
        if (role === 'ADMIN') next();
        else next('/');
      }
    },
  ],
})

export default router
