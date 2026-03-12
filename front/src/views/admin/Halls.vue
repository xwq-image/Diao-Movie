<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-3xl font-heading text-white">影厅管理</h2>
      <button @click="showDialog = true; editingHall = null; resetForm()" class="btn-cta !py-2.5 text-sm">添加影厅</button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="hall in halls" :key="hall.id" class="glass-card p-6 card-hover">
        <h3 class="text-xl font-heading text-white mb-4">{{ hall.name }}</h3>
        <div class="space-y-2 text-slate-400 text-sm mb-6">
          <div class="flex justify-between"><span>排数</span><span class="text-white">{{ hall.rows }} 排</span></div>
          <div class="flex justify-between"><span>列数</span><span class="text-white">{{ hall.cols }} 列</span></div>
          <div class="flex justify-between"><span>总座位</span><span class="text-cta font-semibold">{{ hall.totalSeats }} 个</span></div>
        </div>
        <div class="flex gap-2">
          <button @click="editHall(hall)" class="flex-1 py-2 border border-cta/30 text-cta rounded-xl hover:bg-cta/10 transition-all cursor-pointer text-sm">编辑</button>
          <button @click="deleteHall(hall.id)" class="flex-1 py-2 border border-red-400/30 text-red-400 rounded-xl hover:bg-red-400/10 transition-all cursor-pointer text-sm">删除</button>
        </div>
      </div>
    </div>

    <div v-if="showDialog" class="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50" @click="showDialog = false">
      <div class="glass-card p-8 max-w-md w-full mx-4" @click.stop>
        <h3 class="text-2xl font-heading text-white mb-6">{{ editingHall ? '编辑影厅' : '添加影厅' }}</h3>
        <form @submit.prevent="saveHall" class="space-y-4">
          <div><label class="block text-sm font-medium text-slate-400 mb-2">影厅名称</label><input v-model="form.name" type="text" placeholder="如：1号厅" class="input-dark" required></div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">座位排数</label><input v-model.number="form.rows" type="number" min="1" max="20" class="input-dark" required></div>
          <div><label class="block text-sm font-medium text-slate-400 mb-2">座位列数</label><input v-model.number="form.cols" type="number" min="1" max="30" class="input-dark" required></div>
          <div class="bg-white/5 p-4 rounded-xl border border-white/10">
            <div class="text-sm text-slate-400 mb-1">总座位数</div>
            <div class="text-2xl font-heading text-cta">{{ form.rows * form.cols }} 个</div>
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

const halls = ref([])
const showDialog = ref(false)
const editingHall = ref(null)
const form = ref({ name: '', rows: 8, cols: 10 })
const resetForm = () => { form.value = { name: '', rows: 8, cols: 10 } }

const loadHalls = async () => { try { const r = await request.get('/hall/list'); halls.value = r.data || [] } catch (e) { console.error('加载影厅失败', e) } }
const editHall = (h) => { editingHall.value = h; form.value = { ...h }; showDialog.value = true }

const saveHall = async () => {
  try {
    const data = { ...form.value, totalSeats: form.value.rows * form.value.cols }
    if (editingHall.value) { await request.put('/hall/update', data); toast.success('更新成功') }
    else { await request.post('/hall/create', data); toast.success('添加成功') }
    showDialog.value = false; loadHalls()
  } catch (e) { console.error('保存失败', e) }
}

const deleteHall = async (id) => {
  if (!confirm('确定删除该影厅吗？')) return
  try { await request.delete(`/hall/delete/${id}`); toast.success('删除成功'); loadHalls() } catch (e) { console.error('删除失败', e) }
}

onMounted(() => loadHalls())
</script>
