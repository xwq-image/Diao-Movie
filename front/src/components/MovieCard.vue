<template>
  <div
    @click="$emit('click')"
    class="group relative overflow-hidden rounded-2xl cursor-pointer card-hover"
  >
    <div class="aspect-[2/3] bg-gray-900 overflow-hidden">
      <img
        v-if="movie.poster"
        :src="movie.poster"
        :alt="movie.title"
        class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
        loading="lazy"
      >
      <div v-else class="w-full h-full flex items-center justify-center">
        <svg class="w-16 h-16 text-slate-400/30" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
            d="M7 4v16M17 4v16M3 8h4m10 0h4M3 12h18M3 16h4m10 0h4M4 20h16a1 1 0 001-1V5a1 1 0 00-1-1H4a1 1 0 00-1 1v14a1 1 0 001 1z" />
        </svg>
      </div>
    </div>

    <div class="absolute inset-0 bg-gradient-to-t from-black/90 via-black/30 to-transparent opacity-80 group-hover:opacity-100 transition-opacity duration-300" />

    <div class="absolute bottom-0 left-0 right-0 p-4">
      <h3 class="text-white font-semibold text-lg mb-1 line-clamp-1">{{ movie.title }}</h3>
      <div class="flex items-center gap-2 text-sm">
        <span v-if="movie.genre" class="text-cta">{{ movie.genre }}</span>
        <span v-if="movie.duration" class="text-slate-400">{{ movie.duration }}分钟</span>
      </div>
      <p v-if="showDate && movie.releaseDate" class="text-slate-400 text-xs mt-1">
        {{ movie.releaseDate }} 上映
      </p>
    </div>

    <div v-if="badge" class="absolute top-3 right-3">
      <span class="px-3 py-1 rounded-full text-xs font-medium bg-cta/90 text-black backdrop-blur-sm">
        {{ badge }}
      </span>
    </div>
  </div>
</template>

<script setup>
defineProps({
  movie: { type: Object, required: true },
  badge: { type: String, default: '' },
  showDate: { type: Boolean, default: false },
})

defineEmits(['click'])
</script>
