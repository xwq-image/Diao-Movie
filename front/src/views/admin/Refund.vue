<template>
  <div>
    <h2 class="text-3xl font-heading text-white mb-6">退票处理</h2>

    <div class="glass-card p-6">
      <div class="mb-6">
        <label class="block text-sm font-medium text-slate-400 mb-2">订单号查询</label>
        <div class="flex gap-4">
          <input v-model="orderNo" type="text" placeholder="请输入订单号" class="input-dark flex-1">
          <button @click="searchOrder" class="btn-cta whitespace-nowrap">查询</button>
        </div>
      </div>

      <div v-if="order" class="border border-white/10 rounded-xl p-6 bg-white/5">
        <div class="grid grid-cols-2 gap-4 mb-6 text-sm">
          <div><span class="text-slate-400">订单号：</span><span class="text-white font-medium">{{ order.orderNo }}</span></div>
          <div><span class="text-slate-400">金额：</span><span class="text-cta font-heading text-lg">¥{{ order.totalPrice }}</span></div>
          <div><span class="text-slate-400">座位数：</span><span class="text-white">{{ order.seatCount }}</span></div>
          <div><span class="text-slate-400">状态：</span><span :class="['px-3 py-1 rounded-full text-xs border', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</span></div>
        </div>
        <button v-if="order.status === 'paid'" @click="handleRefund"
          class="px-6 py-3 bg-red-500/20 text-red-400 border border-red-500/30 rounded-xl hover:bg-red-500/30 transition-all cursor-pointer">
          确认退票
        </button>
        <div v-else class="text-slate-400 text-sm">该订单状态不支持退票</div>
      </div>

      <div v-else-if="searched" class="text-center py-12 text-slate-400">未找到该订单</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { refundOrder } from '@/api/order'
import { toast } from '@/utils/toast'
import request from '@/utils/request'

const orderNo = ref(''), order = ref(null), searched = ref(false)

const searchOrder = async () => {
  if (!orderNo.value) { toast.warning('请输入订单号'); return }
  searched.value = true
  try { const r = await request.get('/order/list', { params: { orderNo: orderNo.value } }); const list = r.data || []; order.value = list.length > 0 ? list[0] : null }
  catch (e) { console.error(e); order.value = null }
}

const handleRefund = async () => {
  if (!confirm('确认为该订单办理退票吗？')) return
  try { await refundOrder({ orderId: order.value.id }); toast.success('退票成功'); searchOrder() }
  catch (e) { console.error(e) }
}

const getStatusText = (s) => ({ unpaid: '待付款', paid: '已付款', completed: '已完成', refunded: '已退款', cancelled: '已取消' })[s] || s
const getStatusClass = (s) => ({
  unpaid: 'bg-yellow-500/10 text-yellow-400 border-yellow-500/20', paid: 'bg-green-500/10 text-green-400 border-green-500/20',
  completed: 'bg-blue-500/10 text-blue-400 border-blue-500/20', refunded: 'bg-gray-500/10 text-gray-400 border-gray-500/20', cancelled: 'bg-red-500/10 text-red-400 border-red-500/20'
})[s] || 'bg-gray-500/10 text-gray-400 border-gray-500/20'
</script>
