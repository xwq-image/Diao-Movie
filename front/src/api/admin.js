import request from '@/utils/request'

export const getUserList = () => {
  return request.get('/admin/users')
}

export const getStaffList = () => {
  return request.get('/admin/staff')
}

export const createStaff = (data) => {
  return request.post('/admin/create-staff', data)
}

export const resetPassword = (data) => {
  return request.post('/admin/reset-password', data)
}

export const toggleUserStatus = (data) => {
  return request.post('/admin/toggle-user-status', data)
}
