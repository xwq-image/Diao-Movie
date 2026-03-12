<template>
  <div class="min-h-screen bg-gray-950">
    <NavBar />

    <!-- Hero -->
    <section class="relative pt-16 overflow-hidden">
      <div class="absolute inset-0 bg-gradient-to-b from-cyan-900/20 via-gray-950 to-gray-950" />
      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-[800px] h-[400px] bg-cta/10 rounded-full blur-[120px]" />

      <div class="relative max-w-7xl mx-auto px-6 pt-20 pb-16 text-center">
        <h1 class="text-5xl md:text-7xl font-heading text-white glow-text mb-4 animate-fade-in">
          票易达
        </h1>
        <p class="text-slate-400 text-lg md:text-xl max-w-xl mx-auto mb-8 animate-slide-up">
          沉浸式观影体验，一键选座购票
        </p>
        <router-link to="#movies" class="btn-cta text-lg !px-8 !py-4 inline-block animate-slide-up">
          立即选片
        </router-link>
      </div>
    </section>

    <!-- 正在热映 -->
    <section id="movies" class="max-w-7xl mx-auto px-6 pb-16">
      <h2 class="text-3xl font-heading text-white mb-8 flex items-center gap-3">
        <span class="w-1 h-8 bg-cta rounded-full" />
        正在热映
      </h2>

      <Skeleton v-if="loading" type="card" :count="4" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6" />

      <div v-else-if="nowShowingMovies.length" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
        <MovieCard
          v-for="movie in nowShowingMovies"
          :key="movie.id"
          :movie="movie"
          badge="热映"
          @click="goToDetail(movie.id)"
        />
      </div>
      <div v-else class="text-center py-16 text-slate-400">暂无热映影片</div>
    </section>

    <!-- 即将上映 -->
    <section class="max-w-7xl mx-auto px-6 pb-20">
      <h2 class="text-3xl font-heading text-white mb-8 flex items-center gap-3">
        <span class="w-1 h-8 bg-accent rounded-full" />
        即将上映
      </h2>

      <div v-if="comingSoonMovies.length" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
        <MovieCard
          v-for="movie in comingSoonMovies"
          :key="movie.id"
          :movie="movie"
          showDate
          @click="goToDetail(movie.id)"
        />
      </div>
      <div v-else class="text-center py-16 text-slate-400">暂无即将上映影片</div>
    </section>

    <!-- Footer -->
    <footer class="border-t border-white/5 py-8 text-center text-slate-400 text-sm">
      <p>票易达影院票务管理系统 &copy; 2026</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMovieList } from '@/api/movie'
import NavBar from '@/components/NavBar.vue'
import MovieCard from '@/components/MovieCard.vue'
import Skeleton from '@/components/Skeleton.vue'

const router = useRouter()

const nowShowingMovies = ref([])
const comingSoonMovies = ref([])
const loading = ref(true)

const loadMovies = async () => {
  loading.value = true
  try {
    const [res1, res2] = await Promise.all([
      getMovieList({ status: 1 }),
      getMovieList({ status: 2 })
    ])
    nowShowingMovies.value = res1.data || []
    comingSoonMovies.value = res2.data || []
  } catch (error) {
    console.error('加载影片失败', error)
  } finally {
    loading.value = false
  }
}

const goToDetail = (id) => router.push(`/movie/${id}`)

onMounted(() => loadMovies())
</script>
