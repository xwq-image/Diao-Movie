import request from '@/utils/request'

export const getScheduleList = (params) => {
  return request.get('/schedule/list', { params })
}

export const getMovieSchedules = (movieId, date) => {
  return request.get(`/schedule/movie/${movieId}`, { params: { date } })
}

export const getScheduleSeats = (scheduleId) => {
  return request.get(`/schedule/${scheduleId}/seats`)
}

export const createSchedule = (data) => {
  return request.post('/schedule/create', data)
}

export const updateSchedule = (data) => {
  return request.put('/schedule/update', data)
}

export const deleteSchedule = (id) => {
  return request.delete(`/schedule/delete/${id}`)
}
