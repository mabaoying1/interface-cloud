import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login'//登录
import index1 from '@/components/index'//首页

import welcome_index from '@/components/welcome_index'//欢迎页面
import logInfo from '@/components/logInfo'//欢迎页面-日志查询
import logInfoPort from '@/components/logInfoPort'//欢迎页面-日志查询
import serviceTree from '@/components/serviceTree'//欢迎页面-服务情况树
import todayStatistics from '@/components/todayStatistics'//24小时接口调用量统计
import interfaceTimeStatistics from '@/components/interfaceTimeStatistics'//接口调用时长统计



import index_index from '@/components/index_index'//index demo

import getInterface from '@/components/getInterface'//接口管理
import projectManagement from '@/components/projectManagement'//项目管理
import accountManagement from '@/components/accountManagement'//平台账号管理
import ServiceManagement from '@/components/ServiceManagement'//服务配置信息管理
import organizationManagement from '@/components/organizationManagement'//机构管理
import serviceLook  from '@/components/serviceTrees'//欢迎页面-服务情况树
import log from '@/components/log'//日志查询




Vue.use(Router)

export default new Router({

	routes: [
		{
			path: '/login',
			name: 'login',
			component: login,
			meta:{
				requireAuth: false
			}
		},{
			path: '/index',
			name: 'index1',
			component: index1,
			meta: {
				'aaa':'bbb',
				requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
			},
			children:[
				{path:'/index/',component:index_index,meta: { requireAuth: true }},
				{path:'/welcome_index',component:welcome_index,meta: { requireAuth: true }},
				{path:'/serviceTree',component:serviceTree,meta: { requireAuth: true }},
				{path:'/logInfo',component:logInfo,meta: { requireAuth: true }},
				{path:'/logInfoPort',component:logInfoPort,meta: { requireAuth: true }},
				{path:'/getInterface',component:getInterface,meta: { requireAuth: true }},
				{path:'/projectManagement',component:projectManagement,meta: { requireAuth: true }},
				{path:'/accountManagement',component:accountManagement,meta: { requireAuth: true }},
				{path:'/ServiceManagement',component:ServiceManagement,meta: { requireAuth: true }},
				{path:'/log',component:log,meta: { requireAuth: true }},
				{path:'/organizationManagement',component:organizationManagement,meta: { requireAuth: true }},
				{path:'/todayStatistics',component:todayStatistics,meta: { requireAuth: true }},
				{path:'/interfaceTimeStatistics',component:interfaceTimeStatistics,meta: { requireAuth: true }},
				{path:'/serviceLook',component:serviceLook,meta: { requireAuth: true }},


			]
		}
	]

})
