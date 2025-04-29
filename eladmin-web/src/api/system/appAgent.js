import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/appAgent',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/appAgent/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/appAgent',
    method: 'put',
    data
  })
}

export default { add, edit, del }
