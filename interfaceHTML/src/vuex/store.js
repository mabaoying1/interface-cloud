import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
// 首先声明一个状态 state
const state = {
  tabList:[]
}

// 然后给 actions 注册事件处理函数，当这个函数被触发时，将状态提交到 mutaions中处理
const actions = {

}
// 更新状态
const mutations = {
  changeTab(state,b){
    console.log(b)
    let list = state.tabList
    for (const key in list) {
      if(list[key].alias === b.alias){
        return
      }
    }
    state.tabList.push({'alias':b.alias,'value':b.value})
  },
  deleteTab(state,b){
    console.log(b)
    let list = state.tabList
    for (const key in list) {
      if(list[key].alias === b.alias){
        list.splice(key , 1);
        return
      }
    }
  }
}
// 获取状态信息
const getters = {
}

// 下面这个相当关键了，所有模块，记住是所有，注册才能使用
export default new Vuex.Store({
  state,
  mutations,
  actions,
  getters
})