<template>
  <el-dialog :title="title" :visible.sync="show"
             :close-on-click-modal="false"
             width="800px" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="140px" @submit.native.prevent>
      <el-form-item label="接口类型" prop="typeId">
        <el-select v-model="form.typeId" clearable placeholder="请选择接口类型">
          <el-option
            v-for="item in typeList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="接口名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入接口名称" maxlength="100" v-trim/>
      </el-form-item>
      <el-form-item label="拦截地址" prop="path">
        <el-input v-model="form.path" placeholder="请输入拦截地址" maxlength="100" v-trim/>
      </el-form-item>
      <el-form-item prop="resultExp">
        <span slot="label">
          拦截表达式
          <el-tooltip
            content='通过SpringEL表达式来判断请求体中的数据是否满足接口要求'>
          <i class="el-icon-question"></i>
          </el-tooltip>
        </span>
        <el-input v-model="form.typeExp" placeholder="请输入拦截表达式" maxlength="400" v-trim/>
      </el-form-item>
      <el-form-item label="接口认证">
        <http-auth-builder v-model="form.httpAuth"></http-auth-builder>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  name: "entry-api-edit",
  components: {
    'http-auth-builder': () => import('@/components/http-auth-builder'),
  },
  data() {
    return {
      // 弹出层标题
      title: '新增接口',
      // 是否显示弹出层
      show: false,
      typeList: [],
      form: {
        typeId: '',
        name: '',
        path: '',
        typeExp: '',
        httpAuth: {
          authType: 'none',
          authKeyValues: [{key: '', value: ''}],
          basicUsername: '',
          basicPassword: '',
          bearerToken: '',
          jwtAlgorithm: 'HS256',
          jwtSecret: '',
          jwtPayload: [{key: '', value: ''}],
        },
      },
      rules: {
        typeId: [
          {required: true, message: "接口类型不能为空", trigger: "blur"}
        ],
        name: [
          {required: true, message: "接口名称不能为空", trigger: "blur"}
        ],
        path: [
          {required: true, message: "拦截地址不能为空", trigger: "blur"}
        ],
      }
    }
  },
  mounted() {
    this.loadTypeList();
  },
  methods: {
    loadTypeList() {
      this.$http.get(`/api/v1/entry_api_type/list`)
        .then(res => {
          this.typeList = res;
          if (this.typeList.length > 0) {
            this.form.typeId = this.typeList[0].id;
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    open(typeId, row) {
      this.resetForm();
      if (row) {
        this.form = {...row};
        this.title = '编辑接口';
      } else {
        this.title = '新增接口';
        this.form.typeId = typeId;
      }
      this.show = true;
    },
    resetForm() {
      this.form = {
        typeId: '',
        name: '',
        path: '',
        typeExp: '',
        httpAuth: {
          authType: 'none',
          authKeyValues: [{key: '', value: ''}],
          basicUsername: '',
          basicPassword: '',
          bearerToken: '',
          jwtAlgorithm: 'HS256',
          jwtSecret: '',
          jwtPayload: [{key: '', value: ''}],
        },
      };
      this.$nextTick(() => {
        this.$refs.form.clearValidate();
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$http.save('/api/v1/entry_api', {...this.form})
            .then(() => {
              this.$modal.msgSuccess('保存成功');
              this.show = false;
              this.$emit('refresh');
            })
        }
      });
    },
    // 取消按钮
    cancel() {
      this.show = false;
    },
  }
}
</script>

<style scoped>

</style>
