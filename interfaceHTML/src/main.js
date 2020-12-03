// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Vuex from 'vuex'

Vue.config.productionTip = false
// vuex
import store from './vuex/store'
Vue.use(Vuex)
// 接口
//ajax
import axios from 'axios'
Vue.prototype.$http = axios;

// emement - UI

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);
// diy

//百度 echart
import echarts from 'echarts';
Vue.prototype.$echarts = echarts ;

// eachert X 轴文字过多 换行
import tool from './utils/tool';
Vue.use(tool)

// 全局过滤器
import * as filter from './filter/filter'
Object.keys(filter).forEach(key => {
  Vue.filter(key, filter[key])
})
// 文本编辑器
import VueQuillEditor from 'vue-quill-editor';
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
Vue.use(VueQuillEditor)

// 路由拦截器
router.beforeEach((to, from, next) => {
  console.log('路由拦截器，全局进入');
  if (to.meta.requireAuth){
    if(sessionStorage.getItem('token')){
      next()
    }else{
      next('/login')

    }
  }else{
      next()
  }

})

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
