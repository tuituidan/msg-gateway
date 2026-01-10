<template>
  <div>
    <el-select v-model="value.authType" style="width: 200px; margin-bottom: 15px">
      <el-option label="无认证" value="none"></el-option>
      <el-option label="键值对" value="key-value"></el-option>
      <el-option label="Basic Auth" value="basic"></el-option>
      <el-option label="Bearer Token" value="bearer"></el-option>
      <el-option label="JWT Bearer" value="jwt"></el-option>
    </el-select>

    <div v-if="value.authType === 'key-value'">
      <key-value-builder v-model="value.authKeyValues"></key-value-builder>
    </div>

    <div v-if="value.authType === 'basic'">
      <el-input v-model="value.basicUsername" placeholder="请输入用户名" style="margin-bottom: 10px"></el-input>
      <el-input v-model="value.basicPassword" placeholder="请输入密码"></el-input>
    </div>

    <div v-if="value.authType === 'bearer'">
      <el-input v-model="value.bearerToken" placeholder="请输入token"></el-input>
    </div>

    <div v-if="value.authType === 'jwt'">
      <el-select v-model="value.jwtAlgorithm" placeholder="请选择加密算法" clearable
                 style="width: 100%; margin-bottom: 10px">
        <el-option v-for="algorithm in signatureAlgorithms"
                   :key="algorithm"
                   :label="algorithm"
                   :value="algorithm"></el-option>
      </el-select>
      <el-input v-model="value.jwtSecret" placeholder="请输入秘钥Secret" clearable
                style="margin-bottom: 10px"></el-input>
      <el-divider content-position="left">payload参数</el-divider>
      <key-value-builder v-model="value.jwtPayload"></key-value-builder>
    </div>
  </div>
</template>

<script>
export default {
  name: "http-auth-builder",
  components: {
    'key-value-builder': () => import('./key-value-builder.vue'),
  },
  props: {
    value: {
      type: Object,
      default: () => {
        return {
          authType: 'none',
          authKeyValues: [{key: '', value: ''}],
          basicUsername: '',
          basicPassword: '',
          bearerToken: '',
          jwtAlgorithm: 'HS256',
          jwtSecret: '',
          jwtPayload: [{key: '', value: ''}],
        }
      },
    },
  },
  data() {
    return {
      signatureAlgorithms: [
        'HS256', 'HS384', 'HS512',
        'RS256', 'RS384', 'RS512',
        'PS256', 'PS384', 'PS512',
        'ES256', 'ES384', 'ES512'
      ],
    }
  },
  methods: {
    changeInput(){
      this.$emit('input', this.value)
    },
  }
}
</script>
