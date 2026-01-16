<template>
  <div v-if="display==='edit'">
    <el-select v-model="value.authType" style="width: 200px; margin-bottom: 15px">
      <el-option v-for="key in Object.keys(authTypes)" :label="authTypes[key]" :value="key"></el-option>
    </el-select>
    <div v-if="value.authType === 'key-value'">
      <key-value-builder v-model="value.authKeyValues"></key-value-builder>
    </div>
    <div v-if="value.authType === 'basic'">
      <el-input v-model="value.basicUsername" placeholder="请输入用户名" style="margin-bottom: 10px"></el-input>
      <el-input v-model="value.basicPassword" placeholder="请输入密码"></el-input>
    </div>
    <div v-if="value.authType === 'bearer'">
      <el-input v-model="value.bearerToken" placeholder="请输入令牌"></el-input>
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
  <div v-else>
    <div>认证类型：{{ authTypes[value.authType] }}</div>
    <div v-if="value.authType === 'key-value'">
      <div v-for="item in value.authKeyValues">{{ item.key }}: {{ item.value }}</div>
    </div>
    <div v-if="value.authType === 'basic'">
      <div>用户名：{{ value.basicUsername }}</div>
      <div>密码：{{ value.basicPassword }}</div>
    </div>
    <div v-if="value.authType === 'bearer'">
      <div>令牌：{{ value.bearerToken }}</div>
    </div>
    <div v-if="value.authType === 'jwt'">
      <div>加密算法：{{ value.jwtAlgorithm }}</div>
      <div>秘钥Secret：{{ value.jwtSecret }}</div>
      <table v-if="value.jwtPayload && value.jwtPayload.length">
        <tr>
          <td style="vertical-align: top">payload参数：</td>
          <td>
            <div v-for="item in value.jwtPayload">{{ item.key }} = {{ item.value }}</div>
          </td>
        </tr>
      </table>
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
    display: {
      type: String,
      default: 'edit',
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
      authTypes: {
        'none': '无认证',
        'key-value': '键值对',
        'basic': 'Basic Auth',
        'bearer': 'Bearer Token',
        'jwt': 'JWT Bearer',
      }
    }
  },
  methods: {
    changeInput() {
      this.$emit('input', this.value)
    },
  }
}
</script>
