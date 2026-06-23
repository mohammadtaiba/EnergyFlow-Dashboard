import { createRouter, createWebHistory } from 'vue-router'
import SitesView from '@/views/SitesView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/sites',
        },
        {
            path: '/sites',
            name: 'sites',
            component: SitesView,
        },
    ],
})

export default router