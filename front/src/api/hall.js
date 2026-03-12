import request from '@/utils/request'

export const getHallList = () => {
  return request.get('/hall/list')
}

export const getHallDetail = (id) => {
  return request.get(`/hall/detail/${id}`)
}

export const createHall = (data) => {
  return request.post('/hall/create', data)
}

export const updateHall = (data) => {
  return request.put('/hall/update', data)
}

export const deleteHall = (id) => {
  return request.delete(`/hall/delete/${id}`)
}
