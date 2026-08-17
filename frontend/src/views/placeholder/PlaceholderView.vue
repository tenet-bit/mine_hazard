<template>
  <div class="placeholder-view">
    <div class="placeholder-content">
      <el-icon :size="80" color="rgba(0,212,255,0.3)"><component :is="pageIcon" /></el-icon>
      <h2 class="page-name">{{ pageTitle }}</h2>
      <p class="page-desc">该模块将在 Phase 2 实现完整功能</p>
      <el-tag type="info" size="large">开发中</el-tag>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const pageTitle = computed(() => (route.meta.title as string) || '功能模块')

const pageIcon = computed(() => {
  const path = route.path
  if (path.includes('monitor')) return 'Monitor'
  if (path.includes('alarm')) return 'Bell'
  if (path.includes('geology')) return 'MapLocation'
  if (path.includes('project')) return 'Setting'
  if (path.includes('inspection')) return 'Search'
  if (path.includes('report') || path.includes('dashboard')) return 'DataAnalysis'
  if (path.includes('system')) return 'Tools'
  return 'Grid'
})
</script>

<style scoped>
.placeholder-view {
  width: 100%;
  height: 100%;
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  text-align: center;
}

.page-name {
  font-size: 22px;
  font-weight: 600;
  color: #e8f4ff;
}

.page-desc {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.4);
}
</style>
