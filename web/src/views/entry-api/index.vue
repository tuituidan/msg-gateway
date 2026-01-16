<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="8">
        <entry-api-type @rowChange="rowChange"></entry-api-type>
      </el-col>
      <el-col :span="16">
        <entry-api-list ref="refEntryApiList"></entry-api-list>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "entry-api",
  components: {
    'entry-api-type': () => import('@/views/entry-api/entry-api-type'),
    'entry-api-list': () => import('@/views/entry-api/entry-api-list'),
  },
  mounted() {
  },
  methods: {
    rowChange(row) {
      this.$refs.refEntryApiList.loadList(row);
    },
  },
}
</script>

<style scoped>

</style>
