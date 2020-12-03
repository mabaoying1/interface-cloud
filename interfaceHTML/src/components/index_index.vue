<template>
    <div v-loading="loading" class="page1">
		<div class="head">
			<div class="title">我是某二级页面名称 — 表格页展示</div>
			<div class="search">
				<div class="li">
					<div>选择日期：</div>
					<!-- <el-input v-model="input" placeholder="请输入内容" size='small' ></el-input> -->
					<el-date-picker
						v-model="value1"
						size="small "
						class="w_140"
						type="date"
						placeholder="选择日期">
					</el-date-picker>
				</div>
				<div class="li">
					<div>选择类型：</div>
						
					<el-select v-model="value2" placeholder="请选择" class="w_140"  size="small ">
						<el-option 
						v-for="item in options"
						:key="item.value"
						:label="item.label"
						:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="li">
					<div class="li_1">输入内容：</div>
					<el-input
						class="w_220"
						size="small "
						placeholder="请输入内容"
						v-model="input"
						clearable>
					</el-input>
				</div>
				<div class="li">
					<el-button  class="ps_but" size="small " >信息按钮</el-button>
					<el-button  class="ps_but" size="small ">信息按钮</el-button>

				</div>
				
			</div>
		</div>
		<div class="body">
			<!-- table -->
			<el-table
				:data="tableData"
				border
				size='mini'
				style="width: 100%">
				<el-table-column
				align="center"
				prop="date"
				label="日期"
				width="180">
				</el-table-column>
				<el-table-column
				align="center"
				prop="name"
				label="姓名"
				width="180">
				</el-table-column>
				<el-table-column
				align="center"
				prop="address"
				label="地址">
				</el-table-column>
				<el-table-column
				align="center"
				prop="handle"
				label="操作"
				width="120">
					<template slot-scope="scope">
						<i @click="handleClick(scope.row)"  class="ps_but2 el-icon-edit-outline"></i>
						<i @click="dialogFormVisible=true"  class="ps_but2 el-icon-view"></i>
						<i @click="deleteItem(scope.row)"  class="ps_but2 el-icon-delete"></i>
						<i @click="handleClick(scope.row)"  class="ps_but2 el-icon-share"></i>
						
					</template>
				</el-table-column>
			</el-table>
			<!-- 分页 参数：total 总条目数 ;currentPage 当前页码-->
			<el-pagination
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				:current-page="currentPage"
				:page-sizes="[10, 20, 30, 40]"
				:page-size="100"
				layout="total, sizes, prev, pager, next, jumper"
				:total="400">
			</el-pagination>
		</div>
		<!-- 弹出表单对话窗口 -->
		<el-dialog title="对话表单" :visible.sync="dialogFormVisible" :width="form1Width" v-loading="loading2">
			<el-form :model="form1" >
				<el-form-item label="活动名称" :label-width="formLabelWidth">
					<el-input class="w_220" v-model="form1.name" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="活动区域" :label-width="formLabelWidth">
					<el-select v-model="value2" placeholder="请选择" class="w_220"  size="small ">
						<el-option 
							v-for="item in options"
							:key="item.value"
							:label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-form-item>

				<el-form-item label="操作指南" :label-width="formLabelWidth">
					<el-button :plain="true" class="ps_but" @click="alertSuccess">成功</el-button>
					<el-button :plain="true" class="ps_but" @click="alertWarning">警告</el-button>
					<el-button :plain="true" class="ps_but" @click="alertInfo">消息</el-button>
					<el-button :plain="true" class="ps_but" @click="alertError">错误</el-button>
				</el-form-item>

				<el-form-item label="遮罩层" :label-width="formLabelWidth">
					<el-button :plain="true" class="ps_but" @click="changeLoading">遮罩层</el-button>
					
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button class="ps_but" @click="dialogFormVisible = false">取 消</el-button>
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
	name: 'index_index',
	data () {
		return {
			// 加载框1
			loading:false,
			// 加载框2
			loading2:false,
			// 对话框
			dialogFormVisible:false,
			// formLabelWidth
			formLabelWidth:'120px',
			// formInputWitdh
			formInputWitdh:'120px',
			// form1Width
			form1Width:'80%',
			input:'',
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
			currentPage: 2,
			tableData:[{
				date: '2016-05-03',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-02',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-04',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-01',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-08',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-06',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-06',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-06',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-06',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-06',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
				}, {
				date: '2016-05-07',
				name: '王小虎',
				address: '上海市普陀区金沙江路 1518 弄'
			}],
			form1:{
				name: '',
				region: '',
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
		},
		// 分页操作
		handleCurrentChange(val) {
			console.log(`当前页: ${val}`);
		},
		// 删除
		deleteItem(item){
			alert(item)
			this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning' //可选类型 warning/info
			}).then(() => {
				//confirm to do....
				
			}).catch(() => {
				// cancel to do...
				      
			})
		},
		// 提示
		alertSuccess(){
			this.$message({
				type: 'success',
				message: '删除成功!'
			})
		},
		alertInfo(){
			this.$message({
				type: 'info',
				message: '提示信息'
			});   
		},
		alertWarning(){
			this.$message({
				message: '警告哦，这是一条警告消息',
				type: 'warning'
			});
		},
		alertError(){
			this.$message.error('错了哦，这是一条错误消息')
		},
		// 加载 遮罩层
		changeLoading(){
			// 改变 div属性 v-loading=true/false 状态 操作遮罩层 
			this.loading2 = !this.loading2
			setTimeout(()=>{
				this.loading2 = !this.loading2
			},1500)
		},
		

	
	},
	mounted(){
	
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.page1{
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
