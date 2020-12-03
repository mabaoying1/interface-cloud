<template>
    <div v-loading="loading" class="serviceTree">
		<div id="tree1"></div>

    </div>
</template>
<script>
import http from '../utils/http'
import api from '../utils/api'
import { setTimeout } from 'timers';
export default {
	name: 'serviceTree',
	data () {
		return {
			// 加载框1
			loading:false,
            list:[],
            projectNumber:sessionStorage.getItem('projectNumber'),
            projectName:sessionStorage.getItem('projectName'),
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


        // 获取项目数据列表
        async getTree(){

			this.loading = true;
			let projectName =this.projectName;
			let data = {
                projectNumber:this.projectNumber
                // projectNumber:'1001'
            };

			let res = await http.post(api.getServiceTree, data);
			// console.log(res)

			if(res.data.code =='1'){
                this.loading = false
                this.list = res.data.data

                this.tree(projectName)

			}else{
				this.loading = false
			}

        },
        tree(projectName){


            console.log(projectName);


            let data = {
                "name": projectName,
                "children":[]
            }
            for (const key in this.list) {
                console.log(this.list[key])
                data.children.push(this.list[key])
            }

            console.log(321,data)

            let option = {
                tooltip: {
                    trigger: 'item',
                    triggerOn: 'mousemove'
                },
                series: [
                    {
                        type: 'tree',

                        data: [data],

                        top: '1%',
                        left: '20%',
                        bottom: '1%',
                        right: '20%',

                        symbolSize: 7,

                        label: {
                            normal: {
                                position: 'left',
                                verticalAlign: 'middle',
                                align: 'right',
                                fontSize: 9
                            }
                        },

                        leaves: {
                            label: {
                                normal: {
                                    position: 'right',
                                    verticalAlign: 'middle',
                                    align: 'left'
                                }
                            }
                        },

                        expandAndCollapse: true,
                        animationDuration: 550,
                        animationDurationUpdate: 750
                    }
                ]
            }
            // console.log(this.list)
            var myChart = this.$echarts.init(document.getElementById('tree1'));
            myChart.setOption(option)
        }


	},
	mounted(){
        this.getTree()
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
	.serviceTree{
		height: calc(100% - 155px);

    }
    #tree1{
        height: 100%;
    }
</style>
