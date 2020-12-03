<template>
  <div class="commonHeader">
      <div class="l">
          <img src="../assets/logo_jh.png" alt="">
      </div>
      <div class="r"><span style="font-size:14px;color:white;">某某某</span>
        
        <span style="font-size:14px;color:white;"> 管理员</span>
        <el-button @click.native.prevent="loginOut(userId)" type="primary" size="small">
          注销
        </el-button>
      </div>
  </div>
</template>

<script>
import http from '../utils/http'
import api from '../utils/api'
export default {
  name: 'commonHeader',
  data () {
    return {

      msg: 'Welcome to Your commonHeader'
    }
  },
  methods:{
    loginOut(){}
  },
  mounted(){
    
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
    .commonHeader{
        margin: 0 30px ;
        height: 100%;
        // width: 100%;
        display: flex;
        justify-content:space-between;
        align-items: center;
    }
</style>
