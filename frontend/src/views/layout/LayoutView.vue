<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapsed ? '64px' : '240px'" class="layout-aside">
      <!-- Logo 区域 -->
      <div class="sidebar-logo" :class="{ collapsed: isCollapsed }">
        <el-icon :size="28" color="#00d4ff"><Monitor /></el-icon>
        <span v-if="!isCollapsed" class="logo-text">水害防治系统</span>
      </div>

      <!-- 导航菜单 -->
      <el-scrollbar class="sidebar-scrollbar">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapsed"
          :collapse-transition="false"
          background-color="#0d1b2a"
          text-color="#a0b4c8"
          active-text-color="#00d4ff"
          class="sidebar-menu"
          router
        >
          <template v-for="menu in menus" :key="menu.id">
            <!-- 有子菜单的目录 -->
            <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.path">
              <template #title>
                <el-icon><component :is="menu.icon" /></el-icon>
                <span>{{ menu.menuName }}</span>
              </template>
              <el-menu-item
                v-for="child in menu.children"
                :key="child.id"
                :index="child.path"
              >
                <el-icon><component :is="child.icon" /></el-icon>
                <span>{{ child.menuName }}</span>
              </el-menu-item>
            </el-sub-menu>

            <!-- 无子菜单的菜单项 -->
            <el-menu-item v-else :index="menu.path">
              <el-icon><component :is="menu.icon" /></el-icon>
              <template #title>{{ menu.menuName }}</template>
            </el-menu-item>
          </template>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 主区域 -->
    <el-container class="layout-main">
      <!-- 顶部 Header -->
      <el-header class="layout-header" height="60px">
        <!-- 折叠按钮 -->
        <div class="header-left">
          <el-icon
            :size="20"
            class="collapse-btn"
            @click="toggleCollapse"
          >
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
          <!-- 面包屑 -->
          <span class="page-title">{{ currentTitle }}</span>
        </div>

        <!-- 右侧用户信息 -->
        <div class="header-right">
          <!-- 角色标签 -->
          <el-tag
            v-for="role in userStore.getRoles()"
            :key="role"
            :type="roleTagType(role)"
            size="small"
            class="role-tag"
          >
            {{ roleLabel(role) }}
          </el-tag>

          <!-- 用户头像 & 下拉菜单 -->
          <el-dropdown trigger="click" @command="handleUserCommand">
            <div class="user-info">
              <el-avatar :size="32" class="user-avatar">
                {{ avatarText }}
              </el-avatar>
              <span class="username">{{ userStore.userInfo?.realName || userStore.userInfo?.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人信息
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="layout-content">
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import {
  Monitor, Fold, Expand, ArrowDown, User, SwitchButton
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import type { MenuDTO } from '@/types'

const userStore = useUserStore()
const route = useRoute()

const isCollapsed = ref(false)

// 当前激活的菜单（匹配路由路径）
const activeMenu = computed(() => '/' + route.path.replace(/^\//, ''))

// 当前页面标题
const currentTitle = computed(() => {
  return (route.meta.title as string) || '矿用水害防治系统'
})

// 菜单树
const menus = computed(() => userStore.getMenus())

// 用户头像文字（取名字第一个字）
const avatarText = computed(() => {
  const name = userStore.userInfo?.realName || userStore.userInfo?.username || ''
  return name.charAt(0).toUpperCase()
})

function toggleCollapse() {
  isCollapsed.value = !isCollapsed.value
}

function roleTagType(role: string): 'success' | 'warning' | 'info' | 'danger' {
  const map: Record<string, 'success' | 'warning' | 'info' | 'danger'> = {
    ADMIN: 'danger',
    MONITOR: 'success',
    ENGINEER: 'warning',
    MANAGER: 'info'
  }
  return map[role] || 'info'
}

function roleLabel(role: string): string {
  const map: Record<string, string> = {
    ADMIN: '管理员',
    MONITOR: '值班员',
    ENGINEER: '工程师',
    MANAGER: '管理人员'
  }
  return map[role] || role
}

async function handleUserCommand(command: string) {
  if (command === 'logout') {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await userStore.doLogout()
  }
}
</script>

<style scoped>
.layout-container {
  width: 100vw;
  height: 100vh;
  background: #0a0e1a;
}

/* ---- 侧边栏 ---- */
.layout-aside {
  background: #0d1b2a;
  border-right: 1px solid rgba(0, 212, 255, 0.12);
  transition: width 0.25s ease;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.sidebar-logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid rgba(0, 212, 255, 0.1);
  padding: 0 16px;
  white-space: nowrap;
  overflow: hidden;
  flex-shrink: 0;
}

.sidebar-logo.collapsed {
  padding: 0;
  justify-content: center;
}

.logo-text {
  font-size: 15px;
  font-weight: 700;
  color: #e8f4ff;
  letter-spacing: 1px;
  white-space: nowrap;
}

.sidebar-scrollbar {
  flex: 1;
  overflow: hidden;
}

.sidebar-menu {
  border: none;
  padding: 8px 0;
}

.sidebar-menu :deep(.el-menu-item),
.sidebar-menu :deep(.el-sub-menu__title) {
  height: 44px;
  line-height: 44px;
  border-radius: 0 24px 24px 0;
  margin-right: 8px;
  transition: all 0.2s;
}

.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(0, 212, 255, 0.08) !important;
  color: #00d4ff !important;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: rgba(0, 212, 255, 0.15) !important;
  color: #00d4ff !important;
  border-right: 3px solid #00d4ff;
}

.sidebar-menu :deep(.el-sub-menu.is-active > .el-sub-menu__title) {
  color: #00d4ff !important;
}

/* ---- 顶部 Header ---- */
.layout-header {
  background: #0d1b2a;
  border-bottom: 1px solid rgba(0, 212, 255, 0.12);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  cursor: pointer;
  color: #a0b4c8;
  transition: color 0.2s;
}

.collapse-btn:hover {
  color: #00d4ff;
}

.page-title {
  font-size: 15px;
  font-weight: 600;
  color: #e8f4ff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.role-tag {
  font-size: 11px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 6px;
  transition: background 0.2s;
}

.user-info:hover {
  background: rgba(0, 212, 255, 0.08);
}

.user-avatar {
  background: linear-gradient(135deg, #0080ff, #00d4ff);
  font-size: 14px;
  font-weight: 700;
}

.username {
  font-size: 14px;
  color: #e8f4ff;
}

/* ---- 内容区 ---- */
.layout-main {
  display: flex;
  flex-direction: column;
  background: #0a0e1a;
  overflow: hidden;
}

.layout-content {
  flex: 1;
  overflow: auto;
  padding: 20px;
  background: #0a0e1a;
}

/* Element Plus 弹出菜单深色风格 */
:deep(.el-dropdown-menu) {
  background: #0d1b2a !important;
  border: 1px solid rgba(0, 212, 255, 0.2) !important;
}

:deep(.el-dropdown-menu__item) {
  color: #a0b4c8 !important;
}

:deep(.el-dropdown-menu__item:hover) {
  background: rgba(0, 212, 255, 0.08) !important;
  color: #00d4ff !important;
}
</style>
