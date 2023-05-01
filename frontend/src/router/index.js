import {createRouter, createWebHistory} from 'vue-router'
import {useStore} from "@/stores";


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

router.beforeEach((to, from, next) => {
    const store = useStore()
    if (store.auth.user != null && to.name.startsWith('welcome-')){
        next('/index')
    }else if (store.auth.user == null && to.fullPath.startsWith('/index')){
        next('/login')
    }else if (to.matched.length === 0){
        next('/index')
    }else {
        next()
    }
})

export default router
