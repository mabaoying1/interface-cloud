<template>
    <div v-loading="loading" class="projectManagement">
		<div class="head">
			<div class="title">日志查询 </div>
			<div class="search">
				<div class="li">
					<div>项目名称：</div>
					<el-select v-model="projectNumber" placeholder="请选择" class="w_140"  size="small ">
						<el-option
                            :key="''"
                            :label="'全部'"
                            :value="''">
						</el-option>
						<el-option
						 	v-for="item in projecList"
                            :key="item.projectNumber"
                            :label="item.projectName"
                            :value="item.projectNumber">
						</el-option>
					</el-select>
				</div>
				<div class="li">
					<div>状态：</div>
					<el-select v-model="status" placeholder="请选择" class="w_140"  size="small ">
						<el-option
                            :key="'1'"
                            :label="'成功'"
                            :value="'1'">
						</el-option>
						<el-option
                            :key="'-1'"
                            :label="'失败'"
                            :value="'-1'">
						</el-option>
					</el-select>
				</div>
				<!-- <div class="li">
					<div>选择类型：</div>

					<el-select v-model="mechanismName" placeholder="请选择" class="w_140"  size="small ">
						<el-option
						v-for="item in orgList"
						:key="item.mechanismCode"
						:label="item.mechanismName"
						:value="item.mechanismCode">
						</el-option>
					</el-select>
				</div> -->
				<div class="li">
					<div class="li_1">方法名称:</div>
					<el-input
						class=""
						size="small "
						placeholder="serch methodId"
						v-model="methodId"
						clearable>
					</el-input>
				</div>
        <div class="li"  >
          <el-date-picker
            class="li_time"
             v-model="i_data"
            type="datetimerange"
            range-separator="至"
            value-format=" yyyy-MM-dd HH:mm:ss" format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </div>
				<div class="li">
					<el-button  class="ps_but" size="small" @click="getlog()" >搜索</el-button>
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
				prop="methodId"
				label="方法名字"
				width="">
				<template slot-scope="scope">
					<span>{{scope.row.methodId|M}}</span>
				</template>
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
				prop="status"
				label="状态"
				width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1" class="">{{scope.row.status==1?'成功':'失败'}}</span>
						<span v-if="scope.row.status!=1" class="red_font">{{scope.row.status==1?'成功':'失败'}}</span>
					</template>
				</el-table-column>
                <el-table-column
				align="center"
				prop="method"
				label="接口类型"
                width="120">
				</el-table-column>
				<el-table-column
				align="center"
				prop="reponseStatus"
				label="接口状态"
				width="120">
				</el-table-column>
        <el-table-column
          align="center"
          prop="requestTime"
          label="调用时间"
          width="200">
        </el-table-column>
				<el-table-column
				align="center"
				prop="takeUpTime"
				label="调用时长(ms)"
				width="160">
				</el-table-column>
                <el-table-column
				align="center"
				prop="serviceId"
				label="服务id"
				width="">
				</el-table-column>

				<el-table-column

				align="center"
				prop="handle"
				label="操作"
				width="100">
					<template slot-scope="scope">
						<i @click="getInfo(scope.row)"  class="ps_but2 el-icon-view"></i>

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
		<el-dialog title="对话表单" :visible.sync="dialogFormVisible" :width="form1Width" v-loading="loading2">
			<el-form  >
				<el-form-item label="method" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="info.method" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="methodId" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="info.methodId" autocomplete="off" placeholder="必填"></el-input>
				</el-form-item>
                <el-form-item label="projectNumber" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="info.projectNumber" autocomplete="off"></el-input>
				</el-form-item>
                <el-form-item label="requestParams" :label-width="formLabelWidth">
					<span>{{info.requestParams}}</span>
					<!-- <el-input class="w_220" v-model="info.requestParams" autocomplete="off"></el-input> -->
				</el-form-item>
                <el-form-item label="takeUpTime" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="info.takeUpTime" autocomplete="off"></el-input>
				</el-form-item>
				 <el-form-item label="responseParams" :label-width="formLabelWidth">
					<span>{{info.responseParams}}</span>
					<!-- <el-input class="w_220" v-model="info.requestParams" autocomplete="off"></el-input> -->
				</el-form-item>


			</el-form>
			<div slot="footer" class="dialog-footer">
				<!-- <el-button class="ps_but" @click="dialogFormVisible = false">取 消</el-button> -->
				<el-button class="ps_but bg_color1" type="primary" @click="dialogFormVisible = false">确 定</el-button>
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
      i_data:[],
      formLabelWidth:'150px',
			formInputWitdh:'120px',
			form1Width:'80%',
			addOrUpdate:true,
			dialogFormVisible:false,
            methodId:'',
            pageSize:10,
            pageNum:1,
            total:0,
			currentPage: 1,//分页-初始页码
            list:[],
			value1:'',
			value2:'',
			info:'',
			status:'',
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
			projecList:JSON.parse(sessionStorage.getItem('projecList')),
			orgList:JSON.parse(sessionStorage.getItem('orgList')),
			projectNumber:'',
			getLogInfo:{
				hospitalId:'',//  机构id
                serviceId:'',//   服务id
                projectNumber:'',//  项目编号
                methodId:'',//       接口方法ID
                method:'',//         接口类型，get,post
                requestTime:'',//    接口开始调用时长
                takeUpTime:'',//     接口调用时长
                status:'',//         接口返回状态
                reponseStatus:'',//  接口调用状态
                requestParams:'',//  出参
                responseParams:'',// 入参

			}
		}
	},
	methods:{
		// 分页操作
		handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val
            this.getlog()
		},
		// 分页操作
		handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val
            this.getlog()
		},
		// 查看详情
		getInfo(item){
			this.dialogFormVisible = true
			this.info = item
		},
        // 获取项目数据列表
        async getlog(){

			this.loading = true;
			let data = {
                hospitalId:this.hospitalId,//  机构id
                serviceId:this.serviceId,//   服务id
                projectNumber:this.projectNumber,//  项目编号
                methodId:this.methodId,//       接口方法ID
                method:this.method,//         接口类型，get,post
                requestTime:this.requestTime,//    接口开始调用时长
                takeUpTime:this.takeUpTime,//     接口调用时长
                status:this.status,//         接口返回状态
                reponseStatus:this.reponseStatus,//  接口调用状态
                requestParams:this.requestParams,//  出参
                responseParams:this.responseParams,// 入参
                requestStartTime:this.i_data[0],//请求调用时间开始
                requestEndTime:this.i_data[1],//请求调用时间结束
			        	pageNum:this.pageNum,
   				      pageSize: this.pageSize,
            };

			let res = await http.post(api.getlog, data);
			console.log(res)

			if(res.data.code =='1'){
                this.loading = false
                this.list = res.data.data.list
                this.total = res.data.data.total

			}else{
				this.loading = false
			}

        },



	},
	mounted(){
        this.getlog()
	},
	filters:{
		P:function (nuber) {
			let p = JSON.parse(sessionStorage.getItem('projecList'))
			for (const key in p) {
				if(nuber == p[key].projectNumber){
					return p[key].projectName
				}
			}

		},
		M:function (nuber) {
			let m = JSON.parse(sessionStorage.getItem('methodList'))
			for (const key in m) {
				if(nuber == m[key].interfaceId){
					return m[key].interfaceName
				}
			}
			if(nuber!=m[length]){
				return nuber
			}

		},
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
					margin-right: 20px;
					display: flex;
					justify-content: center;
					align-items: center;
					font-size: 13px;
					color: #999;
					.li_1{
						width: 95px;
					}
          .li_time{
            width: 350px;
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
