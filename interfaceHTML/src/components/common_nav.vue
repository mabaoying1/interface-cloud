<template>
 <div class="navMenu" >
  
  <label v-for="navMenu in navMenus" >
   
   <el-menu-item v-if="navMenu.childs==null&&navMenu.entity&&navMenu.entity.state==='ENABLE'"
		:key="navMenu.entity.id" :data="navMenu" :index="navMenu.entity.name" :route="navMenu.entity.value" 
	>
    <i :class="navMenu.entity.icon"></i>
    <span slot="title" @click="getItem(navMenu.entity)">{{navMenu.entity.alias}}</span>
   </el-menu-item>
  
   <el-submenu v-if="navMenu.childs&&navMenu.entity&&navMenu.entity.state==='ENABLE'"
		:key="navMenu.entity.id" :data="navMenu" :index="navMenu.entity.name">
    <template slot="title">
     <i :class="navMenu.entity.icon"></i>
     <span > {{navMenu.entity.alias}}</span>
    </template>
    <NavMenu :navMenus="navMenu.childs"></NavMenu>
   </el-submenu>
  </label>
 </div>
</template>
  
<script>
 export default {
	name: 'NavMenu',
	props: ['navMenus'],
		data() {
		return {
			isCollapse:true,
		}
		},
		methods: {
			getItem(p){
				console.log(this.$store.state.tabList) 
				this.$store.commit('changeTab',p)
				console.log(this.$store.state.tabList) 

			}
		},
		mounted(){
			console.log()
		}
	}

</script>
  
<style scoped>
  
</style>