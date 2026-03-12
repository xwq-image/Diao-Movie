<template>
  <div class="fixed top-6 right-6 z-[100] space-y-3 pointer-events-none">
    <TransitionGroup name="toast">
      <div
        v-for="t in toasts"
        :key="t.id"
        :class="[
          'pointer-events-auto px-6 py-4 rounded-xl shadow-lg text-white min-w-[280px] max-w-md',
          typeClasses[t.type]
        ]"
      >
        {{ t.message }}
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { toasts } from '@/utils/toast'

const typeClasses = {
  success: 'bg-green-600',
  error: 'bg-red-600',
  info: 'bg-cta',
  warning: 'bg-yellow-500'
}
</script>

<style scoped>
.toast-enter-active {
  transition: all 0.3s ease-out;
}
.toast-leave-active {
  transition: all 0.3s ease-in;
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(60px);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(60px);
}
</style>
