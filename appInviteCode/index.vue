<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="邀请码" prop="code">
            <el-input v-model="form.code" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="所属用户ID" prop="appUserId">
            <el-input v-model="form.appUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="0-未使用 1-已使用">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="createTime">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="过期时间">
            <el-input v-model="form.expireTime" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="code" label="邀请码" />
        <el-table-column prop="appUserId" label="所属用户ID" />
        <el-table-column prop="status" label="0-未使用 1-已使用" />
        <el-table-column prop="createTime" label="createTime" />
        <el-table-column prop="expireTime" label="过期时间" />
        <el-table-column v-if="checkPer(['admin','appInviteCode:edit','appInviteCode:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudAppInviteCode from '@/api/appInviteCode'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { code: null, appUserId: null, status: null, createTime: null, expireTime: null }
export default {
  name: 'AppInviteCode',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '邀请码', url: 'api/appInviteCode', idField: 'code', sort: 'code,desc', crudMethod: { ...crudAppInviteCode }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'appInviteCode:add'],
        edit: ['admin', 'appInviteCode:edit'],
        del: ['admin', 'appInviteCode:del']
      },
      rules: {
        code: [
          { required: true, message: '邀请码不能为空', trigger: 'blur' }
        ],
        appUserId: [
          { required: true, message: '所属用户ID不能为空', trigger: 'blur' }
        ]
      }    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
