<template>
  <div class="animate-pulse">
    <div v-if="type === 'card'" class="rounded-2xl overflow-hidden">
      <div class="aspect-[2/3] bg-gray-800 rounded-2xl" />
      <div class="mt-3 space-y-2">
        <div class="h-4 bg-gray-800 rounded w-3/4" />
        <div class="h-3 bg-gray-800 rounded w-1/2" />
      </div>
    </div>

    <div v-else-if="type === 'list'" class="space-y-4">
      <div v-for="i in count" :key="i" class="flex gap-4 p-4 rounded-xl bg-gray-900">
        <div class="w-16 h-16 rounded-lg bg-gray-800 flex-shrink-0" />
        <div class="flex-1 space-y-2">
          <div class="h-4 bg-gray-800 rounded w-2/3" />
          <div class="h-3 bg-gray-800 rounded w-1/3" />
        </div>
      </div>
    </div>

    <div v-else class="space-y-3">
      <div v-for="i in count" :key="i" class="h-4 bg-gray-800 rounded" :class="i === count ? 'w-2/3' : 'w-full'" />
    </div>
  </div>
</template>

<script setup>
defineProps({
  type: { type: String, default: 'text' },
  count: { type: Number, default: 3 },
})
</script>
