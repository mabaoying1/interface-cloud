<template>
  <div class="welcome_index">
    <div class="l">
      <div class="d1">
        <div class="title">数据概览</div>
        <div class="ul">
          <div class="li b1">
            <div class="info">
              <p class="p1">{{info.projectTotal}}</p>
              <p class="p2">总项目数</p>
            </div>
            <div class="icon iconfont icon-buoumaotubiao25"></div>
          </div>
          <div class="li b2 ">
            <div class="info">
              <p class="p1">{{info.hospitalTotal}}</p>
              <p class="p2">医院总数</p>
            </div>
            <div class="icon iconfont icon-yiliaohangyedeICON-"></div>
          </div>
          <div class="li b3 ">
            <div class="info">
              <p class="p1">{{info.requestTotal}}</p>
              <p class="p2">接口调用总数</p>
            </div>
            <div class="icon iconfont icon-jiekou"></div>
          </div>
        </div>
      </div>
      <div class="d2">
        <input type="text" placeholder="请输入搜索项目" v-model="projectName">
        <div @click="serch()">
          <i class="icon iconfont icon-magnifier"></i>
        </div>
      </div>
      <div class="d3">
        <div class="li" v-for="item in info.projectDetails">
          <p class="p1">{{item.projectName}}</p>
          <div class="p2">{{item.projectIntroduction}}</div>
          <div class="p3">
            <span class="s1"> <span>服务</span> <span @click="getS(item.projectNumber,item.projectName)"
                                                    v-show="item.projectType!=2" class=""
                                                    :class="item.serviceRunTotal==item.serviceTotal ?'s3':'s4'">{{item.serviceRunTotal}}/{{item.serviceTotal}}</span> </span>
            <span class="s2"> <span>日志</span> <span @click="getL(item.projectNumber)" class=""
                                                    :class="item.logSuccessTotal==item.logTotal ?'s3':'s4'">{{item.logSuccessTotal}}/{{item.logTotal}}</span> </span>
          </div>
        </div>

      </div>
    </div>
    <div class="r">
      <div class="d1">
        <div id="e1"></div>
      </div>
      <div class="d2">
        <!-- <button @click="dialogFormVisible=true">6666</button> -->
        <div id='e2' @click="chart3()"></div>
      </div>

    </div>
    <!-- 弹出窗口 -->
    <el-dialog title="错误日志趋势" :visible.sync="dialogFormVisible" :width="form1Width">
      <div>
        <el-select v-model="projectNumber2" @change="chart3()" filterable placeholder="请选择" class="w_140" size="small ">
          <el-option
            :key="'全部'"
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
        <!-- <el-button  class="ps_but" size="small" @click="chart3()" >搜索</el-button> -->
      </div>
      <div id='e3'></div>
    </el-dialog>
  </div>
</template>
<script>
    import http from '../utils/http'
    import api from '../utils/api'
    import {setTimeout} from 'timers';

    export default {
        name: 'welcome_index',
        data() {
            return {
                // 加载框1
                loading: false,
                dialogFormVisible: false,
                formLabelWidth: '150px',
                formInputWitdh: '120px',
                form1Width: '80%',
                addOrUpdate: true,
                methodId: '',
                pageSize: 10,
                pageNum: 1,
                total: 0,
                currentPage: 1,//分页-初始页码
                projectNumber: '',
                projectNumber2: '',
                projectName: '',
                list: [],
                info: '',
                projecList: [],
            }
        },
        methods: {
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
            serch() {
                this.getlog()

            },

            // 获取项目数据列表
            async getlog() {

                this.loading = true;
                let data = {
                    projectName: this.projectName,
                };

                let res = await http.post(api.homePage, data);
                console.log(res)

                if (res.data.code == '1') {
                    this.loading = false
                    this.info = res.data.data

                } else {
                    this.loading = false
                }

            },
            //图表1
            async chart() {
                let data = {
                    projectNumber: this.projectNumber,
                };

                let res = await http.post(api.getMoneyInfo, data)
                console.log(res)
                let arr = res.data.data
                if (arr.length < 1) {
                    arr = [
                        {payName: 'null1', totalMoney: 0},
                        {payName: 'null2', totalMoney: 0},
                        {payName: 'null3', totalMoney: 0},
                    ]
                }
                var option = {
                    title: {
                        text: '通过接口流水金额',
                        x: 'left',
                        top: '5px',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        bottom: '5px',
                        data: [arr[0].payName, arr[1].payName, arr[2].payName]
                    },
                    series: [
                        {
                            name: '资金来源',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
                            labelLine: false,
                            label: [
                                {show: false,}
                            ],
                            data: [
                                {value: arr[0].totalMoney, name: arr[0].payName},
                                {value: arr[1].totalMoney, name: arr[1].payName},
                                {value: arr[2].totalMoney, name: arr[2].payName},
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };

                var myChart = this.$echarts.init(document.getElementById('e1'));
                myChart.setOption(option)
            },
            //图表2
            async chart2() {
                var option = {
                    title: {
                        text: '错误日志趋势',
                        top: '5px;',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'axis',
                        top: '5px;'
                    },

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
                        name: '数量(次数)',
                        type: 'value'
                    },
                    series: [
                        // data
                    ]
                };

                let data = {
                    projectNumber: this.projectNumber,
                };

                let res = await http.post(api.getErrorLogInfo, data)
                console.log(res)
                let arr = res.data.data, time = [], dataArr = []
                // 使用第一个数组的时间作为时间轴  -- 组装时间轴
                for (const key in arr[0].details) {
                    time.push(arr[0].details[key].day)
                }
                option.xAxis.data = time
                // 组装 数据
                for (const key1 in arr) {
                    option.series.push({
                        name: arr[key1].projectName,
                        type: 'line',
                        stack: '总量',
                        smooth: true,
                        data: []
                    })
                    for (const key2 in arr[key1].details) {
                        option.series[key1].data.push(arr[key1].details[key2].logTotal)
                    }

                }
                console.log(option)


                var myChart = this.$echarts.init(document.getElementById('e2'));
                myChart.setOption(option)

            },
            //
            async chart3() {
                this.dialogFormVisible = true
                let option = {
                    title: {
                        text: '错误日志趋势'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: {
                        // 时间
                        data: ''
                    },
                    yAxis: {
                        name: '数量(次数)',
                        splitLine: {
                            show: false
                        }
                    },
                    toolbox: {
                        left: 'center',
                        feature: {
                            dataZoom: {
                                yAxisIndex: 'none'
                            },
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    dataZoom: [{
                        // startValue: '2014-06-01'
                    }, {
                        type: 'inside'
                    }],
                    // visualMap: {
                    //     top: 10,
                    //     right: 10,
                    //     pieces: [{
                    //         gt: 0,
                    //         lte: 50,
                    //         color: '#096'
                    //     }, {
                    //         gt: 50,
                    //         lte: 100,
                    //         color: '#ffde33'
                    //     }, {
                    //         gt: 100,
                    //         lte: 150,
                    //         color: '#ff9933'
                    //     }, {
                    //         gt: 150,
                    //         lte: 200,
                    //         color: '#cc0033'
                    //     }, {
                    //         gt: 200,
                    //         lte: 300,
                    //         color: '#660099'
                    //     }, {
                    //         gt: 300,
                    //         color: '#7e0023'
                    //     }],
                    //     outOfRange: {
                    //         color: '#999'
                    //     }
                    // },
                    series: [
                        // {
                        // name: '',
                        // type: 'line',
                        // data: [],
                        // markLine: {
                        //     silent: true,
                        //     data: [{
                        //         yAxis: 50
                        //     }, {
                        //         yAxis: 100
                        //     }, {
                        //         yAxis: 150
                        //     }, {
                        //         yAxis: 200
                        //     }, {
                        //         yAxis: 300
                        //     }]
                        // }
                        // }
                    ]
                };
                let data = {
                    projectNumber: this.projectNumber2,
                };


                let res = await http.post(api.getErrorLogInfo, data)
                let arr = res.data.data, time = [], dataArr = []
                // 使用第一个数组的时间作为时间轴  -- 组装时间轴
                for (const key in arr[0].details) {
                    time.push(arr[0].details[key].day)
                }
                option.xAxis.data = time
                // 组装 数据
                for (const key1 in arr) {
                    option.series.push({
                            name: arr[key1].projectName,
                            type: 'line',
                            data: [],
                            markLine: {
                                silent: true,
                                data: [{
                                    yAxis: 50
                                }, {
                                    yAxis: 100
                                }, {
                                    yAxis: 150
                                }, {
                                    yAxis: 200
                                }, {
                                    yAxis: 300
                                }]
                            }


                        }
                    )
                    for (const key2 in arr[key1].details) {
                        option.series[key1].data.push(arr[key1].details[key2].logTotal)
                    }

                }
                console.log(option)
                // console.log()


                let myChart = this.$echarts.init(document.getElementById('e3'));
                myChart.setOption(option, true)
            },
            // 获取项目列表
            async getProjecList() {
                let data = {
                    projectType: ''
                };
                let res = await http.post(api.projecList, data)

                this.projecList = res.data.data

            },
            //
            getS(s, name) {
                this.jump('serviceTree')
                sessionStorage.setItem('projectNumber', s)
                sessionStorage.setItem('projectName', name)
            },
            getL(s) {
                this.jump('logInfo')
                sessionStorage.setItem('projectNumber', s)
            }


        },
        mounted() {
            this.getProjecList();
            this.getlog()
            this.chart()
            this.chart2()
        },
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
  .welcome_index {
    padding: 10px;
    height: calc(100% - 125px);
    box-sizing: border-box;
    background: #F2F2F2 !important;
    display: flex;
    justify-content: center;
    align-items: center;

    .l {
      flex: 1;
      height: 100%;
      margin-right: 10px;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;

      .d1 {
        height: 140px;
        background: #ffffff;
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-direction: column;
        padding: 14px;
        box-sizing: border-box;

        .title {

          font-size: 16px;
          color: #555555;
        }

        .ul {
          flex: 1;
          display: flex;
          justify-content: space-between;;
          align-items: center;

          .b1 {
            background: linear-gradient(to left, #4BC9FF, #3270F3)
          }

          .b2 {
            background: linear-gradient(to left, #FEAD7E, #FE8986)
          }

          .b3 {
            background: linear-gradient(to left, #63E98E, #2AC45D)
          }

          .li {
            color: #ffffff;
            box-sizing: border-box;
            padding: 0 15px;
            display: flex;
            justify-content: space-between;;
            align-items: center;
            width: 30%;
            height: 100%;
            border-radius: 10px;

            .info {
              flex: 1;

              .p1 {
                font-weight: 600;
                font-size: 24px;
              }

              .p2 {
                font-size: 14px;
              }
            }

            .icon {
              width: 27px;
              font-size: 25px;
            }
          }
        }

      }

      .d2 {
        margin: 10px 0;
        width: 100%;
        display: flex;
        height: 40px;
        justify-content: center;
        align-items: center;

        input {
          border: none;
          height: 100%;
          flex: 1;
          padding-left: 20px;
        }

        div {
          cursor: pointer;
          width: 60px;
          background: #DCDFE6;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        }
      }

      .d3 {
        flex: 1;
        // background: red;
        overflow-y: auto;
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;

        .li {
          width: calc(50% - 5px);
          background: #ffffff;
          margin-bottom: 5px;
          height: 140px;
          box-sizing: border-box;
          padding: 20px;
          display: flex;
          flex-direction: column;

          .p1 {
            color: #555555;
            font-size: 16px;
            font-weight: 600;
            height: 20px;
          }

          .p2 {
            margin-top: 10px;
            overflow-y: auto;
            flex: 1;
            color: #A9B0B9;
          }

          .p3 {
            height: 20px;
            font-weight: 600;
            color: #111111;
            display: flex;

            .s1 {
              cursor: pointer;
              display: flex;
              width: 50%;
            }

            .s2 {
              cursor: pointer;
              display: flex;
              width: 50%;
            }

            .s3 {
              padding: 0 5px;;
              margin-left: 5px;
              display: flex;
              justify-content: center;
              align-items: center;
              border-radius: 10px;
              background: #67C23A;
              width: auto;
              color: #ffffff;
            }

            .s4 {
              padding: 0 5px;;

              margin-left: 5px;
              display: flex;
              justify-content: center;
              align-items: center;
              border-radius: 10px;
              background: #F56C6C;
              width: auto;
              color: #ffffff;
            }
          }
        }
      }
    }

    .r {
      width: 400px;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;

      .d1 {
        width: 100%;
        height: 50%;
        background: #ffffff;
        margin-bottom: 10px;
      }

      .d2 {
        width: 100%;
        height: 50%;
        background: #ffffff;
      }
    }
  }

  #e1, #e2 {
    height: 100%;;
  }

  #e3 {
    height: 400px;
  }
</style>
