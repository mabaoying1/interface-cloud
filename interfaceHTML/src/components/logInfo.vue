<template>
    <div v-loading="loading" class="logInfo">
		<div class="head">
			<div class="tab">
                <div class="ul">
                    <div class="li" @click="tabOrg(index,item)" :class="index==bingo? 'bingo':''" v-for="(item,index) in tab.mechanismDetails"><span>{{item.mechanismName}}</span></div>
                    <div class="li li_1"></div>
                </div>
                <div class="ul2">
                    <div class="li li1">
                        <div class="l">
							<i class="icon">ID </i>

						</div>
                        <div class="r">
                            <p class="p1">{{orgInfo.mechanismName}}</p>
                            <p class="p2">{{orgInfo.mechanismCode}} 账号</p>
                        </div>
                    </div>
                    <div class="li li2">
                        <div class="l">
							<i class="icon iconfont icon-icon-bar-graph "></i>
						</div>
                        <div class="r">
                            <p class="p1">{{list.total}}</p>
                            <p class="p2">请求总数</p>
                        </div>
                    </div>
                    <div class="li li3">
                        <div class="l">
							<i class="icon iconfont icon-gou "></i>
						</div>
                        <div class="r">
                            <p class="p1">{{list.logSuccessTotal}}</p>
                            <p class="p2">成功</p>
                        </div>
                    </div>
                    <div class="li li4">
                        <div class="l">
							<i class="icon iconfont icon-fork "></i>
						</div>
                        <div class="r">
                            <p class="p1">{{list.logErrorTotal}}</p>
                            <p class="p2">失败</p>
                        </div>
                    </div>
                </div>
            </div>
			<div class="search">
				<!-- <div class="li">
					<div>项目名称：</div>
					<el-select v-model="projectNumber" placeholder="请选择" class="w_140"  size="small ">
						<el-option
						 	v-for="item in projecList"
                            :key="item.projectNumber"
                            :label="item.projectName"
                            :value="item.projectNumber">
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
				<div class="li">
					<div class="li_2">状态:</div>
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
					<el-button  class="ps_but" size="small" @click="getService()" >搜索</el-button>
					<!-- <el-button  class="ps_but" size="small " @click="add() ; addOrUpdate=true">新增</el-button> -->

				</div>

			</div>
		</div>
		<div class="body">
			<!-- table -->
			<el-table
				:data="list.list"
				border
				size='mini'
				style="width: 100%">
                <el-table-column
				align="center"
				prop="methodId"
				label="方法名称"
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
				prop="method"
				label="接口类型"
                width="">
				</el-table-column>
				<el-table-column
				align="center"
				prop="reponseStatus"
				label="接口返回状态"
				width="">
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
				label="接口调用时长(ms)"
				width="160">
				</el-table-column>
                <el-table-column
				align="center"
				prop="serviceId"
				label="服务id"
				width="180">
				</el-table-column>
				<el-table-column
				align="center"
				prop="status"
				label="状态"
				width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1" class="">{{scope.row.status==1?'成功':'失败'}}</span>
						<span v-if="scope.row.status!=1" class="red_font">{{scope.row.status==1?'成功':'失败'}}</span>
					</template>
				</el-table-column>

				<el-table-column

				align="center"
				prop="handle"
				label="操作"
				width="120">
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
	name: 'logInfo',
	data () {
		return {
			// 加载框1
			loading:false,
			// 加载框2
			loading2:false,
			// 对话框
			modal1:false,
        i_data:[],
			dialogFormVisible:false,
			formLabelWidth:'150px',
			formInputWitdh:'120px',
			form1Width:'80%',
			addOrUpdate:true,
            methodId:'',
            pageSize:10,
            pageNum:1,
			total:0,
			status:'',
			orgInfo:'',
			info:'',
			currentPage: 1,//分页-初始页码
            list:[],
			projecList:JSON.parse(sessionStorage.getItem('projecList')),
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
            bingo:'0',
			tab:[],
			getLogInfo:{
			},
			mechanismCode:''
		}
	},
	methods:{
		// 分页操作
		handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val
            this.getService()
		},
		// 分页操作
		handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val
            this.getService()
		},
		tabOrg(index,item){
			this.bingo = index
			console.log(item)
			this.mechanismCode = item.mechanismCode
			this.orgInfo = item
			this.getService(item)

		},
        // 获取机构列表
        async getOrg(){
			let data = {

                projectNumber:sessionStorage.getItem('projectNumber'),//  项目编号
				// projectNumber:'1001',//  项目编号



            };
			let res = await http.post(api.getIogInfo, data);

			console.log(res)
			if(res.data.code =='1'){
				this.tab = res.data.data
				this.orgInfo = this.tab.mechanismDetails[0]
				console.log(this.tab)
				this.mechanismCode = this.tab.mechanismDetails[0].mechanismCode
				this.getService()

			}else{

			}

		},
		// 获取服务列表
		async getService(item){
			this.loading = true
			let data = {
          requestStartTime:this.i_data[0],//请求调用时间开始
          requestEndTime:this.i_data[1],//请求调用时间结束
                projectNumber:sessionStorage.getItem('projectNumber'),//  项目编号
				// projectNumber:'1002',//  项目编号
				pageNum:this.pageNum,
   				pageSize: this.pageSize,
				mechanismCode:this.mechanismCode,//机构id
				// "hospitalId":null,//'机构ID',
				// "method":null,//'方法类型',
				"methodId":this.methodId,//'接口方法名',
				// "reponseStatus":null,//'接口调用状态',
				// "requestParams":null,//'接口入参',
				// "requestTime":null,//'接口调用时间',
				// "responseParams":null,//接口出差',
				// "serviceId":null,//'服务ID',
				"status":this.status,//'状态',
				// "takeUpTime":null,//'接口调用时长',

            };
			let res = await http.post(api.getLogInfo, data);
			console.log(res)
			if(res.data.code =='1'){
				this.list = res.data.data
				this.loading = false
				// console.log(this.tab)
				this.total = res.data.data.total

			}else{
				this.loading = false
			}
		},
		// 查看详情
		getInfo(item){
			this.dialogFormVisible = true
			this.info = item
		}


	},
	mounted(){
        this.getOrg()
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
			let m = JSON.parse(sessionStorage.getItem('orgList'))
			for (const key in m) {
				if(nuber == m[key].mechanismCode){
					return m[key].mechanismName
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
	.logInfo{
		padding: 20px;
		.head{
				margin-bottom: 25px;
			.title{
				font-size: 16px;
				color: #555;
				margin-bottom: 20px;
			}
			.search{
                margin-top: 10px;
				display: flex;
				justify-content:flex-start;
				align-items: center;
				.li{
					// width: auto;
					// width:fit-content;
					margin-right: 20px;
					display: flex;
					justify-content: flex-start;
					align-items: center;
					font-size: 13px;
					color: #999;
					.li_1{
						height: 100%;
						width:80px;
					}
					.li_2{
						width:40px;
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
        .tab{
            height: auto;
            .ul{
                height: 50px;
                display: flex;
                justify-content: flex-start;
                align-items: center;
                .li{
                    height: 50px;
                    box-sizing: border-box;
                    width: 100px;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    border-bottom: 2px solid #E4E7ED;
                    span{
                        cursor: pointer;
                        color: #555555;
                        font-weight: 600;
                    }
                }
                .li_1{
                    flex: 1;
                }
                .bingo{
                    border-bottom: 2px solid #409EFF;
                }

            }
            .ul2{
                display: flex;
                justify-content: center;
                align-items: center;
                margin:  20px 0;
                .li{
					width: auto;
                    font-weight: 600;
                    color: #A9B0B9;
                    height: 80px;
                    border: 1px solid #E4E7ED;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;

                    .l{
                        margin-left: 30px;
                        width: 32px;
                        height: 32px;
                        background: #A9B0B9;
                        border-radius: 50%;
						display: flex;
						justify-content: center;
						align-items: center;
						.icon{
							color: #fff;
							color: 20px;
						}
                    }
                    .r{
                        flex: 1;
                        margin-right: 30px;
                        p{
                            text-align: right;
                        }
                        .p1{
                            font-size: 24px;
                        }
                        .p2{
                            margin-top: 10px;
                            font-size: 14px;
                        }
                    }
                }
                .li1{
                    flex: 3;

                }
                .li2,.li3,.li4{
                    border: 1px solid #E4E7ED;
                    height: 80px;
                    flex: 2;
                    margin-left: 10px;
                }
                .li2{
                    color: #E6A23C;
                    .l{
                        background: #E6A23C;
                    }
                }
                .li3{
                    color: #67C23A;
                    .l{
                        background: #67C23A;
                    }
                }
                .li4{
                    color: #F56C6C;
                    .l{
                        background: #F56C6C;
                    }
                }
            }
        }
	}
</style>
