<template>
    <div v-loading="loading" class="getInterface">
		<div class="head">
			<div class="title">接口列表 </div>
			<div class="search">

				<!-- <div class="li">
					<div>选择类型：</div>

					<el-select v-model="value2" placeholder="请选择" class="w_140"  size="small ">
						<el-option
						v-for="item in options"
						:key="item.value"
						:label="item.label"
						:value="item.value">
						</el-option>
					</el-select>
				</div> -->
				<div class="li">
					<div class="li_1">接口名称：</div>
					<el-input
						class="w_220"
						size="small "
						placeholder="请输入内容"
						v-model="interfaceName"
						clearable>
					</el-input>
				</div>
				<div class="li">
					<el-button  class="ps_but" size="small" @click="getInterface()" >搜索</el-button>
					<el-button  class="ps_but" size="small " @click="add() ; addOrUpdate=true">新增</el-button>

				</div>

			</div>
		</div>
		<div class="body">
			<!-- table -->
			<el-table
				:data="list"
				border
				size='mini'
				style="width: 100%">
                <el-table-column
				align="center"
				prop="interfaceName"
				label="接口名称"
				width="200">
				</el-table-column>
				<el-table-column
				align="center"
				prop="interfaceDescription"
				label="描述"
				width="">
				</el-table-column>
				<el-table-column
				align="center"
				prop="interfaceId"
				label="接口主键"
				width="">
				</el-table-column>
                <el-table-column
				align="center"
				prop="interfaceRemark"
				label="备注"
				width="">
				</el-table-column>

				<el-table-column
				align="center"
				prop="handle"
				label="操作"
				width="120">
					<template slot-scope="scope">
						<i @click="update(scope.row)"  class="ps_but2 el-icon-edit-outline"></i>
						<!-- <i @click="handleClick(scope.row)"  class="ps_but2 el-icon-view"></i> -->
						<i @click="deleteItem(scope.row)"  class="ps_but2 el-icon-delete"></i>
						<!-- <i @click="handleClick(scope.row)"  class="ps_but2 el-icon-share"></i> -->

					</template>
				</el-table-column>
			</el-table>
			<!-- 分页 参数：total 总条目数 ;currentPage 当前页码-->
			<el-pagination
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				:current-page="currentPage"
				:page-sizes="[10, 20, 30, 40]"
				:page-size="pageSize"
				layout="total, sizes, prev, pager, next, jumper"
				:total="total">
			</el-pagination>
		</div>
		<!-- 弹出表单对话窗口 -->
		<el-dialog title="对话表单" :visible.sync="modal1" :width="form1Width" >
			<el-form :model="addInterfaceInfo"  >
				<el-form-item label="接口主键" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addInterfaceInfo.interfaceId" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="接口名称" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addInterfaceInfo.interfaceName" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="描述" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addInterfaceInfo.interfaceDescription" autocomplete="off"></el-input>
				</el-form-item>
                <el-form-item label="备注" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addInterfaceInfo.interfaceRemark" autocomplete="off"></el-input>
				</el-form-item>





			</el-form>
			<div slot="footer" class="dialog-footer" v-loading="loading2">
				<el-button class="ps_but" @click="modal1 = false">取 消</el-button>
				<el-button v-if="addOrUpdate" class="ps_but bg_color1" type="primary" @click="addInterface()">确 定</el-button>
				<el-button v-else class="ps_but bg_color1" type="primary" @click="updateInterface()">修 改</el-button>
			</div>
		</el-dialog>
    </div>
</template>
<script>
import http from '../utils/http'
import api from '../utils/api'
import { setTimeout } from 'timers';
export default {
	name: 'index_index',
	data () {
		return {
			// 加载框1
			loading:false,
			// 加载框2
			loading2:false,
			// 对话框
			modal1:false,
			// formLabelWidth
			formLabelWidth:'150px',
			// formInputWitdh
			formInputWitdh:'120px',
			// form1Width
			form1Width:'80%',
			addOrUpdate:true,
            interfaceName:'',
            pageSize:10,
            pageNum:1,
            total:0,
			currentPage: 1,//分页-初始页码
            list:[],
			value1:'',
			value2:'',
			options: [
				{
				value: "0",
				label: '0'
				}, {
				value: "1",
				label: '1'
				}
			],

			addInterfaceInfo:{
				interfaceId: '',//接口主键
                interfaceName: '',//接口名称
                interfaceDescription:'',//描述
                interfaceRemark:'',//备注


			}
		}
	},
	methods:{
		// 查看
		handleClick(row){
			console.log(row);
		},
		// 分页操作
		handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val
            this.getInterface()
		},
		// 分页操作
		handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val
            this.getInterface()
		},
		// 删除
		deleteItem(item){
			this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning' //可选类型 warning/info
			}).then(() => {
				this.deleteInterface(item)

			}).catch(() => {
				// cancel to do...

			})
		},
		// 修改
		update(item){
			this.addOrUpdate = false
			this.modal1 = true
			console.log(item)
			this.addInterfaceInfo = {
				id:item.id,//id
				interfaceId: item.interfaceId,//接口主键
                interfaceName: item.interfaceName,//接口名称
                interfaceDescription:item.interfaceDescription,//描述
                interfaceRemark:item.interfaceRemark,//备注
			}
		},
		// 新增
		add(){
			this.modal1=true
			this.addInterfaceInfo = {
				interfaceId: '',//接口主键
                interfaceName: '',//接口名称
                interfaceDescription:'',//描述
                interfaceRemark:'',//备注
			}
		},
        // 获取接口列表
        async getInterface(){

			this.loading = true;
			let data = {
                interfaceName:this.interfaceName,
				pageNum:this.pageNum,
   				pageSize: this.pageSize,
			};
			let res = await http.post(api.getInterface, data);
			console.log(res)

			if(res.data.code =='1'){
                this.loading = false
                this.list = res.data.data.list
                this.total = res.data.data.total

			}else{
				this.loading = false
			}

        },
        //新增接口
        async addInterface(){

			this.loading2 = true;
			let data = this.addInterfaceInfo
			let res = await http.post(api.addInterface, data);
            console.log(res)

			if(res.data.code =='1'){
				this.modal1=false
                this.loading2 = false
                this.list = res.data.data.list
                this.total = res.data.data.total
          this.M();
			}else{
                this.loading2 = false;
                this.$message.error(res.data.msg)
			}

        },
        //删除接口
        async deleteInterface(item){
            console.log(item)
            let data = {
				id:item.id
			}
			let res = await http.post(api.deleteInterface, data);


			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                })
          this.M();
			}else{
                this.$message.error(res.data.msg)
            }

		},
		// 修改接口
		async updateInterface(){
			let data = this.addInterfaceInfo
			let res = await http.post(api.updateInterface, data);
			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: res.data.msg
				})
				this.modal1 = false
				this.getInterface()
          this.M();
			}else{
                this.$message.error(res.data.msg)
            }
		},

      // 方法名称

      async M(){
          let data=''
          let res = await http.post(api.methodList, data)
          this.orgList = res.data.data
          sessionStorage.setItem("methodList",JSON.stringify( res.data.data))
          JSON.parse(sessionStorage.getItem('methodList'))
          console.log(JSON.parse(sessionStorage.getItem('methodList')))
      }
  },
	mounted(){
        this.getInterface()
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.getInterface{
		padding: 20px;
		.head{
				margin-bottom: 25px;
			.title{
				font-size: 16px;
				color: #555;
				margin-bottom: 20px;
			}
			.search{
				display: flex;
				justify-content:flex-start;
				align-items: center;
				.li{
					margin-right: 20px;
					display: flex;
					justify-content: center;
					align-items: center;
					font-size: 13px;
					color: #999;
					.li_1{
						width: auto;
					}
				}
			}
		}
		.ps_but:hover{
			color: #24B393;
			border-color: #24B393;
		}
		.ps_but:focus{
			color: #fff !important;
			border-color: #24B393;
			background-color:#24B393
		}
		.el-button--text{
			color: #24B393;

		}
		.el-button:focus, .el-button:hover{
			color: #24B393;
		}
		.el-pagination{
			margin-top: 10px;
		}
		.ps_but{
			font-size: 13px !important;
			color: #24B393;
			border-color: #24B393;
		}
		.ps_but2{
			font-size: 16px;
			margin: 0 2px;
			color: #24B393;
			cursor: pointer;
		}
	}
</style>
