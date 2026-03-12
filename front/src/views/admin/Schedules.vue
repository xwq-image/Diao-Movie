<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-3xl font-heading text-white">排期管理</h2>
      <button @click="showDialog = true; editingSchedule = null; resetForm()" class="btn-cta !py-2.5 text-sm">添加排期</button>
    </div>

    <div class="glass-card p-4 mb-6">
      <div class="flex gap-4 items-center">
        <label class="text-sm font-medium text-slate-400">选择日期：</label>
        <input v-model="selectedDate" type="date" class="input-dark !w-auto">
      </div>
    </div>

    <div class="glass-card overflow-hidden">
      <table class="w-full">
        <thead class="bg-white/5">
          <tr>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">影片</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">影厅</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">日期</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">时间</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">票价</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-white/5">
          <tr v-for="s in schedules" :key="s.id" class="hover:bg-white/5 transition-colors">
            <td class="px-6 py-4 text-white font-medium">{{ s.movieTitle }}</td>
            <td class="px-6 py-4 text-slate-400">{{ s.hallName }}</td>
            <td class="px-6 py-4 text-slate-400">{{ s.showDate }}</td>
            <td class="px-6 py-4 text-slate-400">{{ s.startTime }} - {{ s.endTime }}</td>
            <td class="px-6 py-4 text-cta font-heading">¥{{ s.price }}</td>
            <td class="px-6 py-4">
              <button @click="editSchedule(s)" class="text-cta hover:text-cyan-300 mr-4 text-sm cursor-pointer">编辑</button>
              <button @click="deleteSchedule(s.id)" class="text-red-400 hover:text-red-300 text-sm cursor-pointer">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showDialog" class="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50" @click="showDialog = false">
      <div class="glass-card p-8 max-w-md w-full mx-4 max-h-[90vh] overflow-y-auto" @click.stop>
        <h3 class="text-2xl font-heading text-white mb-6">{{ editingSchedule ? '编辑排期' : '添加排期' }}</h3>
        <form @submit.prevent="saveSchedule" class="space-y-4">
          <div><label class="block text-sm font-medium text-slate-400 mb-2">影片</label>
            <select v-model="form.movieId" class="input-dark" required><option value="">请选择影片</option><option v-for="m in movies" :key="m.id" :value="m.id">{{ m.title }}</option></select>
          </div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">影厅</label>
            <select v-model="form.hallId" class="input-dark" required><option value="">请选择影厅</option><option v-for="h in halls" :key="h.id" :value="h.id">{{ h.name }}</option></select>
          </div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">放映日期</label><input v-model="form.showDate" type="date" class="input-dark" required></div>
          <div class="grid grid-cols-2 gap-4">
            <div><label class="block text-sm font-medium text-slate-400 mb-2">开始时间</label><input v-model="form.startTime" type="time" class="input-dark" required></div>
            <div><label class="block text-sm font-medium text-slate-400 mb-2">结束时间</label><input v-model="form.endTime" type="time" class="input-dark" required></div>
          </div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">票价（元）</label><input v-model.number="form.price" type="number" step="0.01" min="0" class="input-dark" required></div>
          <div class="flex gap-4 pt-4">
            <button type="button" @click="showDialog = false" class="btn-outline flex-1">取消</button>
            <button type="submit" class="btn-cta flex-1">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import request from '@/utils/request'
import { toast } from '@/utils/toast'

const schedules = ref([]), movies = ref([]), halls = ref([])
const showDialog = ref(false), editingSchedule = ref(null)
const selectedDate = ref(new Date().toISOString().split('T')[0])
const form = ref({ movieId: '', hallId: '', showDate: '', startTime: '', endTime: '', price: 0 })
const resetForm = () => { form.value = { movieId: '', hallId: '', showDate: '', startTime: '', endTime: '', price: 0 } }

const loadSchedules = async () => { try { const r = await request.get('/schedule/list', { params: { date: selectedDate.value } }); schedules.value = r.data || [] } catch (e) { console.error(e) } }
const loadMovies = async () => { try { const r = await request.get('/movie/list'); movies.value = r.data || [] } catch (e) { console.error(e) } }
const loadHalls = async () => { try { const r = await request.get('/hall/list'); halls.value = r.data || [] } catch (e) { console.error(e) } }

const editSchedule = (s) => { editingSchedule.value = s; form.value = { ...s }; showDialog.value = true }
const saveSchedule = async () => {
  try {
    if (editingSchedule.value) { await request.put('/schedule/update', form.value); toast.success('更新成功') }
    else { await request.post('/schedule/create', form.value); toast.success('添加成功') }
    showDialog.value = false; loadSchedules()
  } catch (e) { console.error(e) }
}
const deleteSchedule = async (id) => {
  if (!confirm('确定删除该排期吗？')) return
  try { await request.delete(`/schedule/delete/${id}`); toast.success('删除成功'); loadSchedules() } catch (e) { console.error(e) }
}

watch(selectedDate, () => loadSchedules())
onMounted(() => { loadSchedules(); loadMovies(); loadHalls() })
</script>
