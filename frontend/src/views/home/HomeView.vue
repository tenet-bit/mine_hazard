<template>
  <div class="home-view">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="welcome-content">
        <h2 class="welcome-title">
          欢迎回来，{{ userStore.userInfo?.realName || userStore.userInfo?.username }}
        </h2>
        <p class="welcome-subtitle">矿用水害防治系统 · 实时保障矿井安全生产</p>
      </div>
      <div class="welcome-icon">
        <el-icon :size="60" color="rgba(0,212,255,0.3)"><Monitor /></el-icon>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6" v-for="stat in stats" :key="stat.label">
        <div class="stat-card" :style="{ borderColor: stat.color }">
          <div class="stat-icon" :style="{ color: stat.color, background: stat.bgColor }">
            <el-icon :size="28"><component :is="stat.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" :style="{ color: stat.color }">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 可用模块 -->
    <div class="module-section">
      <h3 class="section-title">可用功能模块</h3>
      <el-row :gutter="16">
        <el-col :span="4" v-for="menu in topMenus" :key="menu.id">
          <div class="module-card" @click="navigateToMenu(menu)">
            <el-icon :size="36" :color="'#00d4ff'"><component :is="menu.icon" /></el-icon>
            <span class="module-name">{{ menu.menuName }}</span>
            <span class="module-count" v-if="menu.children">{{ menu.children.length }} 个功能</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 系统信息 -->
    <div class="sys-info">
      <el-descriptions title="系统信息" :column="3" border size="small">
        <el-descriptions-item label="当前角色">
          <el-tag
            v-for="role in userStore.getRoles()"
            :key="role"
            :type="roleTagType(role)"
            size="small"
            style="margin-right: 4px"
          >{{ roleLabel(role) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="用户名">{{ userStore.userInfo?.username }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userStore.userInfo?.email || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="可用菜单数">{{ allMenuCount }} 个</el-descriptions-item>
        <el-descriptions-item label="系统版本">v1.0.0 MVP</el-descriptions-item>
        <el-descriptions-item label="系统状态">
          <el-tag type="success">正常运行</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Monitor, DataLine, Bell, Tools, Search, DataAnalysis } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import type { MenuDTO } from '@/types'

const userStore = useUserStore()
const router = useRouter()

const topMenus = computed(() => userStore.getMenus())

const allMenuCount = computed(() => {
  let count = 0
  for (const menu of userStore.getMenus()) {
    count++
    if (menu.children) count += menu.children.length
  }
  return count
})

const stats = [
  { label: '可用模块', value: computed(() => topMenus.value.length).value, icon: 'Grid', color: '#00d4ff', bgColor: 'rgba(0,212,255,0.1)' },
  { label: '系统角色', value: computed(() => userStore.getRoles().length).value, icon: 'UserFilled', color: '#67c23a', bgColor: 'rgba(103,194,58,0.1)' },
  { label: '系统状态', value: '正常', icon: 'CircleCheck', color: '#67c23a', bgColor: 'rgba(103,194,58,0.1)' },
  { label: '系统版本', value: 'v1.0', icon: 'InfoFilled', color: '#e6a23c', bgColor: 'rgba(230,162,60,0.1)' }
]

function navigateToMenu(menu: MenuDTO) {
  if (menu.children && menu.children.length > 0) {
    router.push(menu.children[0].path)
  } else if (menu.path) {
    router.push(menu.path)
  }
}

function roleTagType(role: string): 'success' | 'warning' | 'info' | 'danger' {
  const map: Record<string, 'success' | 'warning' | 'info' | 'danger'> = {
    ADMIN: 'danger', MONITOR: 'success', ENGINEER: 'warning', MANAGER: 'info'
  }
  return map[role] || 'info'
}

function roleLabel(role: string): string {
  const map: Record<string, string> = {
    ADMIN: '系统管理员', MONITOR: '监测值班员', ENGINEER: '防治水工程师', MANAGER: '矿井管理人员'
  }
  return map[role] || role
}
</script>

<style scoped>
.home-view {
  color: #e8f4ff;
}

.welcome-banner {
  background: linear-gradient(135deg, rgba(0, 128, 255, 0.15) 0%, rgba(0, 212, 255, 0.08) 100%);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 12px;
  padding: 28px 32px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.welcome-title {
  font-size: 22px;
  font-weight: 700;
  color: #e8f4ff;
  margin-bottom: 8px;
}

.welcome-subtitle {
  font-size: 13px;
  color: rgba(0, 212, 255, 0.7);
}

.stat-row {
  margin-bottom: 24px;
}

.stat-card {
  background: rgba(13, 27, 42, 0.8);
  border: 1px solid;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 4px;
}

.module-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #e8f4ff;
  margin-bottom: 16px;
  padding-left: 12px;
  border-left: 3px solid #00d4ff;
}

.module-card {
  background: rgba(13, 27, 42, 0.8);
  border: 1px solid rgba(0, 212, 255, 0.12);
  border-radius: 10px;
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.2s;
  text-align: center;
}

.module-card:hover {
  border-color: rgba(0, 212, 255, 0.4);
  background: rgba(0, 212, 255, 0.06);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 212, 255, 0.1);
}

.module-name {
  font-size: 13px;
  color: #c8dff0;
  font-weight: 500;
}

.module-count {
  font-size: 11px;
  color: rgba(0, 212, 255, 0.6);
}

.sys-info {
  background: rgba(13, 27, 42, 0.8);
  border: 1px solid rgba(0, 212, 255, 0.12);
  border-radius: 10px;
  padding: 20px;
}

.sys-info :deep(.el-descriptions__title) {
  color: #e8f4ff;
  font-size: 15px;
}

.sys-info :deep(.el-descriptions__label) {
  background: rgba(0, 212, 255, 0.05);
  color: rgba(255, 255, 255, 0.5);
}

.sys-info :deep(.el-descriptions__content) {
  background: transparent;
  color: #c8dff0;
}

.sys-info :deep(.el-descriptions__cell) {
  border-color: rgba(0, 212, 255, 0.1);
}
</style>
