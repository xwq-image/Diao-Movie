<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-3xl font-heading text-white">影片管理</h2>
      <button @click="showDialog = true; editingMovie = null; resetForm()" class="btn-cta !py-2.5 text-sm">添加影片</button>
    </div>

    <div class="glass-card overflow-hidden">
      <table class="w-full">
        <thead class="bg-white/5">
          <tr>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">片名</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">类型</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">时长</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">上映日期</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">状态</th>
            <th class="px-6 py-4 text-left text-sm font-medium text-slate-400">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-white/5">
          <tr v-for="movie in movies" :key="movie.id" class="hover:bg-white/5 transition-colors">
            <td class="px-6 py-4 text-white font-medium">{{ movie.title }}</td>
            <td class="px-6 py-4 text-slate-400">{{ movie.genre }}</td>
            <td class="px-6 py-4 text-slate-400">{{ movie.duration }}分钟</td>
            <td class="px-6 py-4 text-slate-400">{{ movie.releaseDate }}</td>
            <td class="px-6 py-4">
              <span :class="[
                'px-3 py-1 rounded-full text-xs border',
                movie.status === 1 ? 'bg-green-500/10 text-green-400 border-green-500/20' :
                movie.status === 2 ? 'bg-blue-500/10 text-blue-400 border-blue-500/20' :
                'bg-gray-500/10 text-gray-400 border-gray-500/20'
              ]">{{ getStatusText(movie.status) }}</span>
            </td>
            <td class="px-6 py-4">
              <button @click="editMovie(movie)" class="text-cta hover:text-cyan-300 mr-4 text-sm cursor-pointer">编辑</button>
              <button @click="deleteMovie(movie.id)" class="text-red-400 hover:text-red-300 text-sm cursor-pointer">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 弹窗 -->
    <div v-if="showDialog" class="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50" @click="showDialog = false">
      <div class="glass-card p-8 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto" @click.stop>
        <h3 class="text-2xl font-heading text-white mb-6">{{ editingMovie ? '编辑影片' : '添加影片' }}</h3>
        <form @submit.prevent="saveMovie" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-400 mb-2">片名</label>
            <input v-model="form.title" type="text" class="input-dark" required>
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">导演</label>
              <input v-model="form.director" type="text" class="input-dark">
            </div>
            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">主演</label>
              <input v-model="form.actors" type="text" class="input-dark">
            </div>
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">类型</label>
              <input v-model="form.genre" type="text" placeholder="如：科幻/动作" class="input-dark">
            </div>
            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">时长（分钟）</label>
              <input v-model.number="form.duration" type="number" class="input-dark" required>
            </div>
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">上映日期</label>
              <input v-model="form.releaseDate" type="date" class="input-dark">
            </div>
            <div>
              <label class="block text-sm font-medium text-slate-400 mb-2">状态</label>
              <select v-model.number="form.status" class="input-dark">
                <option :value="0">下架</option>
                <option :value="1">热映</option>
                <option :value="2">即将上映</option>
              </select>
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-400 mb-2">海报URL</label>
            <input v-model="form.poster" type="text" class="input-dark">
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-400 mb-2">剧情简介</label>
            <textarea v-model="form.description" rows="4" class="input-dark"></textarea>
          </div>
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
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { toast } from '@/utils/toast'

const movies = ref([])
const showDialog = ref(false)
const editingMovie = ref(null)
const form = ref({ title: '', director: '', actors: '', genre: '', duration: 0, releaseDate: '', poster: '', description: '', status: 1 })

const resetForm = () => { form.value = { title: '', director: '', actors: '', genre: '', duration: 0, releaseDate: '', poster: '', description: '', status: 1 } }

const loadMovies = async () => {
  try { const res = await request.get('/movie/list'); movies.value = res.data || [] }
  catch (e) { console.error('加载影片失败', e) }
}

const getStatusText = (s) => ({ 0: '下架', 1: '热映', 2: '即将上映' })[s] || ''

const editMovie = (movie) => { editingMovie.value = movie; form.value = { ...movie }; showDialog.value = true }

const saveMovie = async () => {
  try {
    if (editingMovie.value) { await request.put('/movie/update', form.value); toast.success('更新成功') }
    else { await request.post('/movie/create', form.value); toast.success('添加成功') }
    showDialog.value = false; loadMovies()
  } catch (e) { console.error('保存失败', e) }
}

const deleteMovie = async (id) => {
  if (!confirm('确定删除该影片吗？')) return
  try { await request.delete(`/movie/delete/${id}`); toast.success('删除成功'); loadMovies() }
  catch (e) { console.error('删除失败', e) }
}

onMounted(() => loadMovies())
</script>
