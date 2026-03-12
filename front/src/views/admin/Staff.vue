<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-3xl font-heading text-white">员工管理</h2>
      <button @click="showDialog = true; resetForm()" class="btn-cta !py-2.5 text-sm">添加员工</button>
    </div>

    <div class="glass-card overflow-hidden">
      <table class="w-full">
        <thead class="bg-white/5">
          <tr>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">ID</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">手机号</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">姓名</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">角色</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">状态</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">创建时间</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-white/5">
          <tr v-for="s in staffList" :key="s.id" class="hover:bg-white/5 transition-colors">
            <td class="px-6 py-4 text-slate-400">{{ s.id }}</td>
            <td class="px-6 py-4 text-white">{{ s.phone }}</td>
            <td class="px-6 py-4 text-slate-400">{{ s.nickname }}</td>
            <td class="px-6 py-4">
              <span :class="['px-3 py-1 rounded-full text-xs border', getRoleClass(s.role)]">{{ getRoleText(s.role) }}</span>
            </td>
            <td class="px-6 py-4">
              <span :class="['px-3 py-1 rounded-full text-xs border', s.status === 1 ? 'bg-green-500/10 text-green-400 border-green-500/20' : 'bg-red-500/10 text-red-400 border-red-500/20']">
                {{ s.status === 1 ? '正常' : '禁用' }}
              </span>
            </td>
            <td class="px-6 py-4 text-slate-400 text-sm">{{ formatTime(s.createTime) }}</td>
            <td class="px-6 py-4">
              <button @click="resetPassword(s)" class="text-cta hover:text-cyan-300 mr-4 text-sm cursor-pointer">重置密码</button>
              <button @click="toggleStatus(s)" :class="[s.status === 1 ? 'text-red-400 hover:text-red-300' : 'text-green-400 hover:text-green-300', 'text-sm cursor-pointer']">
                {{ s.status === 1 ? '禁用' : '启用' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showDialog" class="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50" @click="showDialog = false">
      <div class="glass-card p-8 max-w-md w-full mx-4" @click.stop>
        <h3 class="text-2xl font-heading text-white mb-6">添加员工</h3>
        <form @submit.prevent="saveStaff" class="space-y-4">
          <div><label class="block text-sm font-medium text-slate-400 mb-2">手机号</label><input v-model="form.phone" type="tel" maxlength="11" placeholder="请输入手机号" class="input-dark" required></div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">姓名</label><input v-model="form.nickname" type="text" placeholder="请输入姓名" class="input-dark" required></div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">角色</label>
            <select v-model="form.role" class="input-dark" required>
              <option value="">请选择角色</option><option value="seller">售票员</option><option value="finance">财务人员</option><option value="admin">超级管理员</option>
            </select>
          </div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">初始密码</label><input v-model="form.password" type="password" placeholder="请输入初始密码（至少6位）" class="input-dark" required></div>
          <div class="flex gap-4 pt-4">
            <button type="button" @click="showDialog = false" class="btn-outline flex-1">取消</button>
            <button type="submit" class="btn-cta flex-1">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { toast } from '@/utils/toast'

const staffList = ref([])
const showDialog = ref(false)
const form = ref({ phone: '', nickname: '', role: '', password: '' })
const resetForm = () => { form.value = { phone: '', nickname: '', role: '', password: '' } }

const loadStaff = async () => { try { const r = await request.get('/admin/staff'); staffList.value = r.data || [] } catch (e) { console.error(e) } }

const getRoleText = (r) => ({ seller: '售票员', finance: '财务人员', admin: '超级管理员' })[r] || r
const getRoleClass = (r) => ({ seller: 'bg-blue-500/10 text-blue-400 border-blue-500/20', finance: 'bg-purple-500/10 text-purple-400 border-purple-500/20', admin: 'bg-red-500/10 text-red-400 border-red-500/20' })[r] || 'bg-gray-500/10 text-gray-400 border-gray-500/20'
const formatTime = (t) => t ? new Date(t).toLocaleString('zh-CN') : ''

const saveStaff = async () => {
  try { await request.post('/admin/create-staff', form.value); toast.success('添加成功'); showDialog.value = false; resetForm(); loadStaff() }
  catch (e) { console.error(e) }
}

const resetPassword = async (staff) => {
  const newPassword = prompt('请输入新密码（至少6位）')
  if (!newPassword || newPassword.length < 6) { toast.warning('密码至少6位'); return }
  try { await request.post('/admin/reset-password', { userId: staff.id, password: newPassword }); toast.success('密码重置成功') }
  catch (e) { console.error(e) }
}

const toggleStatus = async (staff) => {
  const action = staff.status === 1 ? '禁用' : '启用'
  if (!confirm(`确定${action}该员工吗？`)) return
  try { await request.post('/admin/toggle-user-status', { userId: staff.id, status: staff.status === 1 ? 0 : 1 }); toast.success(`${action}成功`); loadStaff() }
  catch (e) { console.error(e) }
}

onMounted(() => loadStaff())
</script>
