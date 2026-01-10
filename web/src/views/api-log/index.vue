<template>
  <div class="app-container home">
    <el-form :model="queryParams"
             ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="接口路径" prop="path">
        <el-input
          v-model="queryParams.path"
          placeholder="请输入接口路径"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表名" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="请输入表名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading"
              :data="table.data"
              border stripe
              ref="dataTable"
              @expand-change="expandChange"
              :default-sort="{prop: 'createTime', order: 'descending'}">
      <el-table-column type="expand" label="展开">
        <template slot-scope="props">
          <div style="padding: 10px 20px">
            <el-table :data="props.row.children" border :header-cell-style="{backgroundColor: 'white'}">
              <el-table-column label="序号" type="index" width="50" align="center"/>
              <el-table-column label="应用名称" align="center" prop="appName" show-overflow-tooltip/>
              <el-table-column label="推送状态" align="center" prop="status" show-overflow-tooltip>
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.status === '01'" type="success" effect="plain">成功</el-tag>
                  <el-tag v-else type="danger" effect="plain">失败</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="推送结果" align="center" prop="response" show-overflow-tooltip/>
              <el-table-column label="推送时间" align="center" prop="pushTime" show-overflow-tooltip/>
              <el-table-column label="推送耗时(毫秒)" align="center" prop="costTime" show-overflow-tooltip/>
              <el-table-column label="推送次数" align="center" prop="pushTimes" show-overflow-tooltip/>
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="序号" type="index" width="50" align="center" :index="table.index"/>
      <el-table-column label="日志ID" align="center" prop="id" show-overflow-tooltip/>
      <el-table-column label="接口路径" align="center" prop="path" show-overflow-tooltip/>
      <el-table-column label="客户端IP" align="center" prop="clientIp" show-overflow-tooltip/>
      <el-table-column label="接口名称" align="center" prop="entryApiName" show-overflow-tooltip/>
      <el-table-column label="状态" align="center" prop="statusText" show-overflow-tooltip/>
      <el-table-column label="接收时间" align="center" prop="createTime" show-overflow-tooltip/>
    </el-table>

    <pagination
      v-show="table.total>0"
      :total="table.total"
      :page.sync="queryParams.pageIndex"
      :limit.sync="queryParams.limit"
      @pagination="pageChange"
    />
  </div>
</template>

<script>

export default {
  name: "Index",
  data() {
    return {
      // 遮罩层
      loading: false,
      // 查询参数
      queryParams: {
        pageIndex: 1,
        offset: 0,
        limit: 10,
        path: '',
        tableName: '',
        sort: '-createTime',
      },
      table: {
        total: 0,
        index: 1,
        data: []
      },
    };
  },
  mounted() {
    this.handleQuery();
  },
  methods: {
    pageChange(page) {
      this.queryParams.pageIndex = page.page;
      this.queryParams.offset = this.queryParams.limit * (page.page - 1);
      this.handleQuery();
    },
    handleQuery() {
      this.loading = true;
      this.$http.get('/api/v1/log/api/page', {params: this.queryParams})
        .then(res => {
          this.table = res;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageIndex: 1,
        offset: 0,
        limit: 10,
        path: '',
        tableName: '',
        sort: this.queryParams.sort
      };
      this.handleQuery();
    },
    expandChange(row, expandedRows) {
      if (!expandedRows.map(item => item.id).includes(row.id)) {
        // 控制收起时不加载数据
        return;
      }
      this.$http.get(`/api/v1/log/api_log/${row.id}/push_log`)
        .then(res => {
          row.children = res;
        });
    },
  }
};
</script>

