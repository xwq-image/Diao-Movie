import { ref } from 'vue'

export const toasts = ref([])
let nextId = 0

function showToast(message, type = 'info', duration = 3000) {
  const toast = { id: ++nextId, message, type }
  toasts.value.push(toast)
  setTimeout(() => {
    toasts.value = toasts.value.filter(t => t.id !== toast.id)
  }, duration)
}

export const toast = {
  success: (msg) => showToast(msg, 'success'),
  error: (msg) => showToast(msg, 'error'),
  info: (msg) => showToast(msg, 'info'),
  warning: (msg) => showToast(msg, 'warning')
}
