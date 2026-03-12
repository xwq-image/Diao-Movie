<template>
  <div class="min-h-screen bg-gray-950">
    <NavBar />

    <!-- 顶部 Banner -->
    <section class="relative pt-16">
      <div class="absolute inset-0 h-[450px] overflow-hidden">
        <img v-if="movie.poster" :src="movie.poster" class="w-full h-full object-cover opacity-20 blur-2xl scale-110" alt="">
        <div class="absolute inset-0 bg-gradient-to-b from-gray-950/60 via-gray-950/80 to-gray-950" />
      </div>

      <div class="relative max-w-7xl mx-auto px-6 pt-12 pb-8">
        <div class="flex flex-col md:flex-row gap-8">
          <div class="w-48 md:w-64 flex-shrink-0 mx-auto md:mx-0">
            <div class="aspect-[2/3] bg-gray-900 rounded-2xl overflow-hidden shadow-2xl shadow-black/50">
              <img v-if="movie.poster" :src="movie.poster" :alt="movie.title" class="w-full h-full object-cover">
            </div>
          </div>
          <div class="flex-1 pt-4">
            <h1 class="text-4xl md:text-5xl font-heading text-white glow-text mb-4">{{ movie.title }}</h1>
            <div class="flex flex-wrap gap-3 mb-4">
              <span v-if="movie.genre" class="px-3 py-1 rounded-full text-sm bg-cta/10 text-cta border border-cta/20">{{ movie.genre }}</span>
              <span v-if="movie.duration" class="px-3 py-1 rounded-full text-sm bg-white/5 text-slate-400 border border-white/10">{{ movie.duration }}分钟</span>
            </div>
            <div class="space-y-2 text-slate-400 text-sm mb-6">
              <p v-if="movie.director"><span class="text-white/70 mr-2">导演</span>{{ movie.director }}</p>
              <p v-if="movie.actors"><span class="text-white/70 mr-2">主演</span>{{ movie.actors }}</p>
              <p v-if="movie.releaseDate"><span class="text-white/70 mr-2">上映</span>{{ movie.releaseDate }}</p>
            </div>
            <p v-if="movie.description" class="text-slate-400 leading-relaxed text-sm max-w-2xl">{{ movie.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 选择日期 -->
    <section class="max-w-7xl mx-auto px-6 py-6">
      <h3 class="text-xl font-heading text-white mb-4">选择日期</h3>
      <div class="flex gap-3 overflow-x-auto pb-2">
        <button
          v-for="date in dates"
          :key="date.value"
          @click="selectedDate = date.value"
          :class="[
            'px-5 py-2.5 rounded-xl text-sm whitespace-nowrap transition-all duration-200 cursor-pointer border',
            selectedDate === date.value
              ? 'bg-cta text-black border-cta font-medium'
              : 'bg-gray-900 text-slate-400 border-white/10 hover:border-cta/50 hover:text-white'
          ]"
        >
          {{ date.label }}
        </button>
      </div>
    </section>

    <!-- 排期列表 -->
    <section class="max-w-7xl mx-auto px-6 pb-20">
      <h3 class="text-xl font-heading text-white mb-4">选择场次</h3>
      <div v-if="schedules.length === 0" class="text-center py-16 text-slate-400">暂无排期</div>
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
          v-for="schedule in schedules"
          :key="schedule.id"
          @click="goToSeat(schedule.id)"
          class="glass-card p-5 cursor-pointer hover:border-cta/40 hover:shadow-lg hover:shadow-cta/5 transition-all duration-300"
        >
          <div class="flex justify-between items-start mb-3">
            <div>
              <div class="text-2xl font-heading text-white">{{ schedule.startTime }}</div>
              <div class="text-xs text-slate-400 mt-1">{{ schedule.endTime }} 散场</div>
            </div>
            <div class="text-2xl font-heading text-cta">¥{{ schedule.price }}</div>
          </div>
          <div class="flex justify-between items-center text-sm text-slate-400">
            <span>{{ schedule.hallName }}</span>
            <span class="text-cta/80">剩余 {{ schedule.availableSeats }} 座</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getMovieDetail } from '@/api/movie'
import { getMovieSchedules } from '@/api/schedule'
import NavBar from '@/components/NavBar.vue'

const route = useRoute()
const router = useRouter()

const movie = ref({})
const dates = ref([])
const selectedDate = ref('')
const schedules = ref([])

const generateDates = () => {
  const result = []
  const today = new Date()
  for (let i = 0; i < 7; i++) {
    const date = new Date(today)
    date.setDate(today.getDate() + i)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const value = `${year}-${month}-${day}`
    const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
    const label = i === 0 ? '今天' : i === 1 ? '明天' : `${month}-${day} ${weekdays[date.getDay()]}`
    result.push({ value, label })
  }
  return result
}

const loadMovie = async () => {
  try {
    const res = await getMovieDetail(route.params.id)
    movie.value = res.data
  } catch (error) {
    console.error('加载影片详情失败', error)
  }
}

const loadSchedules = async () => {
  try {
    const res = await getMovieSchedules(route.params.id, selectedDate.value)
    schedules.value = res.data || []
  } catch (error) {
    console.error('加载排期失败', error)
  }
}

const goToSeat = (scheduleId) => router.push(`/seat/${scheduleId}`)

watch(selectedDate, () => loadSchedules())

onMounted(() => {
  dates.value = generateDates()
  selectedDate.value = dates.value[0].value
  loadMovie()
  loadSchedules()
})
</script>
