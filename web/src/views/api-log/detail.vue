<template>
  <div class="app-container home">
    <el-page-header content="接口日志详情" @back="goBack"></el-page-header>
    <el-descriptions class="log-form" :column="3" border>
      <el-descriptions-item label="请求路径">{{ form.path }}</el-descriptions-item>
      <el-descriptions-item label="客户端IP">{{ form.clientIp }}</el-descriptions-item>
      <el-descriptions-item label="接口名称">{{ form.entryApiName }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ form.statusText }}</el-descriptions-item>
      <el-descriptions-item label="推送应用数">{{ form.pushAppCount }}</el-descriptions-item>
      <el-descriptions-item label="接收时间">{{ form.createTime }}</el-descriptions-item>
    </el-descriptions>
    <el-row class="log-body" :gutter="10">
      <el-col :span="12">
        <div class="desc-label">请求头</div>
        <el-input type="textarea" :rows="15"
                  v-model="form.headers"
                  readonly show-word-limit></el-input>
      </el-col>
      <el-col :span="12">
        <div class="desc-label">请求体</div>
        <el-input type="textarea" :rows="15"
                  v-model="form.body"
                  readonly show-word-limit></el-input>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {
  name: "api-log-detail",
  data() {
    return {
      form: {
        path: '',
        clientIp: '',
        entryApiName: '',
        statusText: '',
        pushAppCount: '',
        createTime: ''
      },
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      this.$http.get(`/api/v1/log/api_log/${this.$route.params.id}`)
        .then(res => {
          this.form = res;
        });
    },
    goBack() {
      this.$router.back();
    }
  }
};
</script>

<style scoped>
.log-form, .log-body, .push-log-table {
  margin-top: 20px;
}

.desc-label {
  border-top: 1px solid #e6ebf5;
  border-left: 1px solid #e6ebf5;
  border-right: 1px solid #e6ebf5;
  background-color: #fafafa;
  color: #909399;
  line-height: 1.5;
  font-size: 14px;
  padding: 10px;
}

::v-deep .el-textarea__inner {
  border: 1px solid #e6ebf5;
  border-radius: 0;
}

::v-deep .el-textarea__inner:hover {
  border: 1px solid #e6ebf5;
  border-radius: 0;
}
</style>

