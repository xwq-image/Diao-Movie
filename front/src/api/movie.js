import request from '@/utils/request'

export const getMovieList = (params) => {
  return request.get('/movie/list', { params })
}

export const getMovieDetail = (id) => {
  return request.get(`/movie/detail/${id}`)
}

export const getMovieSchedules = (movieId, date) => {
  return request.get(`/schedule/movie/${movieId}`, { params: { date } })
}

export const createMovie = (data) => {
  return request.post('/movie/create', data)
}

export const updateMovie = (data) => {
  return request.put('/movie/update', data)
}

export const deleteMovie = (id) => {
  return request.delete(`/movie/delete/${id}`)
}
