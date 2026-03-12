import request from '@/utils/request'

export const createOrder = (data) => {
  return request.post('/order/create', data)
}

export const payOrder = (data) => {
  return request.post('/order/pay', data)
}

export const refundOrder = (data) => {
  return request.post('/order/refund', data)
}

export const getOrderList = (params) => {
  return request.get('/order/list', { params })
}
