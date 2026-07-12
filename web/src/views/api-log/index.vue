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
      <el-form-item label="接收状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择推送状态">
          <el-option
            v-for="item in statusOptions"
            :key="item.code"
            :label="item.name"
            :value="item.code">
          </el-option>
        </el-select>
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
      <el-table-column label="序号" type="index" width="50" align="center" :index="table.index"/>
      <el-table-column label="日志ID" align="center" prop="id" show-overflow-tooltip/>
      <el-table-column label="接口路径" align="center" prop="path" show-overflow-tooltip/>
      <el-table-column label="客户端IP" align="center" prop="clientIp" show-overflow-tooltip/>
      <el-table-column label="接口名称" align="center" prop="entryApiName" show-overflow-tooltip/>
      <el-table-column label="接收状态" align="center" prop="statusText" show-overflow-tooltip/>
      <el-table-column label="接收时间" align="center" prop="createTime" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" width="110" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document"
            @click.stop="$router.push({name: 'api-log-detail', params: {id: scope.row.id}})"
          >查看
          </el-button>
        </template>
      </el-table-column>
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
      statusOptions: [
        {
          code: '01',
          name: '接收成功',
        },
        {
          code: '02',
          name: '没有匹配的api接收',
        },
        {
          code: '03',
          name: '接口认证未通过',
        },
      ],
      // 查询参数
      queryParams: {
        pageIndex: 1,
        offset: 0,
        limit: 10,
        path: '',
        status: '',
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
      this.$http.get('/api/v1/log/api_log/page', {params: this.queryParams})
        .then(res => {
          for (const item of res.data) {
            item.children = [];
          }
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
        status: '',
        sort: this.queryParams.sort
      };
      this.handleQuery();
    },
  }
};
</script>

