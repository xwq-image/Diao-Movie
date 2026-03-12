import request from '@/utils/request'

export const setPayPassword = (data) => {
  return request.post('/user/set-pay-password', data)
}

export const recharge = (data) => {
  return request.post('/user/recharge', data)
}

export const getUserInfo = () => {
  return request.get('/user/info')
}

export const getBalanceLogs = () => {
  return request.get('/user/balance-logs')
}

export const updateNickname = (data) => {
  return request.post('/user/update-nickname', data)
}

export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/user/upload-avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
