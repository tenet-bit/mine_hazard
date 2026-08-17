import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      name: 'Layout',
      component: () => import('@/views/layout/LayoutView.vue'),
      meta: { requiresAuth: true },
      redirect: '/home',
      children: [
        {
          path: 'home',
          name: 'Home',
          component: () => import('@/views/home/HomeView.vue'),
          meta: { title: '首页' }
        },
        // 实时监测中心
        {
          path: 'monitor/overview',
          name: 'MonitorOverview',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '实时监测总览' }
        },
        {
          path: 'monitor/history',
          name: 'MonitorHistory',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '历史数据' }
        },
        // 智能预警中心
        {
          path: 'alarm/rule',
          name: 'AlarmRule',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '预警规则管理' }
        },
        {
          path: 'alarm/record',
          name: 'AlarmRecord',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '预警记录处理' }
        },
        // 水文地质管理
        {
          path: 'geology/aquifer',
          name: 'Aquifer',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '含水层管理' }
        },
        {
          path: 'geology/aquitard',
          name: 'Aquitard',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '隔水层管理' }
        },
        {
          path: 'geology/fracture',
          name: 'Fracture',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '地质台账' }
        },
        // 防治水工程
        {
          path: 'project/list',
          name: 'ProjectList',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '工程列表' }
        },
        {
          path: 'project/drainage',
          name: 'Drainage',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '排水设施' }
        },
        // 巡检统计分析
        {
          path: 'inspection/plan',
          name: 'InspectionPlan',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '巡检计划' }
        },
        {
          path: 'inspection/record',
          name: 'InspectionRecord',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '巡检记录' }
        },
        {
          path: 'inspection/stat',
          name: 'InspectionStat',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '巡检统计' }
        },
        // 综合报表
        {
          path: 'report/dashboard',
          name: 'Dashboard',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '数据大屏' }
        },
        // 系统管理
        {
          path: 'system/user',
          name: 'SystemUser',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '用户管理' }
        },
        {
          path: 'system/role',
          name: 'SystemRole',
          component: () => import('@/views/placeholder/PlaceholderView.vue'),
          meta: { title: '角色管理' }
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/home'
    }
  ]
})

// 路由守卫：未登录跳转到登录页
router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore()

  if (to.path === '/login') {
    if (userStore.isLoggedIn()) {
      next('/')
    } else {
      next()
    }
    return
  }

  if (to.meta.requiresAuth !== false && !userStore.isLoggedIn()) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }

  // 已登录但 Pinia 中没有用户信息（如页面刷新），则重新获取
  if (userStore.isLoggedIn() && !userStore.userInfo) {
    try {
      await userStore.fetchUserInfo()
    } catch {
      // Token 失效，清除并跳转登录
      localStorage.removeItem('token')
      next({ path: '/login' })
      return
    }
  }

  next()
})

export default router
