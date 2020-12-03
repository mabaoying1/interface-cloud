<template>
    <div v-loading="loading" class="todayStatistics">
        <div class="head">
			<div class="title">24小时接口调用 </div>
			<div class="search">
				<div class="li">
                    <div>项目名称：</div>

                    <el-select v-model="projectNumber" @change="getChart()" filterable placeholder="请选择" class="w_220"  size="small ">
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
					<!-- <el-button  class="ps_but" size="small" @click="getChart()" >搜索</el-button> -->

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
	name: 'todayStatistics',
	data () {
		return {
			// 加载框1
			loading:false,
            list:[],
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
            Project:[],
            projectNumber:'',
            projecList:JSON.parse(sessionStorage.getItem('projecList')),
		}
	},
	methods:{


        // 获取项图表
        async getChart(){

			this.loading = true;
			let data = {
                projectNumber:this.projectNumber
                // projectNumber:'1001'
            };

			let res = await http.post(api.getHoursStatistics, data);
			// console.log(res)

			if(res.data.code =='1'){
                this.loading = false
                this.list = res.data.data
                console.log(this.list)

			}else{
				this.loading = false
			}


            var option = {
                title: {
                    text: '24小时接口调用折线图堆叠',
                    top:'5px;',
                    left:'center'
                },
                tooltip: {
                    trigger: 'axis',
                    top:'5px;'
                },
                // legend: {
                    // data:['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
                // },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: [],//x 轴
                },
                yAxis: {
                    type: 'value',
                    name : '数量(次数)',
                },
                series: [
                    // data
                ]
            };
            let arr = res.data.data ,time=[], dataArr=[]
            // 使用第一个数组的时间作为时间轴  -- 组装时间轴
            for (const key in arr[0].details) {
                time.push(arr[0].details[key].hour)
            }
            option.xAxis.data = time
            // 组装 数据
            for (const key1 in arr) {
                option.series.push({
                    name:arr[key1].projectName,
                    type:'line',
                    stack: '总量',
                    smooth:true,
                    data:[]
                })
                for (const key2 in arr[key1].details) {
                    option.series[key1].data.push(arr[key1].details[key2].logTotal)
                }

            }

            // console.log(this.list)
            var myChart = this.$echarts.init(document.getElementById('tree1'));
            myChart.setOption(option,true)
        },



	},
	mounted(){
        this.getChart()
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.todayStatistics{
		height: calc(100% - 225px);
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
        #tree1{
            // max-height: 100%;
            // min-height: 400px;
            height:calc(100% - 95px);

        }

    }

</style>
