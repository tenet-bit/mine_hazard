// 全局类型定义

export interface LoginRequest {
  username: string
  password: string
}

export interface MenuDTO {
  id: number
  parentId: number
  menuName: string
  menuType: string
  path: string
  component: string
  icon: string
  sortOrder: number
  permission: string
  children?: MenuDTO[]
}

export interface UserInfoDTO {
  id: number
  username: string
  realName: string
  email: string
  phone: string
  avatar: string
  roles: string[]
  menus: MenuDTO[]
}

export interface LoginResponse {
  token: string
  tokenType: string
  expiresIn: number
  userInfo: UserInfoDTO
}

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}
