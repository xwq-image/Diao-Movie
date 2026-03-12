<template>
  <div class="min-h-screen bg-gray-950 flex relative overflow-hidden">
    <!-- 左侧品牌区域 -->
    <div class="hidden lg:flex lg:w-1/2 relative items-center justify-center">
      <div class="absolute inset-0 bg-gradient-to-br from-cyan-900/30 via-gray-950 to-gray-950" />
      <div class="absolute top-1/4 left-1/3 w-[400px] h-[400px] bg-cta/15 rounded-full blur-[120px]" />
      <div class="absolute bottom-1/4 right-1/4 w-[300px] h-[300px] bg-cyan-400/10 rounded-full blur-[100px]" />

      <div class="relative z-10 text-center px-12">
        <div class="mb-8">
          <div class="inline-flex items-center justify-center w-20 h-20 rounded-2xl bg-gradient-to-br from-cyan-500 to-cyan-400 mb-6 shadow-lg shadow-cyan-500/30">
            <svg class="w-10 h-10 text-black" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z" />
            </svg>
          </div>
        </div>
        <h1 class="text-5xl font-heading text-white glow-text mb-4">票易达</h1>
        <p class="text-xl text-slate-400 mb-3">影院票务管理系统</p>
        <p class="text-slate-500 text-sm max-w-sm mx-auto leading-relaxed">
          沉浸式观影体验，一键选座购票<br>轻松管理，高效运营
        </p>

        <div class="mt-12 flex items-center justify-center gap-8 text-slate-500">
          <div class="text-center">
            <div class="text-2xl font-heading text-cta">100+</div>
            <div class="text-xs mt-1">热映影片</div>
          </div>
          <div class="w-px h-10 bg-white/10" />
          <div class="text-center">
            <div class="text-2xl font-heading text-cta">4</div>
            <div class="text-xs mt-1">豪华影厅</div>
          </div>
          <div class="w-px h-10 bg-white/10" />
          <div class="text-center">
            <div class="text-2xl font-heading text-cta">24h</div>
            <div class="text-xs mt-1">在线购票</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧登录区域 -->
    <div class="w-full lg:w-1/2 flex items-center justify-center px-6 relative">
      <div class="absolute top-1/3 right-1/4 w-[300px] h-[300px] bg-cta/8 rounded-full blur-[120px] lg:hidden" />

      <div class="w-full max-w-md relative">
        <!-- 移动端 Logo -->
        <div class="lg:hidden text-center mb-10">
          <div class="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-br from-cyan-500 to-cyan-400 mb-4 shadow-lg shadow-cyan-500/30">
            <svg class="w-8 h-8 text-black" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z" />
            </svg>
          </div>
          <h1 class="text-3xl font-heading text-white glow-text">票易达</h1>
        </div>

        <div class="glass-card p-8 md:p-10">
          <div class="mb-8">
            <h2 class="text-2xl font-heading text-white mb-2">欢迎回来</h2>
            <p class="text-slate-400 text-sm">登录您的账号，开启观影之旅</p>
          </div>

          <form @submit.prevent="handleLogin" class="space-y-5">
            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">手机号</label>
              <div class="relative">
                <span class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                  </svg>
                </span>
                <input v-model="form.phone" type="tel" placeholder="请输入手机号" maxlength="11" class="input-dark !pl-11" required>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">密码</label>
              <div class="relative">
                <span class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                  </svg>
                </span>
                <input v-model="form.password" type="password" placeholder="请输入密码" class="input-dark !pl-11" required>
              </div>
            </div>

            <button type="submit" :disabled="loading" class="btn-cta w-full !py-3.5 text-base flex items-center justify-center gap-2">
              <svg v-if="loading" class="w-5 h-5 animate-spin" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3" class="opacity-25" />
                <path d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" fill="currentColor" class="opacity-75" />
              </svg>
              {{ loading ? '登录中...' : '登 录' }}
            </button>
          </form>

          <div class="mt-8 flex items-center gap-4">
            <div class="flex-1 h-px bg-white/10" />
            <span class="text-slate-500 text-xs">还没有账号？</span>
            <div class="flex-1 h-px bg-white/10" />
          </div>

          <div class="mt-4">
            <router-link to="/register" class="block w-full text-center py-3 border border-white/10 text-white rounded-xl hover:bg-white/5 hover:border-cta/30 transition-all duration-200 text-sm">
              立即注册
            </router-link>
          </div>
        </div>

        <div class="mt-6 text-center">
          <router-link to="/" class="text-slate-500 hover:text-cta transition-colors text-sm inline-flex items-center gap-1 cursor-pointer">
            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
            </svg>
            返回首页
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/auth'
import { toast } from '@/utils/toast'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const form = ref({ phone: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    const res = await login(form.value)
    userStore.setToken(res.data.token)
    userStore.setUserInfo(res.data.userInfo)
    toast.success('登录成功')

    const redirect = route.query.redirect
    if (redirect) {
      router.push(redirect)
    } else {
      const role = res.data.userInfo.role
      if (['admin', 'seller', 'finance'].includes(role)) {
        router.push('/admin')
      } else {
        router.push('/')
      }
    }
  } catch (error) {
    console.error('登录失败', error)
  } finally {
    loading.value = false
  }
}
</script>
