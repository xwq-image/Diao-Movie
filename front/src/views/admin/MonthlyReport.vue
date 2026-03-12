<template>
  <div>
    <h2 class="text-3xl font-heading text-white mb-6">销售月报</h2>

    <div class="glass-card p-6">
      <div class="flex items-center gap-4 mb-6">
        <input v-model="month" type="month" class="input-dark !w-auto">
        <button @click="loadReport" class="btn-cta">查询</button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <div class="bg-white/5 rounded-xl p-6 text-center border border-white/10">
          <div class="text-slate-400 text-sm mb-2">总销售额</div>
          <div class="text-3xl font-heading text-cta glow-text">¥{{ report.totalRevenue || 0 }}</div>
        </div>
        <div class="bg-white/5 rounded-xl p-6 text-center border border-white/10">
          <div class="text-slate-400 text-sm mb-2">总订单数</div>
          <div class="text-3xl font-heading text-white">{{ report.totalOrders || 0 }}</div>
        </div>
        <div class="bg-white/5 rounded-xl p-6 text-center border border-white/10">
          <div class="text-slate-400 text-sm mb-2">退票数</div>
          <div class="text-3xl font-heading text-red-400">{{ report.refundCount || 0 }}</div>
        </div>
      </div>

      <div v-if="!report.totalOrders" class="text-center py-8 text-slate-400">暂无数据，请选择月份查询</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getMonthlyReport } from '@/api/finance'

const now = new Date()
const month = ref(`${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`)
const report = ref({})

const loadReport = async () => { try { const r = await getMonthlyReport({ month: month.value }); report.value = r.data || {} } catch (e) { console.error(e) } }
</script>
