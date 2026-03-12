<template>
  <div class="min-h-screen bg-gray-950">
    <NavBar />

    <div class="pt-24 pb-12">
      <div class="max-w-4xl mx-auto px-6">
        <h1 class="text-3xl font-heading text-white mb-8 flex items-center gap-3">
          <span class="w-1 h-8 bg-cta rounded-full" />
          我的订单
        </h1>

        <Skeleton v-if="loading" type="list" :count="3" />

        <div v-else-if="orders.length === 0" class="text-center py-20">
          <svg class="w-20 h-20 text-slate-400/30 mx-auto mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
              d="M15.75 10.5V6a3.75 3.75 0 10-7.5 0v4.5m11.356-1.993l1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 01-1.12-1.243l1.264-12A1.125 1.125 0 015.513 7.5h12.974c.576 0 1.059.435 1.119 1.007z" />
          </svg>
          <p class="text-slate-400 text-lg">暂无订单</p>
          <router-link to="/" class="btn-cta !py-2 !px-6 inline-block mt-4 text-sm">去选片</router-link>
        </div>

        <div v-else class="space-y-4">
          <div v-for="order in orders" :key="order.id" class="glass-card p-5 hover:border-white/20 transition-all duration-200">
            <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div class="flex-1 min-w-0">
                <div class="flex items-center gap-3 mb-2">
                  <h3 class="text-lg text-white font-semibold truncate">{{ order.movieTitle }}</h3>
                  <span :class="statusClass(order.status)" class="text-xs px-2.5 py-1 rounded-full whitespace-nowrap">
                    {{ statusText(order.status) }}
                  </span>
                </div>
                <div class="text-sm text-slate-400 space-y-1">
                  <p>{{ order.showDate }} {{ order.startTime }} | {{ order.hallName }}</p>
                  <p>座位：{{ order.seatInfo }}</p>
                  <p>订单号：{{ order.orderNo }}</p>
                </div>
              </div>

              <div class="flex items-center gap-4">
                <div class="text-right">
                  <div class="text-2xl font-heading text-cta">¥{{ order.totalPrice }}</div>
                </div>

                <div class="flex flex-col gap-2">
                  <button v-if="order.status === 'unpaid'" @click="payOrder(order)"
                    class="btn-cta !py-2 !px-4 text-sm whitespace-nowrap">
                    去支付
                  </button>
                  <button v-if="['unpaid','paid'].includes(order.status)" @click="refund(order)"
                    class="btn-outline !py-2 !px-4 text-sm whitespace-nowrap text-red-400 border-red-400/30 hover:bg-red-400/10 hover:border-red-400/50">
                    退票
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOrderList, payOrder as payOrderApi, refundOrder } from '@/api/order'
import { toast } from '@/utils/toast'
import NavBar from '@/components/NavBar.vue'
import Skeleton from '@/components/Skeleton.vue'

const orders = ref([])
const loading = ref(true)

const statusText = (status) => {
  const map = { unpaid: '待支付', paid: '已支付', refunded: '已退票', cancelled: '已取消' }
  return map[status] || status
}

const statusClass = (status) => {
  const map = {
    unpaid: 'bg-yellow-500/10 text-yellow-400 border border-yellow-500/20',
    paid: 'bg-green-500/10 text-green-400 border border-green-500/20',
    refunded: 'bg-gray-500/10 text-gray-400 border border-gray-500/20',
    cancelled: 'bg-red-500/10 text-red-400 border border-red-500/20',
  }
  return map[status] || ''
}

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await getOrderList()
    orders.value = res.data || []
  } catch (error) {
    console.error('加载订单失败', error)
  } finally {
    loading.value = false
  }
}

const payOrder = async (order) => {
  const payPassword = prompt('请输入支付密码')
  if (!payPassword) return
  try {
    await payOrderApi({ orderId: order.id, payPassword })
    toast.success('支付成功')
    loadOrders()
  } catch (error) {
    console.error('支付失败', error)
  }
}

const refund = async (order) => {
  if (!confirm('确认退票？退票后将退还金额到余额')) return
  try {
    await refundOrder({ orderId: order.id })
    toast.success('退票成功')
    loadOrders()
  } catch (error) {
    console.error('退票失败', error)
  }
}

onMounted(() => loadOrders())
</script>
