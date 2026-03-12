<template>
  <div class="min-h-screen bg-gray-950">
    <NavBar />

    <div class="pt-24 pb-12">
      <div class="max-w-7xl mx-auto px-6">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <!-- 座位图 -->
          <div class="lg:col-span-2">
            <div class="glass-card p-8">
              <h2 class="text-2xl font-heading text-white mb-6">选座购票</h2>

              <!-- 银幕 -->
              <div class="mb-8">
                <div class="h-1.5 bg-gradient-to-r from-transparent via-cta to-transparent rounded-full glow-box mb-2" />
                <div class="text-center text-xs text-slate-400">银幕中央</div>
              </div>

              <!-- 图例 -->
              <div class="flex justify-center gap-6 mb-6 text-sm">
                <div class="flex items-center gap-2">
                  <div class="w-6 h-6 bg-gray-800 border border-white/10 rounded" />
                  <span class="text-slate-400">可选</span>
                </div>
                <div class="flex items-center gap-2">
                  <div class="w-6 h-6 bg-cta rounded shadow-lg shadow-cta/30" />
                  <span class="text-slate-400">已选</span>
                </div>
                <div class="flex items-center gap-2">
                  <div class="w-6 h-6 bg-red-900/50 border border-red-800/30 rounded" />
                  <span class="text-slate-400">已售</span>
                </div>
              </div>

              <!-- 座位 -->
              <div class="overflow-x-auto">
                <div class="inline-block min-w-full">
                  <div v-for="row in seatRows" :key="row" class="flex justify-center gap-1.5 mb-1.5">
                    <div class="w-8 flex items-center justify-center text-xs text-slate-400">{{ row }}排</div>
                    <button
                      v-for="seat in getRowSeats(row)"
                      :key="seat.id"
                      @click="toggleSeat(seat)"
                      :disabled="seat.status === 'sold'"
                      :class="[
                        'w-7 h-7 rounded text-xs font-medium transition-all duration-200',
                        seat.status === 'sold'
                          ? 'bg-red-900/50 border border-red-800/30 text-red-400/50 cursor-not-allowed'
                          : isSelected(seat.id)
                            ? 'bg-cta text-black shadow-lg shadow-cta/30 cursor-pointer'
                            : 'bg-gray-800 border border-white/10 text-slate-400 hover:border-cta/50 hover:text-white cursor-pointer'
                      ]"
                    >
                      {{ seat.col }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 订单信息 -->
          <div class="lg:col-span-1">
            <div class="glass-card p-6 sticky top-24">
              <h3 class="text-lg font-heading text-white mb-4">订单信息</h3>

              <div class="space-y-3 mb-6 text-sm">
                <div class="flex justify-between">
                  <span class="text-slate-400">票价</span>
                  <span class="text-white font-medium">¥{{ price }}</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-slate-400">已选座位</span>
                  <span class="text-cta font-medium">{{ selectedSeats.length }} 个</span>
                </div>
                <div v-if="selectedSeats.length > 0" class="pt-3 border-t border-white/10">
                  <div class="text-slate-400 mb-2">座位详情</div>
                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="seat in selectedSeats"
                      :key="seat.id"
                      class="px-3 py-1 bg-cta/10 text-cta rounded-lg text-xs border border-cta/20"
                    >
                      {{ seat.row }}排{{ seat.col }}座
                    </span>
                  </div>
                </div>
              </div>

              <div class="border-t border-white/10 pt-4 mb-6">
                <div class="flex justify-between items-center">
                  <span class="text-lg text-white">总计</span>
                  <span class="text-3xl font-heading text-cta glow-text">¥{{ totalPrice }}</span>
                </div>
              </div>

              <button
                @click="confirmOrder"
                :disabled="selectedSeats.length === 0"
                :class="[
                  'w-full py-3 rounded-xl font-medium transition-all duration-200',
                  selectedSeats.length === 0
                    ? 'bg-gray-800 text-slate-400 cursor-not-allowed'
                    : 'btn-cta'
                ]"
              >
                确认选座
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getScheduleSeats } from '@/api/schedule'
import { createOrder } from '@/api/order'
import { toast } from '@/utils/toast'
import NavBar from '@/components/NavBar.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const price = ref(0)
const seats = ref([])
const selectedSeats = ref([])

const seatRows = computed(() => {
  const rows = new Set()
  seats.value.forEach(seat => rows.add(seat.row))
  return Array.from(rows).sort((a, b) => a - b)
})

const totalPrice = computed(() => (price.value * selectedSeats.value.length).toFixed(2))

const getRowSeats = (row) => seats.value.filter(s => s.row === row).sort((a, b) => a.col - b.col)
const isSelected = (seatId) => selectedSeats.value.some(s => s.id === seatId)

const toggleSeat = (seat) => {
  if (seat.status === 'sold') return
  const index = selectedSeats.value.findIndex(s => s.id === seat.id)
  if (index > -1) selectedSeats.value.splice(index, 1)
  else selectedSeats.value.push(seat)
}

const loadSeats = async () => {
  try {
    const res = await getScheduleSeats(route.params.scheduleId)
    price.value = res.data.price
    seats.value = res.data.seats
  } catch (error) {
    console.error('加载座位失败', error)
  }
}

const confirmOrder = async () => {
  if (!userStore.isLoggedIn) {
    toast.warning('请先登录')
    router.push('/login')
    return
  }
  if (selectedSeats.value.length === 0) {
    toast.warning('请选择座位')
    return
  }
  try {
    await createOrder({ scheduleId: route.params.scheduleId, seatIds: selectedSeats.value.map(s => s.id) })
    toast.success('订单创建成功')
    router.push('/orders')
  } catch (error) {
    console.error('创建订单失败', error)
  }
}

onMounted(() => loadSeats())
</script>
