<template>
  <div>
    <h2 class="text-3xl font-heading text-white mb-6">影片票房</h2>

    <div class="glass-card p-6">
      <div v-if="movies.length === 0" class="text-center py-12 text-slate-400">暂无票房数据</div>

      <div v-else class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead>
            <tr class="border-b border-white/10 text-left text-slate-400">
              <th class="pb-3 pr-4">排名</th><th class="pb-3 pr-4">影片名称</th><th class="pb-3 pr-4">总票房</th><th class="pb-3 pr-4">售票数</th><th class="pb-3">场次数</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(movie, index) in movies" :key="movie.id" class="border-b border-white/5 last:border-0 hover:bg-white/5 transition-colors">
              <td class="py-3 pr-4">
                <span :class="['inline-flex items-center justify-center w-7 h-7 rounded-full text-xs font-heading',
                  index < 3 ? 'bg-cta/20 text-cta' : 'bg-white/5 text-slate-400']">{{ index + 1 }}</span>
              </td>
              <td class="py-3 pr-4 font-medium text-white">{{ movie.title }}</td>
              <td class="py-3 pr-4 text-cta font-heading">¥{{ (movie.revenue || 0).toFixed(2) }}</td>
              <td class="py-3 pr-4 text-slate-400">{{ movie.ticketCount || 0 }}</td>
              <td class="py-3 text-slate-400">{{ movie.scheduleCount || 0 }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMovieRevenue } from '@/api/finance'

const movies = ref([])

const loadData = async () => {
  try {
    const res = await getMovieRevenue()
    movies.value = res.data || []
  } catch (e) {
    console.error('加载票房数据失败', e)
  }
}

onMounted(() => loadData())
</script>
