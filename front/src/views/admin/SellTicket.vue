<template>
  <div>
    <h2 class="text-3xl font-heading text-white mb-6">前台售票</h2>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <div class="lg:col-span-2 space-y-6">
        <!-- 选择影片 -->
        <div class="glass-card p-6">
          <h3 class="text-lg font-heading text-white mb-4">选择影片</h3>
          <div class="grid grid-cols-2 gap-3">
            <button v-for="movie in movies" :key="movie.id" @click="selectMovie(movie)"
              :class="['p-4 rounded-xl border transition-all duration-200 cursor-pointer text-left',
                selectedMovie?.id === movie.id ? 'border-cta bg-cta/10' : 'border-white/10 hover:border-white/30 bg-white/5']">
              <div class="font-medium text-white">{{ movie.title }}</div>
              <div class="text-sm text-slate-400 mt-1">{{ movie.duration }}分钟</div>
            </button>
          </div>
        </div>

        <!-- 选择场次 -->
        <div v-if="selectedMovie" class="glass-card p-6">
          <h3 class="text-lg font-heading text-white mb-4">选择场次</h3>
          <div class="space-y-3">
            <button v-for="schedule in schedules" :key="schedule.id" @click="selectSchedule(schedule)"
              :class="['w-full p-4 rounded-xl border transition-all duration-200 cursor-pointer',
                selectedSchedule?.id === schedule.id ? 'border-cta bg-cta/10' : 'border-white/10 hover:border-white/30 bg-white/5']">
              <div class="flex justify-between items-center">
                <div><div class="font-medium text-white">{{ schedule.startTime }}</div><div class="text-sm text-slate-400">{{ schedule.hallName }} | 剩余{{ schedule.availableSeats }}座</div></div>
                <div class="text-xl font-heading text-cta">¥{{ schedule.price }}</div>
              </div>
            </button>
          </div>
        </div>

        <!-- 选择座位 -->
        <div v-if="selectedSchedule" class="glass-card p-6">
          <h3 class="text-lg font-heading text-white mb-4">选择座位</h3>
          <div class="mb-6"><div class="h-1.5 bg-gradient-to-r from-transparent via-cta to-transparent rounded-full glow-box mb-2" /><div class="text-center text-xs text-slate-400">银幕中央</div></div>
          <div class="flex justify-center gap-6 mb-4 text-sm">
            <div class="flex items-center gap-2"><div class="w-6 h-6 bg-gray-800 border border-white/10 rounded" /><span class="text-slate-400">可选</span></div>
            <div class="flex items-center gap-2"><div class="w-6 h-6 bg-cta rounded" /><span class="text-slate-400">已选</span></div>
            <div class="flex items-center gap-2"><div class="w-6 h-6 bg-red-900/50 border border-red-800/30 rounded" /><span class="text-slate-400">已售</span></div>
          </div>
          <div class="overflow-x-auto"><div class="inline-block min-w-full">
            <div v-for="row in seatRows" :key="row" class="flex justify-center gap-1.5 mb-1.5">
              <div class="w-8 flex items-center justify-center text-xs text-slate-400">{{ row }}排</div>
              <button v-for="seat in getRowSeats(row)" :key="seat.id" @click="toggleSeat(seat)" :disabled="seat.status === 'sold'"
                :class="['w-7 h-7 rounded text-xs font-medium transition-all duration-200',
                  seat.status === 'sold' ? 'bg-red-900/50 border border-red-800/30 text-red-400/50 cursor-not-allowed' :
                  isSelected(seat.id) ? 'bg-cta text-black shadow-lg shadow-cta/30 cursor-pointer' :
                  'bg-gray-800 border border-white/10 text-slate-400 hover:border-cta/50 cursor-pointer']">{{ seat.col }}</button>
            </div>
          </div></div>
        </div>
      </div>

      <!-- 订单信息 -->
      <div class="lg:col-span-1">
        <div class="glass-card p-6 sticky top-6">
          <h3 class="text-lg font-heading text-white mb-4">订单信息</h3>
          <div class="space-y-3 mb-6 text-sm">
            <div v-if="selectedMovie" class="pb-3 border-b border-white/10"><div class="text-slate-400 mb-1">影片</div><div class="text-white font-medium">{{ selectedMovie.title }}</div></div>
            <div v-if="selectedSchedule" class="pb-3 border-b border-white/10"><div class="text-slate-400 mb-1">场次</div><div class="text-white font-medium">{{ selectedSchedule.startTime }}</div></div>
            <div class="flex justify-between"><span class="text-slate-400">票价</span><span class="text-white">¥{{ selectedSchedule?.price || 0 }}</span></div>
            <div class="flex justify-between"><span class="text-slate-400">已选座位</span><span class="text-cta font-medium">{{ selectedSeats.length }} 个</span></div>
            <div v-if="selectedSeats.length > 0" class="pt-3 border-t border-white/10">
              <div class="text-slate-400 mb-2">座位详情</div>
              <div class="flex flex-wrap gap-2"><span v-for="seat in selectedSeats" :key="seat.id" class="px-3 py-1 bg-cta/10 text-cta rounded-lg text-xs border border-cta/20">{{ seat.row }}排{{ seat.col }}座</span></div>
            </div>
          </div>
          <div class="border-t border-white/10 pt-4 mb-6">
            <div class="flex justify-between items-center"><span class="text-lg text-white">总计</span><span class="text-2xl font-heading text-cta glow-text">¥{{ totalPrice }}</span></div>
          </div>
          <button @click="confirmOrder" :disabled="selectedSeats.length === 0"
            :class="['w-full py-3 rounded-xl font-medium transition-all duration-200', selectedSeats.length === 0 ? 'bg-gray-800 text-slate-400 cursor-not-allowed' : 'btn-cta']">
            现金收款
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getMovieList } from '@/api/movie'
import { getMovieSchedules, getScheduleSeats } from '@/api/schedule'
import { toast } from '@/utils/toast'
import request from '@/utils/request'

const movies = ref([]), selectedMovie = ref(null), schedules = ref([]), selectedSchedule = ref(null), seats = ref([]), selectedSeats = ref([])

const seatRows = computed(() => { const rows = new Set(); seats.value.forEach(s => rows.add(s.row)); return Array.from(rows).sort((a, b) => a - b) })
const totalPrice = computed(() => selectedSchedule.value ? (selectedSchedule.value.price * selectedSeats.value.length).toFixed(2) : 0)

const loadMovies = async () => { try { const r = await getMovieList({ status: 1 }); movies.value = r.data || [] } catch (e) { console.error(e) } }

const selectMovie = async (movie) => {
  selectedMovie.value = movie; selectedSchedule.value = null; selectedSeats.value = []
  try { const today = new Date().toISOString().split('T')[0]; const r = await getMovieSchedules(movie.id, today); schedules.value = r.data || [] } catch (e) { console.error(e) }
}

const selectSchedule = async (schedule) => {
  selectedSchedule.value = schedule; selectedSeats.value = []
  try { const r = await getScheduleSeats(schedule.id); seats.value = r.data.seats || [] } catch (e) { console.error(e) }
}

const getRowSeats = (row) => seats.value.filter(s => s.row === row).sort((a, b) => a.col - b.col)
const isSelected = (id) => selectedSeats.value.some(s => s.id === id)
const toggleSeat = (seat) => { if (seat.status === 'sold') return; const i = selectedSeats.value.findIndex(s => s.id === seat.id); if (i > -1) selectedSeats.value.splice(i, 1); else selectedSeats.value.push(seat) }

const confirmOrder = async () => {
  if (selectedSeats.value.length === 0) { toast.warning('请选择座位'); return }
  if (!confirm(`确认收款 ¥${totalPrice.value} 吗？`)) return
  try { await request.post('/seller/sell-ticket', { scheduleId: selectedSchedule.value.id, seatIds: selectedSeats.value.map(s => s.id) }); toast.success('售票成功'); selectedSeats.value = []; selectSchedule(selectedSchedule.value) } catch (e) { console.error(e) }
}

onMounted(() => loadMovies())
</script>
