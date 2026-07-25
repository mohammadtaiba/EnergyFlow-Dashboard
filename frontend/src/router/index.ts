import { createRouter, createWebHistory } from 'vue-router'
import MetersView from '@/views/MetersView.vue'
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
        {
            path: '/meters',
            name: 'meters',
            component: MetersView,
        },
    ],
})

export default router
