<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-3xl font-heading text-white">当日订单</h2>
      <button @click="loadOrders" class="btn-outline !py-2 !px-4 text-sm">刷新</button>
    </div>

    <div class="glass-card p-6">
      <div class="text-slate-400 text-sm mb-6">{{ today }} 的售票记录</div>

      <div v-if="orders.length === 0" class="text-center py-12 text-slate-400">今日暂无订单</div>

      <div v-else class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead>
            <tr class="border-b border-white/10 text-left text-slate-400">
              <th class="pb-3 pr-4">订单号</th><th class="pb-3 pr-4">金额</th><th class="pb-3 pr-4">座位数</th><th class="pb-3 pr-4">状态</th><th class="pb-3">创建时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in orders" :key="order.id" class="border-b border-white/5 last:border-0 hover:bg-white/5 transition-colors">
              <td class="py-3 pr-4 font-medium text-white">{{ order.orderNo }}</td>
              <td class="py-3 pr-4 text-cta font-heading">¥{{ order.totalPrice }}</td>
              <td class="py-3 pr-4 text-slate-400">{{ order.seatCount }}</td>
              <td class="py-3 pr-4"><span :class="['px-3 py-1 rounded-full text-xs border', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</span></td>
              <td class="py-3 text-slate-400">{{ formatTime(order.createTime) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOrderList } from '@/api/order'

const today = new Date().toISOString().split('T')[0]
const orders = ref([])

const loadOrders = async () => { try { const r = await getOrderList({ date: today }); orders.value = r.data || [] } catch (e) { console.error(e) } }

const getStatusText = (s) => ({ unpaid: '待付款', paid: '已付款', completed: '已完成', refunded: '已退款', cancelled: '已取消' })[s] || s
const getStatusClass = (s) => ({
  unpaid: 'bg-yellow-500/10 text-yellow-400 border-yellow-500/20',
  paid: 'bg-green-500/10 text-green-400 border-green-500/20',
  completed: 'bg-blue-500/10 text-blue-400 border-blue-500/20',
  refunded: 'bg-gray-500/10 text-gray-400 border-gray-500/20',
  cancelled: 'bg-red-500/10 text-red-400 border-red-500/20'
})[s] || 'bg-gray-500/10 text-gray-400 border-gray-500/20'

const formatTime = (t) => t ? new Date(t).toLocaleString('zh-CN') : ''

onMounted(() => loadOrders())
</script>
