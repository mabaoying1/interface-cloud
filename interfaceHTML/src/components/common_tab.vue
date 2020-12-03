<template>
    <div class="warp">
      <div @click="getSrollLeft()">
          <i class="icon iconfont i1 icon-zuoshuangjiantou"></i>
      </div>
        <div class="commonTab" id="warp">
            <div class="li">
                 <span @click="getPath({value:'welcome_index'})">欢迎页面</span>
                </i>
            </div>
            <div  class="li" v-for="(item,index) in this.$store.state.tabList">
                <span @click="getPath(item)">{{item.alias}}</span>
                <i class="icon iconfont icon-fork" @click="deleteItem(item)"></i>
            </div>
        </div>
      <div @click="getSrollRight()">
          <i class="icon iconfont i2 icon-youshuangjiantou"></i>
      </div>

    </div>
  
</template>

<script>
import http from '../utils/http'
import api from '../utils/api'
import { debug } from 'util';
export default {
	name: 'commonTab',
	data () {
		return {
            msg: 'Welcome to Your commonTab',
            srcollL:0
		}
	},
	methods:{
		deleteItem(item){
			this.$store.commit('deleteTab',item)
		},
		getPath(item){
            // alert(item.value)
            console.log(item)
            this.jump(item.value)
        },
        getSrollLeft(){
            let r = document.getElementById('warp')
            
            if(this.srcollL>0){
                this.srcollL = this.srcollL-100
                r.scrollTo(this.srcollL,0)
                console.log(this.srcollL)
            }
            
        },
        getSrollRight(){
            let r = document.getElementById('warp')
            
            if(this.srcollL<(r.scrollWidth-r.offsetWidth)){
                this.srcollL = this.srcollL+100
                r.scrollTo(this.srcollL,0)
                console.log(this.srcollL)
            }
            

        }
	},
	mounted(){
		
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
    .warp{
        padding: 0 10px;
        background: #fff;
        justify-content: center;
        align-items: center;
        display: flex;
        .i1,.i2{
            color: #24B393;
        }
        .i1{
            margin-right: 20px;
        }
        .i2{
            margin-left: 20px;
        }
        .commonTab{
            // padding-left: 20px;
            height: 40px;
            // flex: 1;
            width: 100%;
            display: flex;
            align-items: center;
            color: #8e8e8e;
            overflow-x:hidden;
            // overflow-x:auto;
            overflow-y:hidden;
            
            .li{
                border: 1px solid #eee;
                height: 100%;
                min-width: 85px;;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 0 10px;
                color: #606266;
                font-size: 14px;
                span{
                    cursor: pointer;

                }
                i{
                    height: 100%;
                    margin-left: 10px;
                    line-height: 25px;
                    cursor: pointer;
                    font-size: 11px;
                    color: #8e8e8e;
                }
            }
        }
    }
    
</style>
