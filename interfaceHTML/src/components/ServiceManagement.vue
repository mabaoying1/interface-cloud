<template>
    <div v-loading="loading" class="ServiceManagement">
		<div class="head">
			<div class="title">服务配置信息管理</div>
			<div class="search">
				<div class="li">
					<div>项目名称：</div>
					<el-select v-model="projectNumber" placeholder="请选择" class="w_140"  size="small ">
                        <el-option
                            v-for="item in projecList"
                            :key="item.projectNumber"
                            :label="item.projectName"
                            :value="item.projectNumber">
                            </el-option>
                        </el-select>


				</div>
				<div class="li">
					<div class="li_1">服务名称：</div>
					<el-input
						size="small "
						placeholder="serch serviceName: "
						v-model="serviceName"
						clearable>
					</el-input>
				</div>
				<div class="li">
					<el-button  class="ps_but" size="small" @click="getServiceManagement()" >搜索</el-button>
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
				prop="serviceName"
				label="服务名称"
				width="">
				</el-table-column>
				<el-table-column
				align="center"
				prop="projectNumber"
				label="项目名称"
				width="">
					<template slot-scope="scope">
						<span>{{scope.row.projectNumber|p}}</span>
					</template>
				</el-table-column>
				<el-table-column
				align="center"
				prop="serviceID"
				label="服务id"
				width="">
				</el-table-column>
                <!-- <el-table-column
				align="center"
				prop="id"
				label="id"
				width="">
				</el-table-column> -->
                <el-table-column
				align="center"
				prop="port"
				label="端口"
				width="120">
				</el-table-column>
                <el-table-column
				align="center"
				prop="ipAddress"
				label="IP地址"
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
		<el-dialog title="平台账号" :visible.sync="modal1" :width="form1Width" >
			<el-form :model="addServiceManagementInfo">
				<el-form-item label="服务id" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addServiceManagementInfo.serviceID" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="服务名称" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addServiceManagementInfo.serviceName" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="ip" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addServiceManagementInfo.ipAddress" autocomplete="off"></el-input>
				</el-form-item>
                <el-form-item label="端口" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addServiceManagementInfo.port" autocomplete="off"></el-input>
				</el-form-item>
                <el-form-item label="项目编号" :label-width="formLabelWidth"  >
                    <el-select v-model="addServiceManagementInfo.projectNumber" placeholder="请选择" class="w_220"  size="small ">
                        <el-option

                        v-for="item in projecList"
                        :key="item.projectNumber"
                        :label="item.projectName"
                        :value="item.projectNumber">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="机构名称" :label-width="formLabelWidth">
                    <el-select v-model="addServiceManagementInfo.mechanismCode" placeholder="请选择" class="w_220"  size="small ">
                        <el-option
                        v-for="item in orgList"
                        :key="item.mechanismCode"
                        :label="item.mechanismName"
                        :value="item.mechanismCode">
                        </el-option>
                    </el-select>
                </el-form-item>





			</el-form>
			<div slot="footer" class="dialog-footer" v-loading="loading2">
				<el-button class="ps_but" @click="modal1 = false">取 消</el-button>
				<el-button v-if="addOrUpdate" class="ps_but bg_color1" type="primary" @click="addServiceManagement()">确 定</el-button>
				<el-button v-else class="ps_but bg_color1" type="primary" @click="updateServiceManagement()">修 改</el-button>
			</div>
		</el-dialog>
    </div>
</template>
<script>
import http from '../utils/http'
import api from '../utils/api'
import { setTimeout } from 'timers';
export default {
	name: 'ServiceManagement',
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
			serviceName:"",
			projectNumber:'',
            pageSize:10,
            pageNum:1,
            total:0,
			currentPage: 1,//分页-初始页码
            list:[],
			value1:'',
			value2:'',
			projecList: [],
			orgList: [],

			addServiceManagementInfo:{
				serviceID:'',//服务id
				serviceName:'',//服务名称
				ipAddress:'',//IP地址
				port:'',//端口
                projectNumber:'',//项目编号
                mechanismCode:'',//服务机构码


			}
		}
	},
	methods:{
		// 分页操作
		handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val
            this.getServiceManagement()
		},
		// 分页操作
		handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val
            this.getServiceManagement()
		},
		// 删除
		deleteItem(item){
			this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning' //可选类型 warning/info
			}).then(() => {
				this.deleteteServiceManagement(item)

			}).catch(() => {
				// cancel to do...

			})
		},
		// 修改
		update(item){
			this.modal1 = true
			this.addOrUpdate = !this.addOrUpdate
			console.log(1111111,item)
			this.addServiceManagementInfo = {
                id:item.id,//id
                serviceID:item.serviceID,//服务id
				serviceName:item.serviceName,//服务名称
				ipAddress:item.ipAddress,//IP地址
				port:item.port,//端口
                projectNumber:item.projectNumber,//项目编号
                mechanismCode:item.mechanismCode,//服务机构码
			}
			console.log(22222222222,this.addServiceManagementInfo)
		},
		// 新增
		add(){
			this.modal1=true
			this.addServiceManagementInfo = {
				serviceID:'',//服务id
				serviceName:'',//服务名称
				ipAddress:'',//IP地址
				port:'',//端口
                projectNumber:'',//项目编号
                mechanismCode:'',//服务机构码

			}
		},
        // 获取服务数据列表
        async getServiceManagement(){

			this.loading = true;
			let data = {
				serviceName:this.serviceName,
				projectNumber:this.projectNumber,
				pageNum:this.pageNum,
   				pageSize: this.pageSize,
			};
			let res = await http.post(api.getServiceManagement, data);
			console.log(res)

			if(res.data.code =='1'){
                this.loading = false
                this.list = res.data.data.list
                this.total = res.data.data.total

			}else{
				this.loading = false
			}

        },
        //新增服务
        async addServiceManagement(){

			this.loading2 = true;
			let data = this.addServiceManagementInfo
			let res = await http.post(api.addServiceManagement, data);
            console.log(res)
			if(res.data.code =='1'){
                this.loading2 = false
				this.modal1=false

				this.$message({
					type: 'success',
					message: `${res.data.msg}`
				})
                this.getServiceManagement()

			}else{
                this.loading2 = false;
                this.$message.error(res.data.msg)
			}

        },
        //删除接口
        async deleteteServiceManagement(item){
            console.log(item)
            let data = {
				id:item.id
			}
			console.log(item.id)
			let res = await http.post(api.deleteteServiceManagement, data);


			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
				})
				this.getServiceManagement()

			}else{
                this.$message.error(res.data.msg)
            }

		},
		// 修改接口
		async updateServiceManagement(){
            this.loading2 = true;

			let data = this.addServiceManagementInfo
            let res = await http.post(api.updateServiceManagement, data)
			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: res.data.msg
				})
                this.modal1 = false
				this.loading2 = false;

				this.getServiceManagement()

			}else{
				this.loading2 = false;
                this.$message.error(res.data.msg)
            }
		},
		// 获取项目列表
		async getProjecList(){
        let data = {
            projectType:''
        };
        let res = await http.post(api.projecList, data)
			console.log(res)
			this.projecList = res.data.data

		},
		// 获取机构列表
		async getOrgList(){
			let data=''
            let res = await http.post(api.OrgList, data)
			console.log(res)
			this.orgList = res.data.data

		}

	},
	mounted(){
		this.getServiceManagement()
		this.getProjecList()
		this.getOrgList()
	},
	filters:{
		P:function (nuber) {
			let p = JSON.parse(sessionStorage.getItem('projecList'))
			for (const key in p) {
				if(nuber == p[key].projectNumber){
					return p[key].projectName
				}
			}

		}
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.ServiceManagement{
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
						width: 85px;
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
