<template>
  <nav class="fixed top-0 left-0 right-0 z-50 glass-nav">
    <div class="max-w-7xl mx-auto px-6 py-4">
      <div class="flex items-center justify-between">
        <router-link to="/" class="text-2xl font-heading text-white tracking-wide hover:text-cta transition-colors duration-200">
          票易达
        </router-link>

        <div class="flex items-center gap-2">
          <template v-if="!userStore.isLoggedIn">
            <router-link to="/login" class="btn-outline text-sm !py-2 !px-4">登录</router-link>
            <router-link to="/register" class="btn-cta text-sm !py-2 !px-4">注册</router-link>
          </template>
          <template v-else>
            <router-link
              to="/orders"
              class="px-4 py-2 text-sm text-slate-400 hover:text-white transition-colors duration-200 cursor-pointer"
            >
              我的订单
            </router-link>
            <router-link
              to="/profile"
              class="px-4 py-2 text-sm text-slate-400 hover:text-white transition-colors duration-200 cursor-pointer"
            >
              个人中心
            </router-link>
            <router-link
              v-if="['admin','seller','finance'].includes(userStore.role)"
              to="/admin"
              class="px-4 py-2 text-sm text-cta hover:text-cyan-300 transition-colors duration-200 cursor-pointer"
            >
              管理后台
            </router-link>
            <button
              @click="handleLogout"
              class="px-4 py-2 text-sm text-slate-400 hover:text-red-400 transition-colors duration-200 cursor-pointer"
            >
              退出
            </button>
          </template>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>
