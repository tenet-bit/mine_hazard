FROM node:20-alpine AS build
WORKDIR /app

# 先安装依赖（利用 Docker 层缓存）
COPY package*.json ./
RUN npm install

# 复制源码并构建
COPY . .
RUN npm run build

# ---- 运行阶段 ----
FROM nginx:1.27-alpine
WORKDIR /usr/share/nginx/html

# 复制 Vue 构建产物
COPY --from=build /app/dist .

# 复制 Nginx 配置（SPA路由支持 + API代理）
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
