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
      highlight-current-row
      ref="dataTable"
      v-loading="loading"
      @row-click="rowClickHandler"
      @current-change="currentRowChange"
      :data="dataList">
      <el-table-column label="序号" type="index" width="50" align="center"/>
      <el-table-column label="选择" width="50" align="center">
        <template slot-scope="scope">
          <el-radio v-model="selectRow" :label="scope.row.id"><i></i></el-radio>
        </template>
      </el-table-column>
      <el-table-column label="接口类型名称" align="center" prop="name"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
        <template slot-scope="scope" v-if="scope.row.id !== '1'">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="openEditDialog(scope.row)"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "role-list",
  data() {
    return {
      // 遮罩层
      loading: false,
      dataList: [],
      selectRow: '',
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      this.$http.get(`/api/v1/entry_api_type/list`)
        .then(res => {
          this.dataList = res;
          if (this.dataList.length > 0) {
            const firstItem = this.dataList[0];
            this.rowClickHandler(firstItem);
            this.currentRowChange(firstItem);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    rowClickHandler(row) {
      this.selectRow = row.id;
    },
    currentRowChange(row) {
      if (row) {
        this.$emit('rowChange', row);
      }
    },
    /** 修改按钮操作 */
    openEditDialog(row) {
      const title = row ? '编辑接口类型' : '添加接口类型';
      this.$prompt('', title, {
        inputValue: row ? row.name : '',
        inputPlaceholder: '请输入接口类型名称'
      }).then(({value}) => {
        this.$http.save('/api/v1/entry_api_type', {id: row ? row.id : '', name: value})
          .then(() => {
            this.$modal.msgSuccess("保存成功");
            this.show = false;
            this.getList();
          });
      }).catch(() => {
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm(`是否确认删除【${row.name}】数据项？`)
        .then(() => {
          return this.$http.delete(`/api/v1/entry_api_type/${row.id}`);
        }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  }
}
</script>

<style scoped>

</style>
