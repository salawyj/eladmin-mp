<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="ID">
            <el-input v-model="form.amountId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="app用户id" prop="appUserId">
            <el-input v-model="form.appUserId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="充值总额">
            <el-input v-model="form.rechargeTotal" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="获赠总额">
            <el-input v-model="form.giftTotal" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="充值余额">
            <el-input v-model="form.rechargeBalance" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="获赠余额">
            <el-input v-model="form.giftBalance" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="人民币，元">
            <el-input v-model="form.unit" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="邀请数量" prop="inviteNum">
            <el-input v-model="form.inviteNum" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建者">
            <el-input v-model="form.createBy" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="更新者">
            <el-input v-model="form.updateBy" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="创建日期">
            <el-input v-model="form.createTime" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="更新时间">
            <el-input v-model="form.updateTime" style="width: 370px;" />
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
        <el-table-column prop="amountId" label="ID" />
        <el-table-column prop="appUserId" label="app用户id" />
        <el-table-column prop="rechargeTotal" label="充值总额" />
        <el-table-column prop="giftTotal" label="获赠总额" />
        <el-table-column prop="rechargeBalance" label="充值余额" />
        <el-table-column prop="giftBalance" label="获赠余额" />
        <el-table-column prop="unit" label="人民币，元" />
        <el-table-column prop="inviteNum" label="邀请数量" />
        <el-table-column prop="createBy" label="创建者" />
        <el-table-column prop="updateBy" label="更新者" />
        <el-table-column prop="createTime" label="创建日期" />
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column v-if="checkPer(['admin','appAmount:edit','appAmount:del'])" label="操作" width="150px" align="center">
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
import crudAppAmount from '@/api/appAmount'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { amountId: null, appUserId: null, rechargeTotal: null, giftTotal: null, rechargeBalance: null, giftBalance: null, unit: null, inviteNum: null, createBy: null, updateBy: null, createTime: null, updateTime: null }
export default {
  name: 'AppAmount',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '用户金额', url: 'api/appAmount', idField: 'amountId', sort: 'amountId,desc', crudMethod: { ...crudAppAmount }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'appAmount:add'],
        edit: ['admin', 'appAmount:edit'],
        del: ['admin', 'appAmount:del']
      },
      rules: {
        appUserId: [
          { required: true, message: 'app用户id不能为空', trigger: 'blur' }
        ],
        inviteNum: [
          { required: true, message: '邀请数量不能为空', trigger: 'blur' }
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
