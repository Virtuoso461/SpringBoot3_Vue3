<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px;font-weight: bold">注册新用户</div>
      <div style="font-size: 14px;color: gray;margin-top: 20px">欢迎注册，请在下方填写相关信息</div>
    </div>

    <div style="margin-top: 40px">
      <el-form :model="form" ref="formRef" :rules="rules" @validate="onValidate">
        <el-form-item prop="username">
          <el-input type="text" v-model="form.username" placeholder="用户名">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" :maxlength="16" show-password v-model="form.password" placeholder="密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input type="password" :maxlength="16" show-password v-model="form.password_repeat" placeholder="重复密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input type="email" v-model="form.email" placeholder="电子邮件地址">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input type="text" v-model="form.code" :minlength="6" :maxlength="6" placeholder="请输入验证码">
                <template #prefix>
                  <el-icon>
                    <EditPen/>
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-button type="success" @click="validateEmail" :disabled="!isEmailValid || coldTime > 0">
                {{ coldTime > 0 ? '请稍后' + coldTime + '秒' : '获取验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>

    <div style="margin-top: 50px">
      <el-button style="width: 270px" type="warning" @click="register">立即注册</el-button>
    </div>
    <div style="margin-top: 15px">
      <span style="font-size: 14px;line-height: 15px;color: gray">已有账号?</span>
      <el-link type="primary" style="translate: 0 -2px" :underline="false" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<script setup>
import router from "@/router";
import {reactive, ref} from "vue";
import {post} from "@/api";
import {ElMessage} from "element-plus";

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'))
  } else {
    callback()
  }
}

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const formRef = ref()
const isEmailValid = ref(false)
const coldTime = ref(0)


const onValidate = (prop, isValid) => {
  if (prop === 'email') {
    isEmailValid.value = isValid
  }
}

const register = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('/api/auth/register', {
        username: form.username,
        password: form.password,
        email: form.email,
        code: form.code
      }, (message) => {
        ElMessage.success(message)
        router.push('/')
      })
    } else {
      ElMessage.warning('请完整填写注册表单内容！')
    }
  })
}

const validateEmail = () => {
  coldTime.value = 60
  post('/api/auth/valid-register-email', {
    email: form.email
  }, (message) => {
    ElMessage.success(message)
    setInterval(() => coldTime.value--, 1000)
  }, (message) => {
    ElMessage.warning(message)
    coldTime.value = 0
  })
}

const rules = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']},
    {min: 2, max: 8, message: '用户名的长度必须在2-8个字符之间', trigger: ['blur', 'change']},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur', 'change']}
  ],
  password_repeat: [
    {validator: validatePass, trigger: ['blur', 'change']},
  ],
  email: [
    {required: true, message: '请输入邮件地址', trigger: 'blur'},
    {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入获取的验证码', trigger: 'blur'},
  ]
}
</script>

<style scoped>

</style>