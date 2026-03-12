import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/movie/:id',
    name: 'MovieDetail',
    component: () => import('@/views/MovieDetail.vue')
  },
  {
    path: '/seat/:scheduleId',
    name: 'SeatSelection',
    component: () => import('@/views/SeatSelection.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('@/views/Orders.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/Layout.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'seller', 'finance'] },
    children: [
      {
        path: '',
        redirect: to => {
          const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
          const role = userInfo.role
          if (role === 'seller') return '/admin/sell-ticket'
          if (role === 'finance') return '/admin/daily-report'
          return '/admin/movies'
        }
      },
      {
        path: 'movies',
        name: 'AdminMovies',
        component: () => import('@/views/admin/Movies.vue'),
        meta: { roles: ['admin'] }
      },
      {
        path: 'halls',
        name: 'AdminHalls',
        component: () => import('@/views/admin/Halls.vue'),
        meta: { roles: ['admin'] }
      },
      {
        path: 'schedules',
        name: 'AdminSchedules',
        component: () => import('@/views/admin/Schedules.vue'),
        meta: { roles: ['admin'] }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue'),
        meta: { roles: ['admin'] }
      },
      {
        path: 'staff',
        name: 'AdminStaff',
        component: () => import('@/views/admin/Staff.vue'),
        meta: { roles: ['admin'] }
      },
      {
        path: 'sell-ticket',
        name: 'SellTicket',
        component: () => import('@/views/admin/SellTicket.vue'),
        meta: { roles: ['seller', 'admin'] }
      },
      {
        path: 'today-orders',
        name: 'TodayOrders',
        component: () => import('@/views/admin/TodayOrders.vue'),
        meta: { roles: ['seller', 'admin'] }
      },
      {
        path: 'refund',
        name: 'AdminRefund',
        component: () => import('@/views/admin/Refund.vue'),
        meta: { roles: ['seller', 'admin'] }
      },
      {
        path: 'daily-report',
        name: 'DailyReport',
        component: () => import('@/views/admin/DailyReport.vue'),
        meta: { roles: ['finance', 'admin'] }
      },
      {
        path: 'monthly-report',
        name: 'MonthlyReport',
        component: () => import('@/views/admin/MonthlyReport.vue'),
        meta: { roles: ['finance', 'admin'] }
      },
      {
        path: 'movie-revenue',
        name: 'MovieRevenue',
        component: () => import('@/views/admin/MovieRevenue.vue'),
        meta: { roles: ['finance', 'admin'] }
      },
      {
        path: 'balance-log',
        name: 'BalanceLog',
        component: () => import('@/views/admin/BalanceLog.vue'),
        meta: { roles: ['finance', 'admin'] }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

  if (to.meta.requiresAuth && !token) {
    return next({ name: 'Login', query: { redirect: to.fullPath } })
  }

  if (to.meta.roles && to.meta.roles.length > 0) {
    if (!to.meta.roles.includes(userInfo.role)) {
      return next('/')
    }
  }

  next()
})

export default router
