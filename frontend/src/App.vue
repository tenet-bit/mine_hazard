<template>
  <router-view />
</template>

<script setup lang="ts">
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html, body, #app {
  width: 100%;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #1e2235;
}

::-webkit-scrollbar-thumb {
  background: #3d4466;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #5567a8;
}
</style>
