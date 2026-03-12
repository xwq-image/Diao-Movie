<template>
  <div>
    <h2 class="text-3xl font-heading text-white mb-6">用户管理</h2>

    <div class="glass-card overflow-hidden">
      <table class="w-full">
        <thead class="bg-white/5">
          <tr>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">ID</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">手机号</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">昵称</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">余额</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">状态</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">注册时间</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-white/5">
          <tr v-for="user in users" :key="user.id" class="hover:bg-white/5 transition-colors">
            <td class="px-6 py-4 text-slate-400">{{ user.id }}</td>
            <td class="px-6 py-4 text-white">{{ user.phone }}</td>
            <td class="px-6 py-4 text-slate-400">{{ user.nickname }}</td>
            <td class="px-6 py-4 text-cta font-heading">¥{{ user.balance }}</td>
            <td class="px-6 py-4">
              <span :class="['px-3 py-1 rounded-full text-xs border', user.status === 1 ? 'bg-green-500/10 text-green-400 border-green-500/20' : 'bg-red-500/10 text-red-400 border-red-500/20']">
                {{ user.status === 1 ? '正常' : '禁用' }}
              </span>
            </td>
            <td class="px-6 py-4 text-slate-400 text-sm">{{ formatTime(user.createTime) }}</td>
            <td class="px-6 py-4">
              <button @click="toggleStatus(user)" :class="[user.status === 1 ? 'text-red-400 hover:text-red-300' : 'text-green-400 hover:text-green-300', 'text-sm cursor-pointer']">
                {{ user.status === 1 ? '禁用' : '启用' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { toast } from '@/utils/toast'

const users = ref([])
const loadUsers = async () => { try { const r = await request.get('/admin/users'); users.value = r.data || [] } catch (e) { console.error(e) } }
const formatTime = (t) => t ? new Date(t).toLocaleString('zh-CN') : ''

const toggleStatus = async (user) => {
  const action = user.status === 1 ? '禁用' : '启用'
  if (!confirm(`确定${action}该用户吗？`)) return
  try { await request.post('/admin/toggle-user-status', { userId: user.id, status: user.status === 1 ? 0 : 1 }); toast.success(`${action}成功`); loadUsers() }
  catch (e) { console.error(e) }
}

onMounted(() => loadUsers())
</script>
