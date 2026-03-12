import request from '@/utils/request'

export const getDailyReport = (params) => {
  return request.get('/finance/daily-report', { params })
}

export const getMonthlyReport = (params) => {
  return request.get('/finance/monthly-report', { params })
}

export const getMovieRevenue = () => {
  return request.get('/finance/movie-revenue')
}

export const getBalanceLogs = (params) => {
  return request.get('/finance/balance-logs', { params })
}
