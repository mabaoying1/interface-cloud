<template>
    <div v-loading="loading" class="accountManagement">
		<div class="head">
			<div class="title">账号管理 </div>
			<div class="search">
				<div class="li">
					<div>状态：</div>
						
					<el-select v-model="status" placeholder="请选择" class="w_140"  size="small ">
						<el-option 
						v-for="item in options"
						:key="item.value"
						:label="item.label"
						:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="li">
					<div class="li_1">账号：</div>
					<el-input
						class="w_220"
						size="small "
						placeholder="serch account"
						v-model="account"
						clearable>
					</el-input>
				</div>
				<div class="li">
					<el-button  class="ps_but" size="small" @click="getAccountManagement()" >搜索</el-button>
					<el-button  class="ps_but" size="small " @click="add()">新增</el-button>

				</div>
				
			</div>
		</div>
		<div class="body">
			<!-- table -->
			<el-table
				:data="list"
				border
				size='mini'
				style="width: 482px">
                <el-table-column
				align="center"
				prop="account"
				label="账号"
				width="180">
				</el-table-column>
				<!-- <el-table-column
				align="center"
				prop="password"
				label="password"
				width="180">
				</el-table-column> -->
				<el-table-column
				align="center"
				prop="status"
				label="状态"
				width="180">
					<template slot-scope="scope">
						<span class="" v-if="scope.row.status==0">{{scope.row.status==0?'正常':'注销'}}</span>
						<span class="red_font" v-if="scope.row.status!=0" >{{scope.row.status==0?'正常':'注销'}}</span>
						<span></span>
					</template>
				</el-table-column>
                <!-- <el-table-column
				align="center"
				prop="id"
				label="id"
				width="180">
				</el-table-column> -->
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
		<el-dialog title="平台账号" :visible.sync="modal1" :width="form1Width" >
			<el-form :model="addAccountManagementinfo">
				<el-form-item label="账号" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addAccountManagementinfo.account" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="密码" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addAccountManagementinfo.password" show-password  autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
				<!-- 仅修改出现 -->
                <el-form-item label="状态" :label-width="formLabelWidth" v-if="addOrUpdate">
					<el-select v-model="addAccountManagementinfo.status" placeholder="请选择" class="w_140"  size="small ">
						<el-option 
						v-for="item in options"
						:key="item.value"
						:label="item.label"
						:value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
                

				

				

			</el-form>
			<div slot="footer" class="dialog-footer" v-loading="loading2">
				<el-button class="ps_but" @click="modal1 = false">取 消</el-button>
				<el-button v-if="!addOrUpdate" class="ps_but bg_color1" type="primary" @click="addAccountManagement()">确 定</el-button>
				<el-button v-else class="ps_but bg_color1" type="primary" @click="updateAccountManagement()">修 改</el-button>
			</div>
		</el-dialog>
    </div>
</template>
<script>
import http from '../utils/http'
import api from '../utils/api'
import { setTimeout } from 'timers';
export default {
	name: 'accountManagement',
	data () {
		return {
			// 加载框1
			loading:false,
			// 加载框2
			loading2:false,
			// 对话框
			modal1:false,
			formLabelWidth:'150px',
			formInputWitdh:'120px',
			form1Width:'80%',
			addOrUpdate:true,
            account:"",
            pageSize:10,
            pageNum:1,
            total:0,
			currentPage: 1,//分页-初始页码
            list:[],
			value1:'',
			status:'',
			options: [
				{
				value: "0",
				label: '正常'
				}, {
				value: "1",
				label: '注销'
				}
			],
			
			addAccountManagementinfo:{
				account:'',//账号
				password:'',//密码
				
                
			}
		}
	},
	methods:{
		// 分页操作
		handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val
            this.getProjecManagement()
		},
		// 分页操作
		handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val
            this.getProjecManagement()
		},
		// 删除
		deleteItem(item){
			this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning' //可选类型 warning/info
			}).then(() => {
				this.deleteAccountManagement(item)
				
			}).catch(() => {
				// cancel to do...
				      
			})
		},
		// 修改
		update(item){
			this.modal1 = true
			this.addOrUpdate = true
			console.log(item)
			this.addAccountManagementinfo = {
                id:item.id,//id
                account:item.account,//账号
                password:item.password,//密码
                status:item.status,//状态
			}
		},
		// 新增
		add(){
			
			this.modal1=true
			this.addOrUpdate = false
			this.addAccountManagementinfo = {
				status:'',//状态
				account:'',//账号
				password:'',//密码
			}
		},
        // 获取项目数据列表
        async getAccountManagement(){
            
			this.loading = true;       
			let data = {
				account:this.account,
				status:this.status,
				pageNum:this.pageNum,
   				pageSize: this.pageSize,
			};
			let res = await http.post(api.getAccountManagement, data);
			console.log(res)
			
			if(res.data.code =='1'){
                this.loading = false		
                this.list = res.data.data.list		
                this.total = res.data.data.total

			}else{
				this.loading = false
			}
		
        },
        //新增项目
        async addAccountManagement(){
            
			this.loading2 = true;       
			let data = this.addAccountManagementinfo

			let res = await http.post(api.addAccountManagement, data);
            console.log(res)
			if(res.data.code =='1'){
                this.loading2 = false		
				this.modal1=false
				
				this.$message({
					type: 'success',
					message: `${res.data.msg}`
				})
                this.getAccountManagement()

			}else{
                this.loading2 = false;
                this.$message.error(res.data.msg)
			}
		
        },
        //删除接口
        async deleteAccountManagement(item){
            console.log(item)
            let data = {
				id:item.id
			}
			console.log(item.id)
			let res = await http.post(api.deleteAccountManagement, data);
          
			
			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
				})
				this.getAccountManagement()

			}else{
                this.$message.error(res.data.msg)
            }
		
		},
		// 修改接口
		async updateAccountManagement(){
			let data = this.addAccountManagementinfo 
			let res = await http.post(api.updateAccountManagement, data)
			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: res.data.msg
				})
				this.modal1 = false
				this.getAccountManagement()

			}else{
                this.$message.error(res.data.msg)
            }
		}

	
	},
	mounted(){
        this.getAccountManagement()
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.accountManagement{
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
