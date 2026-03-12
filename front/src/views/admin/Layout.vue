<template>
  <div class="min-h-screen bg-gray-950 flex">
    <!-- 侧边栏 -->
    <aside class="w-64 bg-gray-900/80 backdrop-blur-xl border-r border-white/10 fixed left-0 top-0 bottom-0 overflow-y-auto z-40">
      <div class="p-6">
        <router-link to="/" class="text-2xl font-heading text-white glow-text mb-8 block hover:text-cta transition-colors">
          票易达
        </router-link>

        <!-- 用户信息 -->
        <div class="mb-8 pb-6 border-b border-white/10">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 rounded-full bg-gradient-to-br from-cta to-cyan-300 flex items-center justify-center text-sm font-heading text-black">
              {{ (userStore.userInfo.nickname || 'U').charAt(0).toUpperCase() }}
            </div>
            <div>
              <div class="font-medium text-white text-sm">{{ userStore.userInfo.nickname }}</div>
              <div class="text-xs text-cta">{{ getRoleText(userStore.userInfo.role) }}</div>
            </div>
          </div>
        </div>

        <!-- 菜单 -->
        <nav class="space-y-1">
          <template v-if="userStore.userInfo.role === 'admin'">
            <SidebarItem to="/admin/movies" icon="film">影片管理</SidebarItem>
            <SidebarItem to="/admin/halls" icon="building">影厅管理</SidebarItem>
            <SidebarItem to="/admin/schedules" icon="calendar">排期管理</SidebarItem>
            <SidebarItem to="/admin/users" icon="users">用户管理</SidebarItem>
            <SidebarItem to="/admin/staff" icon="shield">员工管理</SidebarItem>
          </template>

          <template v-if="userStore.userInfo.role === 'seller'">
            <SidebarItem to="/admin/sell-ticket" icon="ticket">前台售票</SidebarItem>
            <SidebarItem to="/admin/today-orders" icon="list">当日订单</SidebarItem>
            <SidebarItem to="/admin/refund" icon="undo">退票处理</SidebarItem>
          </template>

          <template v-if="userStore.userInfo.role === 'finance'">
            <SidebarItem to="/admin/daily-report" icon="chart">销售日报</SidebarItem>
            <SidebarItem to="/admin/monthly-report" icon="calendar">销售月报</SidebarItem>
            <SidebarItem to="/admin/movie-revenue" icon="star">影片票房</SidebarItem>
            <SidebarItem to="/admin/balance-log" icon="wallet">资金流水</SidebarItem>
          </template>
        </nav>

        <div class="mt-8 pt-6 border-t border-white/10">
          <button
            @click="logout"
            class="w-full flex items-center gap-3 px-4 py-3 text-left text-red-400 hover:bg-red-500/10 rounded-xl transition-all duration-200 cursor-pointer text-sm"
          >
            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            退出登录
          </button>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="flex-1 ml-64 p-8">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { h } from 'vue'
import { useRouter, RouterLink, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const getRoleText = (role) => {
  const map = { admin: '超级管理员', seller: '售票员', finance: '财务人员', user: '普通用户' }
  return map[role] || role
}

const logout = () => {
  userStore.logout()
  router.push('/login')
}

const iconPaths = {
  film: 'M7 4v16M17 4v16M3 8h4m10 0h4M3 12h18M3 16h4m10 0h4M4 20h16a1 1 0 001-1V5a1 1 0 00-1-1H4a1 1 0 00-1 1v14a1 1 0 001 1z',
  building: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4',
  calendar: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z',
  users: 'M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z',
  shield: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z',
  ticket: 'M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z',
  list: 'M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01',
  undo: 'M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6',
  chart: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z',
  star: 'M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z',
  wallet: 'M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z',
}

const SidebarItem = (props, { slots }) => {
  const isActive = route.path === props.to
  return h(
    RouterLink,
    {
      to: props.to,
      class: [
        'flex items-center gap-3 px-4 py-3 rounded-xl text-sm transition-all duration-200 cursor-pointer',
        isActive
          ? 'bg-cta/10 text-cta border-l-2 border-cta'
          : 'text-slate-400 hover:text-white hover:bg-white/5'
      ].join(' ')
    },
    () => [
      h('svg', { class: 'w-4 h-4 flex-shrink-0', fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', innerHTML: `<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="${iconPaths[props.icon] || ''}" />` }),
      slots.default?.()
    ]
  )
}
SidebarItem.props = ['to', 'icon']
</script>
