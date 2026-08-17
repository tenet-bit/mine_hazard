<template>
  <div class="login-wrapper">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
    </div>

    <div class="login-box">
      <!-- 系统标题 -->
      <div class="login-header">
        <div class="logo-icon">
          <el-icon :size="48" color="#00d4ff"><Monitor /></el-icon>
        </div>
        <h1 class="system-title">矿用水害防治系统</h1>
        <p class="system-subtitle">Mine Water Hazard Prevention System</p>
      </div>

      <!-- 登录表单 -->
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            class="login-input"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            class="login-input"
          />
        </el-form-item>

        <el-button
          type="primary"
          size="large"
          :loading="loading"
          class="login-btn"
          @click="handleLogin"
        >
          {{ loading ? '登录中...' : '登 录' }}
        </el-button>
      </el-form>

      <!-- 测试账号提示 -->
      <div class="account-tips">
        <p>默认账号：admin / operator / engineer / manager</p>
        <p>默认密码：admin123</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock, Monitor } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await userStore.doLogin(form.username, form.password)
    ElMessage.success(`欢迎回来，${userStore.userInfo?.realName || form.username}！`)
    // 跳转到目标页面或首页
    const redirect = route.query.redirect as string
    router.push(redirect || '/')
  } catch (error: any) {
    ElMessage.error(error?.response?.data?.message || '用户名或密码错误')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrapper {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #0a0e1a 0%, #0d1b2a 40%, #0a1628 70%, #0d1a2e 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  border: 1px solid rgba(0, 212, 255, 0.08);
}

.circle-1 {
  width: 600px;
  height: 600px;
  top: -200px;
  left: -200px;
  background: radial-gradient(circle, rgba(0, 212, 255, 0.03) 0%, transparent 70%);
}

.circle-2 {
  width: 800px;
  height: 800px;
  bottom: -300px;
  right: -300px;
  background: radial-gradient(circle, rgba(0, 100, 255, 0.04) 0%, transparent 70%);
}

.login-box {
  width: 420px;
  background: rgba(15, 25, 50, 0.9);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 12px;
  padding: 48px 40px 36px;
  box-shadow: 0 0 60px rgba(0, 212, 255, 0.08), 0 20px 60px rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(20px);
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  margin-bottom: 16px;
  display: flex;
  justify-content: center;
  filter: drop-shadow(0 0 12px rgba(0, 212, 255, 0.6));
}

.system-title {
  font-size: 22px;
  font-weight: 700;
  color: #e8f4ff;
  letter-spacing: 2px;
  margin-bottom: 8px;
}

.system-subtitle {
  font-size: 12px;
  color: rgba(0, 212, 255, 0.6);
  letter-spacing: 1px;
}

.login-form {
  margin-bottom: 16px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.login-input :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(0, 212, 255, 0.2);
  box-shadow: none;
  border-radius: 8px;
  transition: all 0.3s;
}

.login-input :deep(.el-input__wrapper:hover),
.login-input :deep(.el-input__wrapper.is-focus) {
  border-color: rgba(0, 212, 255, 0.6);
  box-shadow: 0 0 0 1px rgba(0, 212, 255, 0.2);
  background: rgba(0, 212, 255, 0.04);
}

.login-input :deep(.el-input__inner) {
  color: #e8f4ff;
  height: 44px;
  font-size: 15px;
}

.login-input :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.3);
}

.login-input :deep(.el-input__prefix-icon) {
  color: rgba(0, 212, 255, 0.6);
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 4px;
  border-radius: 8px;
  background: linear-gradient(90deg, #0080ff, #00d4ff);
  border: none;
  box-shadow: 0 4px 24px rgba(0, 180, 255, 0.3);
  transition: all 0.3s;
  margin-top: 8px;
}

.login-btn:hover {
  box-shadow: 0 6px 30px rgba(0, 180, 255, 0.5);
  transform: translateY(-1px);
}

.account-tips {
  text-align: center;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

.account-tips p {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.35);
  line-height: 1.8;
}
</style>
