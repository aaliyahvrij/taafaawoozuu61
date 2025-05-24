import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from "@/views/AboutView.vue";
import HowView from "@/views/HowView.vue";
import ForumView from "@/views/ForumView.vue";
import FilterView from "@/views/FilterView.vue";
import CompareView from "@/views/CompareView.vue";

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
  ],
})

export default router
