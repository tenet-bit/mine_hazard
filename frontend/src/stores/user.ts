import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfoDTO, MenuDTO } from '@/types'
import { login as loginApi, getUserInfo as getUserInfoApi, logout as logoutApi } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfoDTO | null>(null)

  /** 登录 */
  async function doLogin(username: string, password: string) {
    const res = await loginApi({ username, password })
    const data = res.data.data
    token.value = data.token
    userInfo.value = data.userInfo
    localStorage.setItem('token', data.token)
  }

  /** 从服务端获取最新用户信息 */
  async function fetchUserInfo() {
    const res = await getUserInfoApi()
    userInfo.value = res.data.data
  }

  /** 退出登录 */
  async function doLogout() {
    try {
      await logoutApi()
    } catch {
      // 忽略退出接口错误
    }
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    router.push('/login')
  }

  /** 是否已登录 */
  function isLoggedIn(): boolean {
    return !!token.value
  }

  /** 获取菜单树 */
  function getMenus(): MenuDTO[] {
    return userInfo.value?.menus || []
  }

  /** 获取角色列表 */
  function getRoles(): string[] {
    return userInfo.value?.roles || []
  }

  /** 是否拥有指定角色 */
  function hasRole(role: string): boolean {
    return getRoles().includes(role)
  }

  return {
    token,
    userInfo,
    doLogin,
    fetchUserInfo,
    doLogout,
    isLoggedIn,
    getMenus,
    getRoles,
    hasRole
  }
})
