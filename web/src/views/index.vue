<template>
  <div class="dashboard-editor-container">
    <panel-group/>
    <el-row>
      <el-col :span="12">
        <line-charts title="本日数据接收情况" url="/api/v1/home/api_log/today/line"></line-charts>
      </el-col>
      <el-col :span="12">
        <line-charts title="本日数据推送情况" url="/api/v1/home/push_log/today/line"></line-charts>
      </el-col>
      <el-col :span="12">
        <line-charts title="近30天数据接收情况" url="/api/v1/home/api_log/last_month/line"></line-charts>
      </el-col>
      <el-col :span="12">
        <line-charts title="近30天数据推送情况" url="/api/v1/home/push_log/last_month/line"></line-charts>
      </el-col>
      <el-col :span="24">
        <bar-charts title="接口接收数据情况" url="/api/v1/home/api_log/list/count"></bar-charts>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Index',
  components: {
    PanelGroup: () => import('./dashboard/PanelGroup'),
    'line-charts': () => import('./dashboard/LineCharts'),
    'bar-charts': () => import('./dashboard/BarCharts'),
  },
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
}

</style>
