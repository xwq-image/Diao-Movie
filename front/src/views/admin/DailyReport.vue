<template>
  <div>
    <h2 class="text-3xl font-heading text-white mb-6">销售日报</h2>

    <div class="glass-card p-4 mb-6">
      <div class="flex gap-4 items-center">
        <label class="text-sm font-medium text-slate-400">选择日期：</label>
        <input v-model="selectedDate" type="date" class="input-dark !w-auto">
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
      <div class="glass-card p-6">
        <div class="text-sm text-slate-400 mb-2">今日票房</div>
        <div class="text-3xl font-heading text-cta glow-text">¥{{ report.totalRevenue || 0 }}</div>
      </div>
      <div class="glass-card p-6">
        <div class="text-sm text-slate-400 mb-2">订单数量</div>
        <div class="text-3xl font-heading text-white">{{ report.orderCount || 0 }}</div>
      </div>
      <div class="glass-card p-6">
        <div class="text-sm text-slate-400 mb-2">退票金额</div>
        <div class="text-3xl font-heading text-red-400">¥{{ report.refundAmount || 0 }}</div>
      </div>
      <div class="glass-card p-6">
        <div class="text-sm text-slate-400 mb-2">净收入</div>
        <div class="text-3xl font-heading text-green-400">¥{{ report.netRevenue || 0 }}</div>
      </div>
    </div>

    <div class="glass-card p-6">
      <h3 class="text-lg font-heading text-white mb-4">详细数据</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="space-y-3">
          <div class="flex justify-between py-2 border-b border-white/10"><span class="text-slate-400">线上订单</span><span class="text-white font-medium">{{ report.onlineOrderCount || 0 }} 笔</span></div>
          <div class="flex justify-between py-2 border-b border-white/10"><span class="text-slate-400">线下订单</span><span class="text-white font-medium">{{ report.offlineOrderCount || 0 }} 笔</span></div>
          <div class="flex justify-between py-2 border-b border-white/10"><span class="text-slate-400">售出票数</span><span class="text-white font-medium">{{ report.ticketCount || 0 }} 张</span></div>
        </div>
        <div class="space-y-3">
          <div class="flex justify-between py-2 border-b border-white/10"><span class="text-slate-400">线上收入</span><span class="text-cta font-medium">¥{{ report.onlineRevenue || 0 }}</span></div>
          <div class="flex justify-between py-2 border-b border-white/10"><span class="text-slate-400">线下收入</span><span class="text-cta font-medium">¥{{ report.offlineRevenue || 0 }}</span></div>
          <div class="flex justify-between py-2 border-b border-white/10"><span class="text-slate-400">退票笔数</span><span class="text-red-400 font-medium">{{ report.refundCount || 0 }} 笔</span></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import request from '@/utils/request'

const selectedDate = ref(new Date().toISOString().split('T')[0])
const report = ref({})

const loadReport = async () => { try { const r = await request.get('/finance/daily-report', { params: { date: selectedDate.value } }); report.value = r.data || {} } catch (e) { console.error(e) } }

watch(selectedDate, () => loadReport())
onMounted(() => loadReport())
</script>
