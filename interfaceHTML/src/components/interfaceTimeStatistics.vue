<template>
    <div v-loading="loading" class="interfaceTimeStatistics">
        <div class="head">
            <div class="search">
                <div class="li">
                    <div>项目：</div>

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
                    <div>机构：</div>
                    <el-select v-model="mechanismCode" placeholder="请选择" class="w_140"  size="small ">
                        <el-option

                        :key="''"
                        :label="'全部'"
                        :value="''">
                        </el-option>
                        <el-option
                        v-for="item in orgList"
                        :key="item.mechanismCode"
                        :label="item.mechanismName"
                        :value="item.mechanismCode">
                        </el-option>
                    </el-select>
                </div>
                <div class="li"></div>
                <div class="li">
                    <el-date-picker
                    class="w_220"
                        v-model="i_data"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                    </el-date-picker>
                </div>
                <div class="li w_140">
					<div class="li_1">调用时长:</div>
					<el-input
						class=""
						size="small"
						placeholder="单位秒"
						v-model="takeUpTime"
						clearable>
					</el-input>
				</div>

                <div class="li">
                    <el-button  class="ps_but" size="medium " @click="getChart()" >搜索</el-button>
                </div>

            </div>
        </div>


		<div id="tree1"></div>

    </div>
</template>
<script>
import http from '../utils/http'
import api from '../utils/api'
import { setTimeout } from 'timers';
export default {
	name: 'interfaceTimeStatistics',
	data () {
		return {
			// 加载框1
            loading:false,
            value2:'',
            Project:'',
            i_data:[new Date(new Date().setMonth(new Date().getMonth()-1)),new Date()],
            list:[],
            options:[],
            projecList: [],
			orgList: [],
            projectNumber:'',
            mechanismCode:'',
            takeUpTime:'3',
		}
	},
	methods:{


        // 获取项目数据列表
        async getChart(){
            var option = {
                title: {
                    left:'center',
                    text: '接口调用时长统计图'
                },
                color: ['#3398DB'],
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis : [
                    {
                        type : 'category',
                        data : [],
                        axisTick: {
                            alignWithLabel: true
                        },
                        axisLabel: {
                            interval:0,
                            rotate:-40
                        }
                    }
                ],
                yAxis : [
                    {   name : '数量(次数)',
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'直接访问',
                        type:'bar',
                        barWidth: '60%',
                        data:[]
                    }
                ]
            }


			this.loading = true;
			let data = {
                projectNumber:this.projectNumber,
                mechanismCode:this.mechanismCode,
                requestStartTime:this.i_data[0],
                requestEndTime:this.i_data[1],
                takeUpTime:this.takeUpTime*1000,
            }

			let res = await http.post(api.getMethodStatistics, data);
			// console.log(res)

			if(res.data.code =='1'){
                this.loading = false
                this.list = res.data.data
                console.log(this.list)

			}else{
                this.loading = false
                this.$message.error(res.data.msg)
			}
            // 组装名字
            for (const key in this.list) {
                option.xAxis[0].data.push(this.list[key].methodId)
            }
            //组装数据
            for (const key in this.list) {
                option.series[0].data.push(
                    this.list[key].takeUpTime
                )
            }


            // console.log(this.list)
            var myChart = this.$echarts.init(document.getElementById('tree1'));
            myChart.setOption(option)
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
        this.getChart()
        this.getProjecList()
        this.getOrgList()
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.interfaceTimeStatistics{
        display: flex;
        flex-direction: column;
        height: calc(100% - 155px);
        .search{
            padding: 20px;
            display: flex;
            justify-content:flex-start;
            align-items: center;
            flex-wrap: wrap;
            .li{
                margin-right: 20px;
                margin-bottom: 10px;
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 13px;
                color: #999;
                .li_1{
                    width: 120px;
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
            }


        }
        .d1{
            margin: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

    }
    #tree1{
        flex: 1;
    }
</style>
