<template>
  <div>
    欢迎来到学习平台
  </div>
  <div>
    <el-button type="danger" plain @click="logout()">退出登录</el-button>
  </div>
</template>

<script setup>
import {get} from "@/api";
import {ElMessage} from "element-plus";
import router from "@/router";

const logout = () => {
  get('/api/auth/logout', (message) => {
    ElMessage.success(message)
    router.push('/')
  })
}
</script>

<style scoped>

</style>