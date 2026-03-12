<template>
  <div class="min-h-screen bg-gray-950">
    <NavBar />

    <div class="pt-24 pb-12">
      <div class="max-w-4xl mx-auto px-6">
        <h1 class="text-3xl font-heading text-white mb-8 flex items-center gap-3">
          <span class="w-1 h-8 bg-cta rounded-full" />
          个人中心
        </h1>

        <!-- 用户信息卡片 -->
        <div class="glass-card p-6 mb-6">
          <div class="flex items-center gap-5">
            <!-- 头像 -->
            <div class="relative group cursor-pointer" @click="triggerAvatarUpload">
              <div class="w-16 h-16 rounded-full overflow-hidden bg-gradient-to-br from-cta to-cyan-300 flex items-center justify-center text-2xl font-heading text-black">
                <img v-if="userInfo.avatar" :src="userInfo.avatar" class="w-full h-full object-cover" alt="头像">
                <span v-else>{{ (userInfo.nickname || 'U').charAt(0).toUpperCase() }}</span>
              </div>
              <div class="absolute inset-0 rounded-full bg-black/50 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                <svg class="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
              <input ref="avatarInput" type="file" accept="image/*" class="hidden" @change="handleAvatarChange">
            </div>

            <!-- 用户名 + 手机号 -->
            <div class="flex-1">
              <div class="flex items-center gap-2 mb-1">
                <h2 class="text-xl text-white font-semibold">{{ userInfo.nickname }}</h2>
                <button
                  v-if="userInfo.role === 'user'"
                  @click="showNicknameDialog = true; newNickname = userInfo.nickname"
                  class="text-slate-400 hover:text-cta transition-colors cursor-pointer"
                  title="修改昵称"
                >
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                  </svg>
                </button>
              </div>
              <p class="text-slate-400 text-sm">手机号：{{ userInfo.phone }}</p>
              <p class="text-slate-400 text-xs mt-0.5">角色：{{ roleText }}</p>
            </div>

            <!-- 余额 -->
            <div class="text-right">
              <div class="text-sm text-slate-400">账户余额</div>
              <div class="text-3xl font-heading text-cta glow-text">¥{{ (userInfo.balance || 0).toFixed(2) }}</div>
            </div>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- 充值 -->
          <div class="glass-card p-6">
            <h3 class="text-lg font-heading text-white mb-4">账户充值</h3>
            <div class="flex gap-3 flex-wrap mb-4">
              <button
                v-for="amount in [50, 100, 200, 500]"
                :key="amount"
                @click="rechargeAmount = amount"
                :class="[
                  'px-5 py-2 rounded-xl text-sm transition-all duration-200 border cursor-pointer',
                  rechargeAmount === amount
                    ? 'bg-cta text-black border-cta font-medium'
                    : 'bg-gray-900 text-slate-400 border-white/10 hover:border-cta/50'
                ]"
              >
                ¥{{ amount }}
              </button>
            </div>
            <div class="flex gap-3">
              <input v-model.number="rechargeAmount" type="number" placeholder="自定义金额" class="input-dark flex-1" min="1">
              <button @click="handleRecharge" class="btn-cta whitespace-nowrap">充值</button>
            </div>
          </div>

          <!-- 支付密码 -->
          <div class="glass-card p-6">
            <h3 class="text-lg font-heading text-white mb-4">修改支付密码</h3>
            <div class="space-y-3">
              <input v-model="passwordForm.oldPassword" type="password" placeholder="原支付密码" class="input-dark">
              <input v-model="passwordForm.newPassword" type="password" placeholder="新密码（6位数字）" maxlength="6" class="input-dark">
              <input v-model="passwordForm.confirmPassword" type="password" placeholder="确认新密码" maxlength="6" class="input-dark">
              <button @click="handleSetPayPassword" class="btn-cta w-full">确认修改</button>
            </div>
          </div>
        </div>

        <!-- 余额变动记录 -->
        <div class="glass-card p-6 mt-6">
          <h3 class="text-lg font-heading text-white mb-4">余额记录</h3>
          <div v-if="logs.length === 0" class="text-center py-8 text-slate-400">暂无记录</div>
          <div v-else class="space-y-3">
            <div v-for="log in logs" :key="log.id" class="flex items-center justify-between p-3 rounded-xl bg-gray-900/80 border border-white/5">
              <div>
                <div class="text-white text-sm">{{ log.remark || log.description }}</div>
                <div class="text-xs text-slate-400 mt-1">{{ log.createTime }}</div>
              </div>
              <div :class="[log.amount > 0 ? 'text-green-400' : 'text-red-400', 'font-semibold text-lg']">
                {{ log.amount > 0 ? '+' : '' }}{{ log.amount }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改昵称弹窗 -->
    <div v-if="showNicknameDialog" class="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50" @click="showNicknameDialog = false">
      <div class="glass-card p-6 max-w-sm w-full mx-4" @click.stop>
        <h3 class="text-lg font-heading text-white mb-4">修改昵称</h3>
        <input v-model="newNickname" type="text" placeholder="请输入新昵称" class="input-dark mb-4" maxlength="20">
        <div class="flex gap-3">
          <button @click="showNicknameDialog = false" class="btn-outline flex-1">取消</button>
          <button @click="handleUpdateNickname" class="btn-cta flex-1">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getUserInfo } from '@/api/auth'
import { setPayPassword, recharge, getBalanceLogs, updateNickname, uploadAvatar } from '@/api/user'
import { toast } from '@/utils/toast'
import NavBar from '@/components/NavBar.vue'

const userStore = useUserStore()
const userInfo = ref({})
const rechargeAmount = ref(100)
const logs = ref([])
const avatarInput = ref(null)
const showNicknameDialog = ref(false)
const newNickname = ref('')

const passwordForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })

const roleText = computed(() => {
  const map = { user: '普通用户', admin: '管理员', seller: '售票员', finance: '财务人员' }
  return map[userInfo.value.role] || userInfo.value.role
})

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    userInfo.value = res.data
  } catch (error) {
    console.error('加载用户信息失败', error)
  }
}

const loadLogs = async () => {
  try {
    const res = await getBalanceLogs()
    logs.value = res.data || []
  } catch (error) {
    console.error('加载余额记录失败', error)
  }
}

const triggerAvatarUpload = () => avatarInput.value?.click()

const handleAvatarChange = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 2 * 1024 * 1024) {
    toast.warning('图片大小不能超过2MB')
    return
  }
  try {
    const res = await uploadAvatar(file)
    userInfo.value.avatar = res.data.url
    toast.success('头像更新成功')
  } catch (error) {
    console.error('上传失败', error)
  }
  e.target.value = ''
}

const handleUpdateNickname = async () => {
  if (!newNickname.value.trim()) {
    toast.warning('昵称不能为空')
    return
  }
  try {
    await updateNickname({ nickname: newNickname.value.trim() })
    userInfo.value.nickname = newNickname.value.trim()
    userStore.setUserInfo({ ...userStore.userInfo, nickname: newNickname.value.trim() })
    toast.success('昵称修改成功')
    showNicknameDialog.value = false
  } catch (error) {
    console.error('修改昵称失败', error)
  }
}

const handleRecharge = async () => {
  if (!rechargeAmount.value || rechargeAmount.value <= 0) {
    toast.warning('请输入有效金额')
    return
  }
  try {
    await recharge({ amount: rechargeAmount.value })
    toast.success('充值成功')
    loadUserInfo()
    loadLogs()
  } catch (error) {
    console.error('充值失败', error)
  }
}

const handleSetPayPassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    toast.warning('两次密码不一致')
    return
  }
  if (passwordForm.value.newPassword.length !== 6) {
    toast.warning('密码为6位数字')
    return
  }
  try {
    await setPayPassword(passwordForm.value)
    toast.success('支付密码修改成功')
    passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } catch (error) {
    console.error('设置支付密码失败', error)
  }
}

onMounted(() => {
  loadUserInfo()
  loadLogs()
})
</script>
