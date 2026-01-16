<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="16">
        <sys-app @rowChange="rowChange"></sys-app>
      </el-col>
      <el-col :span="8">
        <app-api-config ref="refAppApiConfig"></app-api-config>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "sys-app-index",
  components: {
    'sys-app': () => import('@/views/sys-app/sys-app'),
    'app-api-config': () => import('@/views/sys-app/app-api-config'),
  },
  data() {
    return {};
  },
  mounted() {
  },
  methods: {
    rowChange(row) {
      this.$refs.refAppApiConfig.loadConfig(row);
    },
  },
}
</script>

<style scoped>

</style>
