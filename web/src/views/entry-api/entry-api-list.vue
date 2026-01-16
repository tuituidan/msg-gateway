<template>
  <div>
    <el-row type="flex" justify="end">
      <el-row class="mb8 mt5">
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-plus"
            size="small"
            @click="openEditDialog()"
          >新增
          </el-button>
        </el-col>
      </el-row>
    </el-row>
    <el-table
      stripe
      border
      ref="dataTable"
      v-loading="loading"
      :data="dataList">
      <el-table-column label="序号" type="index" width="50" align="center"/>
      <el-table-column type="expand" label="展开">
        <template slot-scope="props">
          <div class="table-expand">
            <el-form :model="props.row" label-width="120px">
              <el-form-item label="接口名称">
                <span v-text="props.row.name"></span>
              </el-form-item>
              <el-form-item label="拦截路径">
                <span v-text="props.row.path"></span>
              </el-form-item>
              <el-form-item label="拦截表达式">
                <span v-text="props.row.typeExp"></span>
              </el-form-item>
              <el-form-item label="接口认证">
                <http-auth-builder :value="props.row.httpAuth" display="show"></http-auth-builder>
              </el-form-item>
            </el-form>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="接口名称" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="拦截路径" align="center" prop="path" :show-overflow-tooltip="true"/>
      <el-table-column label="拦截表达式" align="center" prop="typeExp" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="110" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click.stop="openEditDialog(scope.row)"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click.stop="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <entry-api-edit ref="refEntryApiEdit" @refresh="loadList()"></entry-api-edit>
  </div>
</template>

<script>
export default {
  name: "sys-app-list",
  components: {
    'entry-api-edit': () => import('@/views/entry-api/entry-api-edit'),
    'http-auth-builder': () => import('@/components/http-auth-builder'),
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      dataList: [],
      typeId: '',
    };
  },
  methods: {
    loadList(row) {
      this.loading = true;
      if (row) {
        this.typeId = row.id;
      }
      this.$http.get(`/api/v1/entry_api/list`, {params: {typeId: this.typeId}})
        .then(res => {
          this.dataList = res;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    /** 修改按钮操作 */
    openEditDialog(row) {
      this.$refs.refEntryApiEdit.open(this.typeId, row);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm(`是否确认删除【${row.name}】数据项？`)
        .then(() => {
          return this.$http.delete(`/api/v1/entry_api/${row.id}`);
        }).then(() => {
        this.loadList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  }
}
</script>

<style scoped lang="scss">
.table-expand {
  overflow-x: auto;

  .el-form {
    ::v-deep label {
      color: #99a9bf;
    }

    .el-form-item {
      margin-bottom: 0;

      .header-item {
        white-space: nowrap;
        padding-right: 10px;
      }
    }
  }
}
</style>
