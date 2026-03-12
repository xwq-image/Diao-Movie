<template>
  <div class="min-h-screen bg-gray-950 flex relative overflow-hidden">
    <!-- 左侧品牌区域 -->
    <div class="hidden lg:flex lg:w-1/2 relative items-center justify-center">
      <div class="absolute inset-0 bg-gradient-to-br from-cyan-900/30 via-gray-950 to-gray-950" />
      <div class="absolute top-1/3 left-1/4 w-[400px] h-[400px] bg-cta/15 rounded-full blur-[120px]" />
      <div class="absolute bottom-1/3 right-1/3 w-[300px] h-[300px] bg-cyan-400/10 rounded-full blur-[100px]" />

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
          注册成为会员，享受专属优惠<br>海量影片，精彩不停
        </p>

        <div class="mt-12 grid grid-cols-3 gap-6 max-w-sm mx-auto">
          <div class="glass-card p-4 text-center">
            <svg class="w-6 h-6 text-cta mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z" />
            </svg>
            <div class="text-xs text-slate-400">在线购票</div>
          </div>
          <div class="glass-card p-4 text-center">
            <svg class="w-6 h-6 text-cta mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
            <div class="text-xs text-slate-400">自选座位</div>
          </div>
          <div class="glass-card p-4 text-center">
            <svg class="w-6 h-6 text-cta mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v13m0-13V6a2 2 0 112 2h-2zm0 0V5.5A2.5 2.5 0 109.5 8H12zm-7 4h14M5 12a2 2 0 110-4h14a2 2 0 110 4M5 12v7a2 2 0 002 2h10a2 2 0 002-2v-7" />
            </svg>
            <div class="text-xs text-slate-400">会员优惠</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧注册区域 -->
    <div class="w-full lg:w-1/2 flex items-center justify-center px-6 relative">
      <div class="absolute top-1/3 right-1/4 w-[300px] h-[300px] bg-cta/8 rounded-full blur-[120px] lg:hidden" />

      <div class="w-full max-w-md relative">
        <!-- 移动端 Logo -->
        <div class="lg:hidden text-center mb-8">
          <div class="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-br from-cyan-500 to-cyan-400 mb-4 shadow-lg shadow-cyan-500/30">
            <svg class="w-8 h-8 text-black" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z" />
            </svg>
          </div>
          <h1 class="text-3xl font-heading text-white glow-text">票易达</h1>
        </div>

        <div class="glass-card p-8 md:p-10">
          <div class="mb-8">
            <h2 class="text-2xl font-heading text-white mb-2">创建账号</h2>
            <p class="text-slate-400 text-sm">加入票易达，开启精彩观影之旅</p>
          </div>

          <form @submit.prevent="handleRegister" class="space-y-4">
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
              <label class="block text-sm font-medium text-slate-400 mb-2">昵称</label>
              <div class="relative">
                <span class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </span>
                <input v-model="form.nickname" type="text" placeholder="给自己取个名字吧" maxlength="20" class="input-dark !pl-11" required>
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
                <input v-model="form.password" type="password" placeholder="请输入密码（至少6位）" class="input-dark !pl-11" required>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">确认密码</label>
              <div class="relative">
                <span class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                  </svg>
                </span>
                <input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" class="input-dark !pl-11" required>
              </div>
            </div>

            <button type="submit" :disabled="loading" class="btn-cta w-full !py-3.5 text-base flex items-center justify-center gap-2 !mt-6">
              <svg v-if="loading" class="w-5 h-5 animate-spin" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3" class="opacity-25" />
                <path d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" fill="currentColor" class="opacity-75" />
              </svg>
              {{ loading ? '注册中...' : '立即注册' }}
            </button>
          </form>

          <div class="mt-6 flex items-center gap-4">
            <div class="flex-1 h-px bg-white/10" />
            <span class="text-slate-500 text-xs">已有账号？</span>
            <div class="flex-1 h-px bg-white/10" />
          </div>

          <div class="mt-4">
            <router-link to="/login" class="block w-full text-center py-3 border border-white/10 text-white rounded-xl hover:bg-white/5 hover:border-cta/30 transition-all duration-200 text-sm">
              立即登录
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
import { useRouter } from 'vue-router'
import { register } from '@/api/auth'
import { toast } from '@/utils/toast'

const router = useRouter()

const form = ref({ phone: '', nickname: '', password: '', confirmPassword: '' })
const loading = ref(false)

const handleRegister = async () => {
  if (form.value.password !== form.value.confirmPassword) {
    toast.warning('两次密码输入不一致')
    return
  }
  if (form.value.password.length < 6) {
    toast.warning('密码至少6位')
    return
  }
  loading.value = true
  try {
    await register({ phone: form.value.phone, nickname: form.value.nickname, password: form.value.password })
    toast.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    console.error('注册失败', error)
  } finally {
    loading.value = false
  }
}
</script>
