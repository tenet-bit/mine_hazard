import request from './request'
import type { LoginRequest, LoginResponse, UserInfoDTO, ApiResult } from '@/types'

/** 用户登录 */
export function login(data: LoginRequest) {
  return request.post<ApiResult<LoginResponse>>('/auth/login', data)
}

/** 获取当前用户信息（含菜单树） */
export function getUserInfo() {
  return request.get<ApiResult<UserInfoDTO>>('/auth/me')
}

/** 退出登录 */
export function logout() {
  return request.post<ApiResult<void>>('/auth/logout')
}
