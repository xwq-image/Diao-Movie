<template>
  <div>
    <h2 class="text-3xl font-heading text-white mb-6">资金流水</h2>

    <div class="glass-card p-4 mb-6">
      <div class="flex gap-4 items-center flex-wrap">
        <label class="text-sm font-medium text-slate-400">开始日期：</label>
        <input v-model="startDate" type="date" class="input-dark !w-auto">
        <label class="text-sm font-medium text-slate-400">结束日期：</label>
        <input v-model="endDate" type="date" class="input-dark !w-auto">
        <button @click="loadLogs" class="btn-cta !py-2.5 text-sm">查询</button>
      </div>
    </div>

    <div class="glass-card overflow-hidden">
      <table class="w-full">
        <thead class="bg-white/5">
          <tr>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">时间</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">用户ID</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">类型</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">金额</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">变动前余额</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">变动后余额</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">备注</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-white/5">
          <tr v-for="log in logs" :key="log.id" class="hover:bg-white/5 transition-colors">
            <td class="px-6 py-4 text-slate-400 text-sm">{{ formatTime(log.createTime) }}</td>
            <td class="px-6 py-4 text-white">{{ log.userId }}</td>
            <td class="px-6 py-4"><span :class="['px-3 py-1 rounded-full text-xs border', getTypeClass(log.type)]">{{ getTypeText(log.type) }}</span></td>
            <td class="px-6 py-4 font-medium" :class="log.type === 'consume' ? 'text-red-400' : 'text-green-400'">
              {{ log.type === 'consume' ? '-' : '+' }}¥{{ log.amount }}
            </td>
            <td class="px-6 py-4 text-slate-400">¥{{ log.balanceBefore }}</td>
            <td class="px-6 py-4 text-slate-400">¥{{ log.balanceAfter }}</td>
            <td class="px-6 py-4 text-slate-400 text-sm">{{ log.remark }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const startDate = ref(''), endDate = ref(''), logs = ref([])

const loadLogs = async () => {
  try { const params = {}; if (startDate.value) params.startDate = startDate.value; if (endDate.value) params.endDate = endDate.value;
    const r = await request.get('/finance/balance-logs', { params }); logs.value = r.data || [] } catch (e) { console.error(e) }
}

const getTypeText = (t) => ({ recharge: '充值', consume: '消费', refund: '退款' })[t] || t
const getTypeClass = (t) => ({ recharge: 'bg-green-500/10 text-green-400 border-green-500/20', consume: 'bg-red-500/10 text-red-400 border-red-500/20', refund: 'bg-blue-500/10 text-blue-400 border-blue-500/20' })[t] || 'bg-gray-500/10 text-gray-400 border-gray-500/20'
const formatTime = (t) => t ? new Date(t).toLocaleString('zh-CN') : ''

onMounted(() => {
  const today = new Date(), weekAgo = new Date(today); weekAgo.setDate(today.getDate() - 7)
  startDate.value = weekAgo.toISOString().split('T')[0]; endDate.value = today.toISOString().split('T')[0]
  loadLogs()
})
</script>
