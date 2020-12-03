<template>
  <div class="login">
    <el-container class="conttainer">
      <el-header>
        <div class="logo">
          <img class src="../assets/bsoft.png" alt="创业软件">
          <!-- <img class="" src="http://10.254.8.7:8084/BsoftPayPlat/static/img/bsoft.png" alt="创业软件"> -->
        </div>
      </el-header>
      <el-container class="wrap">
        <el-main class="main">
          <img class="img1" src="../assets/img_01.png" alt>
          <!-- <img class="img1" src="http://10.254.8.7:8084/BsoftPayPlat/static/img/img0.png" alt=""> -->

          <div class="dl" v-loading="loading">
            <div class="d1 c1">
              <span>接口管理系统</span>
            </div>
            <div class="d1 c2">
              <i class="icon iconfont icon-zhanghu"></i>
              <input type="text"    placeholder="请输入用户名" ref="userName"   v-model="id" >
            </div>
            <div class="d1 c3">
              <i class="icon iconfont icon-mima"></i>
              <input type="password"    placeholder="请输入密码" ref="pw"  v-model="pass" @keyup.enter="login()">
            </div>
            <div class="d1 c5">
              <button @click="login()">登录</button>
            </div>


          </div>
        </el-main>
      </el-container>
      <p class="copyright">copyright@2017创业软件股份有限公司</p>

      <img class="img2" src="../assets/img_bj.png" alt>
      <!-- <img class="img2" src="http://10.254.8.7:8084/BsoftPayPlat/static/img/building.png" alt=""> -->
    </el-container>

  </div>
</template>

<script>
import http from "../utils/http";
import api from "../utils/api";
export default {
	name: "login",
	data() {
		return {
			loading: false,

		};
	},
	methods: {
		async login() {
        console.log(this.id+'ces'+this.pass)
		   if(this.id=='' || this.id==null){
           return alert("请输入用户名！");
       }
		   if(this.pass=='' || this.pass==null){
            return alert("请输入密码！");
        }
			this.loading = true;
			let data = {
				account: this.id,
   				password: this.pass,
			};
			let res = await http.post(api.login, data);
			console.log(res)

			if(res.data.code =='1'){
				sessionStorage.setItem('token', res.data.data.token)
				sessionStorage.setItem('refreshToken', res.data.data.refreshToken)
				this.loading = false
				this.jump('welcome_index')

			}else{
			    alert(res.data.msg);
				this.loading = false
			}
		}
	},
	mounted(){
		console.log()
	}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
@import "../sass/login.scss";
</style>
