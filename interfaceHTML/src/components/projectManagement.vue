<template>
    <div v-loading="loading" class="projectManagement">
		<div class="head">
			<div class="title">项目列表 </div>
			<div class="search">
				<div class="li">
					<div class="li_1">项目名称：</div>
					<el-input
						class="w_120"
						size="small "
						placeholder="serch projectName"
						v-model="projectName"
						clearable>
					</el-input>
				</div>
				<div class="li">
					<div class="li_1">账号：</div>
					<el-input
						class=""
						size="small "
						placeholder="serch username"
						v-model="username"
						clearable>
					</el-input>
				</div>
				<div class="li">
					<el-button  class="ps_but" size="small" @click="getProjecManagement()" >搜索</el-button>
					<el-button  class="ps_but" size="small " @click="add(); addOrUpdate=true">新增</el-button>

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
				prop="projectName"
				label="项目名称"
				width="">
				</el-table-column>
				<el-table-column
				align="center"
				prop="projectNumber"
				label="项目编号"
				width="">
				</el-table-column>
				<el-table-column
				align="center"
				prop="username"
				label="账号"
				width="">
				</el-table-column>
				<el-table-column
				width="120"
				align="center"
				prop="status"
				label="状态">
				<template slot-scope="scope">
					<span v-if="scope.row.status==0" >{{scope.row.status==0?'正常':'注销'}}</span>
					<span v-if="scope.row.status==1" class="red_font">{{scope.row.status==1?'注销':'正常'}}</span>
				</template>
				</el-table-column>
				<el-table-column
				align="center"
				prop="projectType"
				label="项目状态"
				width="150">
				<template slot-scope="scope">
					<span>{{scope.row.projectType==1?'标准':'第三方'}}</span>
				</template>
				</el-table-column>
                <el-table-column
				align="center"
				prop="projectIntroduction"
				label="项目介绍"
				>
				<template slot-scope="scope">
					<div style="position: relative">
					<div class="hiddenOverText" :title="scope.row.projectIntroduction">{{scope.row.projectIntroduction.substr(0,40)}}</div>
					<span v-if="JSON.stringify(scope.row.projectIntroduction).length > 40" class="overText">....</span>
					</div>
				</template>
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
		<el-dialog title="项目信息" :visible.sync="modal1" :width="form1Width" >
			<el-form :model="addProjecManagementInfo"  >
				<el-form-item label="账号" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addProjecManagementInfo.username" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="密码" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addProjecManagementInfo.password" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="项目代码" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addProjecManagementInfo.projectNumber" autocomplete="off"></el-input>
					<!-- <el-select v-model="addProjecManagementInfo.projectNumber" placeholder="请选择" class="w_220"  size="small ">
						<el-option
						 	v-for="item in projecList"
                            :key="item.projectNumber"
                            :label="item.projectName"
                            :value="item.projectNumber">
						</el-option>
					</el-select> -->
				</el-form-item>
                <el-form-item label="项目名称" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addProjecManagementInfo.projectName" autocomplete="off"></el-input>
				</el-form-item>
                <el-form-item label="项目介绍" :label-width="formLabelWidth">
					<el-input type="textarea" class="w_220" v-model="addProjecManagementInfo.projectIntroduction" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="机构码" :label-width="formLabelWidth">
                    <!-- <el-select v-model="addProjecManagementInfo.mechanismCode" placeholder="请选择" class="w_140"  size="small ">
                        <el-option
                        v-for="item in orgList"
                        :key="item.mechanismCode"
                        :label="item.mechanismName"
                        :value="item.mechanismCode">
                        </el-option>
                    </el-select> -->
					<!-- <span>{{addProjecManagementInfo.mechanismCode}}</span>aaa -->
					<el-select v-model="addProjecManagementInfo.mechanisms" multiple placeholder="请选择">
						<el-option
						v-for="item in orgList"
						:key="item.mechanismCode"
						:label="item.mechanismName"
						:value="item.mechanismCode">
						</el-option>
					</el-select>
                </el-form-item>
				<el-form-item label="接口" :label-width="formLabelWidth">

					<el-select v-model="addProjecManagementInfo.privileges"  multiple placeholder="请选择">

						<el-option
 						v-for="item in API"
						:key="item.interfaceId"
						:label="item.interfaceName"
						:value="item.interfaceId">
						</el-option>
					</el-select>
                </el-form-item>

				<el-form-item label="项目类型" :label-width="formLabelWidth">

					<el-select v-model="addProjecManagementInfo.projectType"  placeholder="必选">
						<el-option
						:key="'标准'"
						:label="'标准'"
						:value="'1'">
						</el-option>
						<el-option
						:key="'第三方'"
						:label="'第三方'"
						:value="'2'">
						</el-option>
					</el-select>
                </el-form-item>



			</el-form>
			<div slot="footer" class="dialog-footer" v-loading="loading2">
				<el-button class="ps_but" @click="modal1 = false">取 消</el-button>
				<el-button v-if="addOrUpdate" class="ps_but bg_color1" type="primary" @click="addProjec()">确 定</el-button>
				<el-button v-else class="ps_but bg_color1" type="primary" @click="updateProjecManagement()">修 改</el-button>
			</div>
		</el-dialog>
    </div>
</template>
<script>
import http from '../utils/http'
import api from '../utils/api'
import { setTimeout } from 'timers';
export default {
	name: 'projectManagement',
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
			username:'',
			projectName:'',
            pageSize:10,
            pageNum:1,
            total:0,
			currentPage: 1,//分页-初始页码
            list:[],
			value1:'',
			status:'',
			options: [
				{
				value: 0,
				label: '0'
				},
				{
				value: 1,
				label: '1'
				}
			],
			API:'',
      oldOptions: [],
			projecList:JSON.parse(sessionStorage.getItem('projecList')),
			orgList:JSON.parse(sessionStorage.getItem('orgList')),
			addProjecManagementInfo:{
				username:'',//账号
				password:'',//密码
				projectNumber:'',//项目编号
				projectName:'',//项目名称
				projectIntroduction:'',//项目介绍
				privileges:[],//权限
				mechanisms:['1'],//账号下机构信息
				projectType:'',//项目状态

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
				this.deleteProjecManagement(item)

			}).catch(() => {
				// cancel to do...

			})
		},
		// 修改
		update(item){
			this.modal1 = true
			this.addOrUpdate = false
			console.log(item)
			this.addProjecManagementInfo = {
				id:item.id,//id
				username:item.username,//账号
				password:item.password,//密码
				projectNumber:item.projectNumber,//项目编号
				projectName:item.projectName,//项目名称
				projectIntroduction:item.projectIntroduction,//项目介绍
				privileges:item.privileges,//权限
				mechanisms:item.mechanisms,//账号下机构信息
				projectType:item.projectType,//项目状态
				// mechanismCode:item.mechanismCode,//机构
			}
		},
		// 新增
		add(){
			this.modal1=true
			this.addProjecManagementInfo = {
				id:'',//id
				username:'',//账号
				password:'',//密码
				projectNumber:'',//项目编号
				projectName:'',//项目名称
				projectIntroduction:'',//项目介绍
				privileges:this.oldOptions,//权限
				mechanisms:[],//账号下机构信息
				projectType:'',
			}
		},
        // 获取项目数据列表
        async getProjecManagement(){

			this.loading = true;
			let data = {
				status:this.status,
				projectName:this.projectName,
                username:this.username,
				pageNum:this.pageNum,
   				pageSize: this.pageSize,
			};
			let res = await http.post(api.getProjecManagement, data);
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
        async addProjec(){

			this.loading2 = true;
			let data = this.addProjecManagementInfo
			let res = await http.post(api.addProjecManagement, data);
            console.log(res)

			if(res.data.code =='1'){
                this.loading2 = false
				this.modal1=false

				this.$message({
					type: 'success',
					message: `${res.data.msg}`
				})

          this.P();

			}else{
                this.loading2 = false;
                this.$message.error(res.data.msg)
			}

        },
        //删除接口
        async deleteProjecManagement(item){
            console.log(item)
            let data = {
				id:item.id
			}
			console.log(item.id)
			let res = await http.post(api.deleteProjecManagement, data);


			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
				})
				this.getProjecManagement()
          this.P();

			}else{
                this.$message.error(res.data.msg)
            }

		},
		// 修改接口
		async updateProjecManagement(){
			this.loading2 = true;
			let data = this.addProjecManagementInfo
			let res = await http.post(api.updateProjecManagement, data)
			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: res.data.msg
				})
				this.modal1 = false
				this.loading2 = false;
				this.getProjecManagement()
          this.P();
			}else{
				this.loading2 = false;
                this.$message.error(res.data.msg)
            }
		},
		// 查询接口列表
		async getAPI(){
			let data = {
				pageNum:1,
   				pageSize: 999,
			};
			let res = await http.post(api.getInterface, data)
			if(res.data.code =='1'){

				  this.API = res.data.data.list
				  for (var ls in this.API){
              this.oldOptions.push(this.API[ls].interfaceId)
          }

			}else{

            }
		},
      async P(){
          let data = {
              projectType:''
          };
          let res = await http.post(api.projecList, data)
          sessionStorage.setItem("projecList",JSON.stringify( res.data.data))
          JSON.parse(sessionStorage.getItem('projecList'))
          console.log(JSON.parse(sessionStorage.getItem('projecList')))
      },

	},
	mounted(){
		this.getProjecManagement()
		this.getAPI()
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.projectManagement{
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
					widows: auto;
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
