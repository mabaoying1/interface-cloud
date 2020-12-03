<template>
  <el-container class="index1">

    <el-container class="wrap">
      <!-- 导航 -->
      <el-aside class="aside_left">
        <div class="nav_img">
          <img src="../assets/logo02.png" alt="logo" class="nav_img_img">
        </div>
        <el-menu
          default-active="0"
          class="el-menu-vertical-demo"
          @select="menuSelected"
          background-color="#1e1d1e"
          text-color="#fff"
          active-text-color="#24b393">
          <commonNav :navMenus="leftMenus"></commonNav>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <div class="Breadcrumb">
          <div class="ll">
            <i class="icon iconfont icon-zhankai"></i>
            <span class="span2">创业慧康西部大区接口服务平台</span>
          </div>
          <div class="rr">
            <i class="icon iconfont i1 icon-rili"></i>
            <i class="icon iconfont i1 icon-10"></i>
            <span class="span3">amdin</span>
            <i class="icon iconfont i2 icon-icon" @click="outLogin()"></i>

          </div>


        </div>
        <commonTab></commonTab>
        <transition name="slide-fade">
          <router-view class="view"></router-view>
        </transition>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
    import common_nav from "./common_nav.vue"
    import commonTab from "./common_tab.vue"
    import http from '../utils/http'
    import api from '../utils/api'

    export default {
        name: 'index1',
        components: {
            "commonNav": common_nav,
            "commonTab": commonTab
        },
        data() {
            return {

                leftMenus: [
                    {
                        "entity": {
                            "id": 31,
                            "parentMenuId": 1,
                            "name": "welcome_index",
                            "icon": "el-icon-message\r\n",
                            "alias": "首页",
                            "state": "ENABLE",
                            "sort": 0,
                            "value": "welcome_index",
                            "type": "LINK",
                            "discription": "用于系统管理的菜单",
                            "createUserId": 1
                        }
                    },
                    {

                        "entity": {
                            "id": 1,
                            "parentMenuId": 0,
                            "name": "systemManage",
                            "icon": "el-icon-message\r\n",
                            "alias": "系统配置",
                            "state": "ENABLE",
                            "sort": 0,
                            "value": null,
                            "type": "NONE",
                            "discription": "用于系统管理的菜单",
                            "createUserId": 1
                        },
                        "childs": [
                            {
                                "entity": {
                                    "id": 2,
                                    "parentMenuId": 1,
                                    "name": "getInterface",
                                    "icon": "el-icon-share",
                                    "alias": "接口管理",
                                    "state": "ENABLE",
                                    "sort": 3,
                                    "value": "getInterface",
                                    "type": "LINK",
                                    "discription": "接口管理",
                                    "createUserId": 1
                                },
                                "childs": null
                            },
                            {
                                "entity": {
                                    "id": 3,
                                    "parentMenuId": 1,
                                    "name": "organizationManagement",
                                    "icon": "el-icon-share",
                                    "alias": "机构管理",
                                    "state": "ENABLE",
                                    "sort": 3,
                                    "value": "organizationManagement",
                                    "type": "LINK",
                                    "discription": "机构管理",
                                    "createUserId": 1
                                },
                                "childs": null
                            },
                            {
                                "entity": {
                                    "id": 4,
                                    "parentMenuId": 1,
                                    "name": "projectManagement",
                                    "icon": "el-icon-loading",
                                    "alias": "项目管理",
                                    "state": "ENABLE",
                                    "sort": 0,
                                    "value": "projectManagement",
                                    "type": "LINK",
                                    "discription": "用于权限管理的菜单",
                                    "createUserId": 1
                                },
                                "childs": null
                            },

                            {
                                "entity": {
                                    "id": 5,
                                    "parentMenuId": 1,
                                    "name": "accountManagement",
                                    "icon": "el-icon-edit",
                                    "alias": "账号管理",
                                    "state": "ENABLE",
                                    "sort": 2,
                                    "value": "accountManagement",
                                    "type": "LINK",
                                    "discription": "用于菜单管理的菜单",
                                    "createUserId": 1
                                },
                                "childs": null
                            },


                        ]
                    },
                    {
                        "entity": {
                            "id": 11,
                            "parentMenuId": 0,
                            "name": "statisticalAnalysis",
                            "icon": "el-icon-message\r\n",
                            "alias": "统计分析",
                            "state": "ENABLE",
                            "sort": 0,
                            "value": null,
                            "type": "NONE",
                            "discription": "用于系统管理的菜单",
                            "createUserId": 11
                        },
                        "childs": [
                            {
                                "entity": {
                                    "id": 12,
                                    "parentMenuId": 11,
                                    "name": "log",
                                    "icon": "el-icon-mobile-phone\r\n",
                                    "alias": "日志查看",
                                    "state": "ENABLE",
                                    "sort": 3,
                                    "value": "log",
                                    "type": "LINK",
                                    "discription": "用于分组管理的菜单",
                                    "createUserId": 11
                                },
                                "childs": null
                            },
                            {
                                "entity": {
                                    "id": 13,
                                    "parentMenuId": 11,
                                    "name": "todayStatistics",
                                    "icon": "el-icon-mobile-phone\r\n",
                                    "alias": "24小时接口调用",
                                    "state": "ENABLE",
                                    "sort": 3,
                                    "value": "todayStatistics",
                                    "type": "LINK",
                                    "discription": "24xiashi",
                                    "createUserId": 11
                                },
                                "childs": null
                            },
                            {
                                "entity": {
                                    "id": 14,
                                    "parentMenuId": 11,
                                    "name": "interfaceTimeStatistics",
                                    "icon": "el-icon-mobile-phone\r\n",
                                    "alias": "接口调用时长",
                                    "state": "ENABLE",
                                    "sort": 3,
                                    "value": "interfaceTimeStatistics",
                                    "type": "LINK",
                                    "discription": "备注",
                                    "createUserId": 11
                                },
                                "childs": null
                            }
                        ]
                    },
                    {
                        "entity": {
                            "id": 21,
                            "parentMenuId": 0,
                            "name": "ServiceManage",
                            "icon": "el-icon-message\r\n",
                            "alias": "服务管理",
                            "state": "ENABLE",
                            "sort": 0,
                            "value": null,
                            "type": "NONE",
                            "discription": "用于系统管理的菜单",
                            "createUserId": 21
                        },
                        "childs": [
                            {
                                "entity": {
                                    "id": 22,
                                    "parentMenuId": 22,
                                    "name": "ServiceManagement",
                                    "icon": "el-icon-bell",
                                    "alias": "服务管理",
                                    "state": "ENABLE",
                                    "sort": 1,
                                    "value": "ServiceManagement",
                                    "type": "LINK",
                                    "discription": "用于角色管理的菜单",
                                    "createUserId": 22
                                },
                                "childs": null
                            },
                            {
                                "entity": {
                                    "id": 12,
                                    "parentMenuId": 22,
                                    "name": "serviceLook",
                                    "icon": "el-icon-mobile-phone\r\n",
                                    "alias": "服务查看",
                                    "state": "ENABLE",
                                    "sort": 3,
                                    "value": "log",
                                    "type": "LINK",
                                    "discription": "用于分组管理的菜单",
                                    "createUserId": 22
                                },
                                "childs": null
                            },

                        ]
                    },
                    // {
                    // 	"entity": {
                    // 	"id": 6,
                    // 	"parentMenuId": 0,
                    // 	"name": "userManage",
                    // 	"icon": "el-icon-news",
                    // 	"alias": "用户管理",
                    // 	"state": "ENABLE",
                    // 	"sort": 1,
                    // 	"value": null,
                    // 	"type": "NONE",
                    // 	"discription": "用于用户管理的菜单",
                    // 	"createUserId": 1
                    // 	},
                    // 	"childs": [
                    // 	{
                    // 		"entity": {
                    // 		"id": 7,
                    // 		"parentMenuId": 6,
                    // 		"name": "accountManage",
                    // 		"icon": "el-icon-phone-outline\r\n",
                    // 		"alias": "帐号管理",
                    // 		"state": "ENABLE",
                    // 		"sort": 0,
                    // 		"value": "",
                    // 		"type": "NONE",
                    // 		"discription": "用于帐号管理的菜单",
                    // 		"createUserId": 1
                    // 		},
                    // 		"childs": [
                    // 		{
                    // 			"entity": {
                    // 			"id": 14,
                    // 			"parentMenuId": 7,
                    // 			"name": "emailManage",
                    // 			"icon": "el-icon-sold-out\r\n",
                    // 			"alias": "邮箱管理",
                    // 			"state": "ENABLE",
                    // 			"sort": 0,
                    // 			"value": "/content/email",
                    // 			"type": "LINK",
                    // 			"discription": "用于邮箱管理的菜单",
                    // 			"createUserId": 1
                    // 			},
                    // 			"childs": null
                    // 		},
                    // 		{
                    // 			"entity": {
                    // 			"id": 13,
                    // 			"parentMenuId": 7,
                    // 			"name": "passManage",
                    // 			"icon": "el-icon-service\r\n",
                    // 			"alias": "密码管理",
                    // 			"state": "ENABLE",
                    // 			"sort": 1,
                    // 			"value": "/content/pass",
                    // 			"type": "LINK",
                    // 			"discription": "用于密码管理的菜单",
                    // 			"createUserId": 1
                    // 			},
                    // 			"childs": null
                    // 		}
                    // 		]
                    // 	},
                    // 	{
                    // 		"entity": {
                    // 		"id": 8,
                    // 		"parentMenuId": 6,
                    // 		"name": "integralManage",
                    // 		"icon": "el-icon-picture",
                    // 		"alias": "积分管理",
                    // 		"state": "ENABLE",
                    // 		"sort": 1,
                    // 		"value": "/user/integral",
                    // 		"type": "LINK",
                    // 		"discription": "用于积分管理的菜单",
                    // 		"createUserId": 1
                    // 		},
                    // 		"childs": null
                    // 	}
                    // 	]
                    // },
                    // {
                    // 	"entity": {
                    // 	"id": 9,
                    // 	"parentMenuId": 0,
                    // 	"name": "contentManage",
                    // 	"icon": "el-icon-rank",
                    // 	"alias": "内容管理",
                    // 	"state": "ENABLE",
                    // 	"sort": 2,
                    // 	"value": null,
                    // 	"type": "NONE",
                    // 	"discription": "用于内容管理的菜单",
                    // 	"createUserId": 1
                    // 	},
                    // 	"childs": [
                    // 	{
                    // 		"entity": {
                    // 		"id": 10,
                    // 		"parentMenuId": 9,
                    // 		"name": "classifyManage",
                    // 		"icon": "el-icon-printer",
                    // 		"alias": "分类管理",
                    // 		"state": "ENABLE",
                    // 		"sort": 0,
                    // 		"value": "/content/classify",
                    // 		"type": "LINK",
                    // 		"discription": "用于分类管理的菜单",
                    // 		"createUserId": 1
                    // 		},
                    // 		"childs": null
                    // 	},
                    // 	{
                    // 		"entity": {
                    // 		"id": 11,
                    // 		"parentMenuId": 9,
                    // 		"name": "articleManage",
                    // 		"icon": "el-icon-star-on",
                    // 		"alias": "文章管理",
                    // 		"state": "ENABLE",
                    // 		"sort": 1,
                    // 		"value": "/content/article",
                    // 		"type": "LINK",
                    // 		"discription": "用于文章管理的菜单",
                    // 		"createUserId": 1
                    // 		},
                    // 		"childs": null
                    // 	},
                    // 	{
                    // 		"entity": {
                    // 		"id": 12,
                    // 		"parentMenuId": 9,
                    // 		"name": "commentManage",
                    // 		"icon": "el-icon-share",
                    // 		"alias": "评论管理",
                    // 		"state": "ENABLE",
                    // 		"sort": 2,
                    // 		"value": "/content/comment",
                    // 		"type": "LINK",
                    // 		"discription": "用于评论管理的菜单",
                    // 		"createUserId": 1
                    // 		},
                    // 		"childs": null
                    // 	}
                    // 	]
                    // }
                ]

            }
        },
        methods: {
            menuSelected(a) {
                console.log(a)
                this.jump(a)
            },
            outLogin() {
                sessionStorage.clear()
                this.jump('login')
            },
            // 获取字典
            getDictionaries() {
                this.P()
                this.O()
                this.M()
                this.P2()
            },
            // 项目
            async P() {
                let data = {
                    projectType:''
                };
                let res = await http.post(api.projecList, data)
                sessionStorage.setItem("projecList", JSON.stringify(res.data.data))
                JSON.parse(sessionStorage.getItem('projecList'))
                console.log(JSON.parse(sessionStorage.getItem('projecList')))
            },            // 项目
            async P2() {
                let data = {
                    projectType:'1'
                };
                let res = await http.post(api.projecList, data)
                sessionStorage.setItem("projectListBz", JSON.stringify(res.data.data))
            },
            // 机构
            async O() {
                let data = ''
                let res = await http.post(api.OrgList, data)
                this.orgList = res.data.data
                sessionStorage.setItem("orgList", JSON.stringify(res.data.data))
                JSON.parse(sessionStorage.getItem('orgList'))
                console.log(JSON.parse(sessionStorage.getItem('orgList')))
            },
            // 方法名称

            async M() {
                let data = ''
                let res = await http.post(api.methodList, data)
                this.orgList = res.data.data
                sessionStorage.setItem("methodList", JSON.stringify(res.data.data))
                JSON.parse(sessionStorage.getItem('methodList'))
                console.log(JSON.parse(sessionStorage.getItem('methodList')))
            }
        },
        created() {
            this.getDictionaries()
        },
        mounted() {

        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
  @import '../sass/index.scss';
</style>


