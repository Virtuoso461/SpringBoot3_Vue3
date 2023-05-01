import {createRouter, createWebHistory} from 'vue-router'


const routes = [
    {
        path: '/',
        name: 'welcome',
        redirect: 'login',
        component: () => import('@/views/welcomeView'),
        children: [
            {
                path: 'login',
                name: 'welcome-login',
                component: () => import('@/components/welcome/LoginPage')
            },
            {
                path: 'register',
                name: 'welcome-register',
                component: () => import('@/components/welcome/Register')
            },
            {
                path: 'forget',
                name: 'welcome-forget',
                component: () => import('@/components/welcome/ForgetPage')
            },
        ]
    },
    {
        path: '/index',
        name: 'index',
        component: () => import('@/views/indexView')
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
