<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">登 录</div>
      <div style="font-size: 14px;color: gray;margin-top: 20px">在进入系统之前请先输入用户名和密码进行登录</div>
    </div>
    <div style="margin-top: 25px">
      <el-input type="text" v-model="form.username" placeholder="用户名/邮箱">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>
      <el-input style="margin-top: 10px" type="password" v-model="form.password" placeholder="密码" show-password>
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
    </div>
    <el-row style="margin-top: 5px">
      <el-col :span="12" style="text-align: left">
        <el-checkbox v-model="form.remember" label="记住我"/>
      </el-col>
      <el-col :span="12" style="text-align: right">
        <el-link :underline="false" @click="router.push('/forget')">忘记密码？</el-link>
      </el-col>
    </el-row>
    <div style="margin-top: 40px">
      <el-button style="width: 270px" type="success" plain @click="login()">立即登录</el-button>
    </div>

    <el-divider>
      <span style="color: gray;font-size: 13px">没有账号</span>
    </el-divider>
    <div>
      <el-button style="width: 270px" type="warning" plain @click="router.push('/register')">注册账号</el-button>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/api";
import router from "@/router";
import {useStore} from "@/stores";

const form = reactive({
  username: 'admin',
  password: '123456',
  remember: false
})
const store = useStore()
const login = () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请填写用户名和密码！')
  } else {
    post('/api/auth/login', {
      username: form.username,
      password: form.password,
      remember: form.remember
    }, (message) => {
      ElMessage.success(message)
      get('/api/user/me', (message) => {
        store.auth.user = message
        router.push('/index')
      }, () => {
        store.auth.user = null
        router.push("/index")
      })
    })
  }
}
</script>

<style scoped>

</style>