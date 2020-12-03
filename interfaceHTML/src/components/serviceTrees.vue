<template>
  <div v-loading="loading" class="ServiceManagement">
    <div class="head">
      <div class="title">服务情况</div>
      <div class="search">
        <div class="li">
          <div>项目名称：</div>
          <el-select v-model="projectNumber" placeholder="请选择" class="w_140" size="small ">
            <el-option
              v-for="item in projecList"
              :key="item.projectNumber"
              :label="item.projectName"
              :value="item.projectNumber">
            </el-option>
          </el-select>
        </div>

        <div class="li">
          <div>机构名称：</div>
          <el-select v-model="mechanismCode" placeholder="请选择" class="w_140" size="small ">
            <el-option
              v-for="item in orgList"
              :key="item.mechanismCode"
              :label="item.mechanismName"
              :value="item.mechanismCode">
            </el-option>
          </el-select>
        </div>

        <div class="li">
          <el-button class="ps_but" size="small" @click="getServiceManagement()">搜索</el-button>
        </div>

      </div>
    </div>
    <div class="body">
      <!-- table -->
      <el-table
        :data="list"
        :span-method="arraySpanMethod"
        :row-class-name="tableHeaderColor"
        border
        size='mini'
        row-style="height:60px;"
        style="width: 100%">

        <el-table-column
          align="center"
          prop="projectNumber"
          label="项目"
          width="">
          <template slot-scope="scope">
            <img style="width: 20px;height: 20px" src="../assets/project.png"/>
            <br>
            <span> {{scope.row.projectNumber|p}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="mechanismCode"
          label="机构"
          width="">
          <template slot-scope="scopes">
            <img style="width: 20px;height: 20px" src="../assets/org.png"/>
            <br>
            <span>{{scopes.row.mechanismCode|Org}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="left"
          prop="serviceName"
          label="功能"
          width="">
          <template slot-scope="scope">
            <span>
              <img v-if="scope.row.serviceName=='住院缴费' || scope.row.serviceName.indexOf('住院') != -1 " style="width: 20px;height: 20px" src="../assets/zyjf.png"/>
              <img v-if="scope.row.serviceName=='门诊收费' || scope.row.serviceName.indexOf('门诊') != -1 " style="width: 20px;height: 20px" src="../assets/mzsf.png"/>
              <img v-if="scope.row.serviceName=='诊间支付' || scope.row.serviceName.indexOf('诊间') != -1 " style="width: 20px;height: 20px" src="../assets/zjzf.png"/>
              <img v-if="scope.row.serviceName=='预约挂号' || scope.row.serviceName.indexOf('挂号') != -1 " style="width: 20px;height: 20px" src="../assets/yygh.png"/>
              <img v-if="scope.row.serviceName=='报告查询' || scope.row.serviceName.indexOf('报告') != -1 " style="width: 20px;height: 20px" src="../assets/bgcx.png"/>
              <img
                v-if="  scope.row.serviceName.indexOf('挂号') == -1 && scope.row.serviceName.indexOf('报告') == -1 &&
                scope.row.serviceName.indexOf('住院') == -1 && scope.row.serviceName.indexOf('门诊') == -1 &&scope.row.serviceName.indexOf('诊间') == -1 &&
                scope.row.serviceName!='住院缴费'&&scope.row.serviceName!='诊间支付'&&scope.row.serviceName!='门诊收费'&&scope.row.serviceName!='预约挂号'&&scope.row.serviceName!='报告查询'"
                style="width: 20px;height: 20px" src="../assets/qtlx.png"/>
              {{scope.row.serviceName}}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          align="left"
          prop="ipAddress"
          label="IP"
          width="">
          <template slot-scope="scope">
            <span>
              <img style="width: 30px;height: 30px" src="../assets/ipaddr.png"/>
              {{scope.row.ipAddress}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="port"
          label="端口"
          width="80%">
          <template slot-scope="scope">
            <span>
              <img style="width:20px;height: 20px" src="../assets/port.png"/>
              {{scope.row.port}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="port"
          :show-overflow-tooltip="true"
          label="服务器配置情况"
          width="300%">
          <template slot-scope="scope">
            <span>
              {{scope.row.serverInfo}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="port"
          label="日志"
          width="80%">
          <template slot-scope="scope">
            <a size="small" style="cursor:pointer" :class="scope.row.logError==0 ?'s3':'s4'"
                  @click="getL(scope.row.projectNumber,scope.row.mechanismCode,scope.row.serviceID,scope.row.ipAddress,scope.row.port,-1)"> {{scope.row.logError}}</a>/
            <a size="small" style="cursor:pointer"
                  @click="getL(scope.row.projectNumber,scope.row.mechanismCode,scope.row.serviceID,scope.row.ipAddress,scope.row.port,'')">  {{scope.row.logTotal}}</a>

          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="status"
          label="状态"
          width="80%">
          <template slot-scope="scope">
            <img v-if="scope.row.status!=1" style="width: 30px;" src="../assets/error.png"/>
            <img v-if="scope.row.status==1" style="width: 30px;" src="../assets/right.png"/>
          </template>

        </el-table-column>
      </el-table>
      <!-- 分页 参数：total 总条目数 ;currentPage 当前页码-->
      <!--      <el-pagination-->
      <!--        @size-change="handleSizeChange"-->
      <!--        @current-change="handleCurrentChange"-->
      <!--        :current-page="currentPage"-->
      <!--        :page-sizes="[10, 20, 30, 40]"-->
      <!--        :page-size="pageSize"-->
      <!--        layout="total, sizes, prev, pager, next, jumper"-->
      <!--        :total="total">-->
      <!--      </el-pagination>-->
    </div>

  </div>
</template>
<script>
    import http from '../utils/http'
    import api from '../utils/api'
    import {setTimeout} from 'timers';

    export default {
        name: 'ServiceManagement',
        data() {
            return {
                // 加载框1
                loading: false,
                // 加载框2
                loading2: false,
                // 对话框
                modal1: false,
                formLabelWidth: '150px',
                formInputWitdh: '120px',
                form1Width: '80%',
                addOrUpdate: true,
                serviceName: "",
                projectNumber: '',
                mechanismCode: '',
                pageSize: 10,
                pageNum: 1,
                total: 0,
                currentPage: 1,//分页-初始页码
                list: [],
                value1: '',
                value2: '',
                projecList: [],
                orgList: [],
                mergeSpanArr: [],
                projectNumberList: [],
                mechanismNameArr: [],
                serviceNameArr: [],
                addServiceManagementInfo: {
                    serviceID: '',//服务id
                    serviceName: '',//服务名称
                    ipAddress: '',//IP地址
                    port: '',//端口
                    projectNumber: '',//项目编号
                    mechanismCode: '',//服务机构码
                    logTotal: '',//总日志数
                    logError: '',//成功日志数
                    serverInfo: '',//服务器配置情况


                }
            }
        },
        methods: {
            // 操作多行合并
            arraySpanMethod({row, column, rowIndex, columnIndex}) {
                if (columnIndex === 0) {
                    if (row.projectNumber === 0) {
                        return {
                            rowspan: 2,
                            colspan: 1
                        }
                    } else {
                        const _row = this.mergeSpanArr[rowIndex]
                        const _col = _row > 0 ? 1 : 0
                        return {
                            rowspan: _row,
                            colspan: _col
                        }
                    }
                }
                ;
                if (columnIndex === 1) {
                    if (row.mechanismName === 0) {
                        return {
                            rowspan: 2,
                            colspan: 1
                        }
                    } else {
                        const _row = this.mechanismNameArr[rowIndex]
                        const _col = _row > 0 ? 1 : 0
                        return {
                            rowspan: _row,
                            colspan: _col
                        }
                    }
                }
                ;
                if (columnIndex === 2) {
                    if (row.serviceName === 0) {
                        return {
                            rowspan: 2,
                            colspan: 1
                        }
                    } else {
                        const _row = this.serviceNameArr[rowIndex]
                        const _col = _row > 0 ? 1 : 0
                        return {
                            rowspan: _row,
                            colspan: _col
                        }
                    }
                }
                ;
            },

            // 修改table header的背景色
            tableHeaderColor({row, rowIndex}) {
                const listLength = this.projectNumberList.length;
                const arr = [];
                for (var a = 0; a < listLength; a++) {
                    if (a%2 ===0) {
                        arr.push(this.projectNumberList[a])
                    }
                }
                if(arr.indexOf(row.projectNumber)!=-1){
                    console.log(row.projectNumber);
                    return 'warning-row';
                }else{
                    return 'success-row';
                }
                return '';
            },


            // 生成一个与行数相同的数组，记录每一行设置的合并数
            setMergeArr(data) {
                this.projectNumberList = [];
                this.mergeSpanArr = [];
                this.serviceNameArr = [];
                this.mechanismNameArr = [];

                for (var i = 0; i < data.length; i++) {
                    if (i === 0) {
                        this.mergeSpanArr.push(1);
                        this.mergeSpanArrIndex = 0
                    } else {
                        // 判断当前元素与上一个元素是否相同
                        if (data[i].projectNumber === data[i - 1].projectNumber) {
                            this.mergeSpanArr[this.mergeSpanArrIndex] += 1;
                            this.mergeSpanArr.push(0);
                        } else {
                            this.projectNumberList.push(data[i].projectNumber);
                            this.mergeSpanArr.push(1);
                            this.mergeSpanArrIndex = i;
                        }
                    }
                }
                ;
                // 如果第一条记录索引为0，向数组中push 1，设置索引值
                // 如果不是第一条记录，判断与前一条记录是否相等，相等则向mergeSpanArr添加元素0
                // 且将前一条记录+1，即需要合并的行数+1，直到得到所有需要合并的行数
                for (var i = 0; i < data.length; i++) {
                    if (i === 0) {
                        this.mechanismNameArr.push(1);
                        this.mergeSpanArrIndex = 0
                    } else {
                        // 判断当前元素与上一个元素是否相同
                        if (data[i].projectNumber === data[i - 1].projectNumber && data[i].mechanismCode === data[i - 1].mechanismCode) {
                            this.mechanismNameArr[this.mergeSpanArrIndex] += 1;
                            this.mechanismNameArr.push(0);
                        } else {
                            this.mechanismNameArr.push(1);
                            this.mergeSpanArrIndex = i;
                        }
                    }
                }
                ;
                for (var a = 0; a < data.length; a++) {
                    if (a === 0) {
                        this.serviceNameArr.push(1);
                        this.mergeSpanArrIndex = 0
                    } else {

                        // 判断当前元素与上一个元素是否相同
                        if (data[a].serviceName === data[a - 1].serviceName && data[a].projectNumber === data[a - 1].projectNumber && data[a].mechanismCode === data[a - 1].mechanismCode) {
                            this.serviceNameArr[this.mergeSpanArrIndex] += 1;
                            this.serviceNameArr.push(0);
                        } else {
                            this.serviceNameArr.push(1);
                            this.mergeSpanArrIndex = a;
                        }
                    }
                }
                ;

            },


            // 获取服务数据列表
            async getServiceManagement() {

                this.loading = true;
                let data = {
                    projectNumber: this.projectNumber,
                    mechanismCode: this.mechanismCode

                };
                let res = await http.post(api.getQueryService, data);


                if (res.data.code == '1') {
                    this.loading = false
                    this.list = res.data.data
                    this.total = res.data.data.length
                    this.setMergeArr(res.data.data)

                } else {
                    this.loading = false
                }

            },
            //新增服务
            async addServiceManagement() {

                this.loading2 = true;
                let data = this.addServiceManagementInfo
                let res = await http.post(api.addServiceManagement, data);

                if (res.data.code == '1') {
                    this.loading2 = false
                    this.modal1 = false

                    this.$message({
                        type: 'success',
                        message: `${res.data.msg}`
                    })
                    this.getServiceManagement()

                } else {
                    this.loading2 = false;
                    this.$message.error(res.data.msg)
                }

            },

            // 获取项目列表
            async getProjecList() {
                let data = {
                    projectType: ''
                };
                let res = await http.post(api.projecList, data)

                this.projecList = res.data.data

            },
            // 获取机构列表
            async getOrgList() {
                let data = ''
                let res = await http.post(api.OrgList, data)

                this.orgList = res.data.data

            },
            async getL(xm, jg, sid, ip, port, zt) {
                this.jump('logInfoPort')
                sessionStorage.setItem('projectNumber', xm)
                sessionStorage.setItem('mechanismCode', jg)
                sessionStorage.setItem('serviceID', sid)
                sessionStorage.setItem('ipAddress', ip)
                sessionStorage.setItem('port', port)
                sessionStorage.setItem('status', zt)
            }
        },


        mounted() {
            this.getServiceManagement()
            this.getProjecList()
            this.getOrgList()
        },
        filters: {
            P: function (nuber) {
                let p = JSON.parse(sessionStorage.getItem('projecList'))
                for (const key in p) {
                    if (nuber == p[key].projectNumber) {
                        return p[key].projectName
                    }
                }

            },
            Org: function (nuber) {
                let p = JSON.parse(sessionStorage.getItem('orgList'))
                for (const key in p) {
                    if (nuber == p[key].mechanismCode) {
                        return p[key].mechanismName
                    }
                }

            }
        },

    }


</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
  .ServiceManagement {
    padding: 20px;

    .head {
      margin-bottom: 25px;

      .title {
        font-size: 16px;
        color: #555;
        margin-bottom: 20px;
      }

      .search {
        display: flex;
        justify-content: flex-start;
        align-items: center;

        .li {
          margin-right: 20px;
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 13px;
          color: #999;

          .li_1 {
            width: 85px;
          }
        }
      }
    }

    .ps_but:hover {
      color: #24B393;
      border-color: #24B393;
    }

    .ps_but:focus {
      color: #fff !important;
      border-color: #24B393;
      background-color: #24B393
    }

    .el-button--text {
      color: #24B393;

    }

    .el-button:focus, .el-button:hover {
      color: #24B393;
    }

    .el-pagination {
      margin-top: 10px;
    }

    .ps_but {
      font-size: 13px !important;
      color: #24B393;
      border-color: #24B393;
    }

    .ps_but2 {
      font-size: 16px;
      margin: 0 2px;
      color: #24B393;
      cursor: pointer;
    }

    .errors {
      background-image: url(../assets/error.png)
    }

    .rights {
      background-image: url(../assets/right.png)
    }
  }

  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
  .s3{
    color: green;
  }
  .s4{
    color: red;
  }
</style>
