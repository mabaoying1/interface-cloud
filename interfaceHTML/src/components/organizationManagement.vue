<template>
    <div v-loading="loading" class="organizationManagement">
		<div class="head">
			<div class="title">机构管理 </div>
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
					<div class="li_1">机构名称：</div>
					<el-input
						class="w_220"
						size="small "
						placeholder="机构名称"
						v-model="mechanismName"
						clearable>
					</el-input>
				</div>
				<div class="li">
					<el-button  class="ps_but" size="small" @click="getOrgManagement()" >搜索</el-button>
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
				prop="mechanismName"
				label="机构名称"
				width="">
				</el-table-column>
				<el-table-column
				align="center"
				prop="mechanismCode"
				label="机构代码"
				width="">
				</el-table-column>
				<el-table-column
				align="center"
				prop="mechanismServiceCode"
				label="机构服务码"
				width="">
				</el-table-column>
                <el-table-column
				align="center"
				prop="mechanismIntroduction"
				label="机构介绍"
				width="">
				</el-table-column>
        <el-table-column
          align="center"
          prop="provinceId"
          label="省行政编码"
          v-if="showClose"
          width="">
        </el-table-column>
        <el-table-column
          align="center"
          prop="provinceName"
          v-if="showClose"
          label="省名称"
          width="">
        </el-table-column>
        <el-table-column
          align="center"
          prop="cityId"
          v-if="showClose"
          label="城市行政编码"
          width="">
        </el-table-column>
        <el-table-column
          align="center"
          prop="cityName"
          v-if="showClose"
          label="城市名称"
          width="">
        </el-table-column>
        <el-table-column
          align="center"
          prop="districtId"
          v-if="showClose"
          label="区域行政编码"
          width="">
        </el-table-column>
        <el-table-column
          align="center"
          prop="districtName"
          v-if="showClose"
          label="区域名称"
          width="">
        </el-table-column>


				<el-table-column
				align="center"
				prop="status"
				label="状态"
				width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.status==0" >{{scope.row.status==0?'存在':'删除'}}</span>
					<span v-if="scope.row.status!=0" class="red_font">{{scope.row.status==0?'存在':'删除'}}</span>
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
		<el-dialog title="机构信息" :visible.sync="modal1" :width="form1Width" >
			<el-form :model="addOrgManagementInfo"  >
				<el-form-item label="机构代码" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addOrgManagementInfo.mechanismCode" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="机构名称" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addOrgManagementInfo.mechanismName" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="机构服务码" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addOrgManagementInfo.mechanismServiceCode" autocomplete="off"></el-input>
				</el-form-item>
                <el-form-item label="介绍" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="addOrgManagementInfo.mechanismIntroduction" autocomplete="off"></el-input>
				</el-form-item>

        <el-form-item label="省行政编码" :label-width="formLabelWidth">
          <el-input class="w_220" v-model="addOrgManagementInfo.provinceId" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="省名称" :label-width="formLabelWidth">
          <el-input class="w_220" v-model="addOrgManagementInfo.provinceName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="城市行政编码" :label-width="formLabelWidth">
          <el-input class="w_220" v-model="addOrgManagementInfo.cityId" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="城市名称" :label-width="formLabelWidth">
          <el-input class="w_220" v-model="addOrgManagementInfo.cityName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="区域行政编码" :label-width="formLabelWidth">
          <el-input class="w_220" v-model="addOrgManagementInfo.districtId" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="区域名称" :label-width="formLabelWidth">
          <el-input class="w_220" v-model="addOrgManagementInfo.districtName" autocomplete="off"></el-input>
        </el-form-item>




			</el-form>
			<div slot="footer" class="dialog-footer" v-loading="loading2">
				<el-button class="ps_but" @click="modal1 = false">取 消</el-button>
				<el-button v-if="addOrUpdate" class="ps_but bg_color1" type="primary" @click="addOrg()">确 定</el-button>
				<el-button v-else class="ps_but bg_color1" type="primary" @click="updateOrgManagement()">修 改</el-button>
			</div>
		</el-dialog>
    </div>
</template>
<script>
import http from '../utils/http'
import api from '../utils/api'
import { setTimeout } from 'timers';
export default {
	name: 'organizationManagement',
	data () {
		return {
			// 加载框1
			loading:false,
			// 加载框2
			loading2:false,
			// 对话框
			modal1:false,
			formLabelWidth:'180px',
			formInputWitdh:'120px',
			form1Width:'80%',
			addOrUpdate:true,
      mechanismName:'',
            pageSize:10,
            pageNum:1,
            total:0,
			currentPage: 1,//分页-初始页码
            list:[],
			value1:'',
			value2:'',
			options: [
				{
				value: '选项1',
				label: '黄金糕'
				}, {
				value: '选项2',
				label: '双皮奶'
				}, {
				value: '选项3',
				label: '蚵仔煎'
				}, {
				value: '选项4',
				label: '龙须面'
				}, {
				value: '选项5',
				label: '北京烤鸭'
				}
			],

			addOrgManagementInfo:{
				mechanismCode:'',//机构代码
				mechanismName:'',//机构名称
				mechanismServiceCode:'',//机构对应服务代码
				projectName:'',//项目名称
				mechanismIntroduction:'',//机构介绍
         provinceId:'',//省行政编码
        provinceName:'',//省名称
        cityId:'',//城市行政编码
        cityName:'',//城市名称
        districtId:'',//区域行政编码
        districtName:''//区域名称
			}
		}
	},
	methods:{
		// 分页操作
		handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val
            this.getOrgManagement()
		},
		// 分页操作
		handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val
            this.getOrgManagement()
		},
		// 删除
		deleteItem(item){
			this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning' //可选类型 warning/info
			}).then(() => {
				this.deleteOrgManagement(item)

			}).catch(() => {
				// cancel to do...

			})
		},
		// 修改
		update(item){
			this.modal1 = true
			this.addOrUpdate = false
			console.log(item)
			this.addOrgManagementInfo = {
				id:item.id,//id
				mechanismCode:item.mechanismCode,//机构代码
				mechanismName:item.mechanismName,//机构名称
				mechanismServiceCode:item.mechanismServiceCode,//机构对应服务代码
				projectName:item.projectName,//项目名称
        provinceId:item.provinceId,//省行政编码
        provinceName:item.provinceName,//省名称
        cityId:item.cityId,//城市行政编码
        cityName:item.cityName,//城市名称
        districtId:item.districtId,//区域行政编码
        districtName:item.districtName,//区域名称
				mechanismIntroduction:item.mechanismIntroduction,//机构介绍
			}
		},
		// 新增
		add(){
			this.modal1=true
			this.addOrgManagementInfo = {
				mechanismCode:'',//机构代码
				mechanismName:'',//机构名称
				mechanismServiceCode:'',//机构对应服务代码
				projectName:'',//项目名称
				mechanismIntroduction:'',//机构介绍

			}
		},
        // 获取项目数据列表
        async getOrgManagement(){

			this.loading = true;
			let data = {
                status:'',
        mechanismName:this.mechanismName,
				pageNum:this.pageNum,
   				pageSize: this.pageSize,
			};
			let res = await http.post(api.getOrg, data);
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
        async addOrg(){

			this.loading2 = true;
			let data = this.addOrgManagementInfo
			let res = await http.post(api.addOrg, data);
            console.log(res)

			if(res.data.code =='1'){
                this.loading2 = false
				this.modal1=false
				this.getOrgManagement()
				this.$message({
					type: 'success',
					message: `${res.data.msg}`
				})
          this.O();


			}else{
                this.loading2 = false;
                this.$message.error(res.data.msg)
			}

        },
        //删除接口
        async deleteOrgManagement(item){
            console.log(item)
            let data = {
				id:item.id
			}
			console.log(item.id)
			let res = await http.post(api.deleteOrg, data);


			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
				})
				this.getProjecManagement()
          this.O();
			}else{
                this.$message.error(res.data.msg)
            }

		},
		// 修改接口
		async updateOrgManagement(){
			this.loading2 = true;
			let data = this.addOrgManagementInfo
			let res = await http.post(api.updateOrg, data)
			if(res.data.code =='1'){
                this.$message({
                    type: 'success',
                    message: res.data.msg
				})
				this.modal1 = false
				this.loading2 = false;
				this.getOrgManagement()
          this.O();

			}else{
				this.loading2 = false;
                this.$message.error(res.data.msg)
            }
		},
      // 机构
      async O(){
          let data=''
          let res = await http.post(api.OrgList, data)
          this.orgList = res.data.data
          sessionStorage.setItem("orgList",JSON.stringify( res.data.data))
          JSON.parse(sessionStorage.getItem('orgList'))
          console.log(JSON.parse(sessionStorage.getItem('orgList')))
      }


	},
	mounted(){
        this.getOrgManagement()
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.organizationManagement{
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
