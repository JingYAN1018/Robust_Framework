<template>
    <div class="wrap">
        <button type="button" @click="titleEvent" @mouseleave="isShow=false">
            {{title}}
            <i class="iconfont icon-xiala"></i>
        </button>
        <transition name="slide-fade">
            <ul v-show="isShow">
                <li 
                    v-for="(option,index) in dataOptions" 
                    :key="index" 
                    @click="itemEvent(option)"
                >
                    {{option.value}}
                </li>
            </ul>
        </transition>
    </div>
</template>

<script>
    import {ref} from 'vue'
    export default{
        name:'dropdown',
        props:{
            // 这个是从父组件接收得到的
            dataOptions:{
                type:Object,
                // 设置默认值
                // default:[{
                //     value:"目标数据集flowers",
                //     number:12000
                // }]
            },
            titleVal: String
        },
        setup(props,{emit}){
            // console.log(props.dataOptions)
            //下拉菜单的列表，使用父组件传递过来的值
            let optionsList = ref({})
            optionsList = props.dataOptions
            
            //显示和隐藏下拉菜单
            let isShow = ref(false)

            //按钮名称
            let title = ref(props.titleVal)

            //下拉菜单的事件对象
            // let methodObj ={
                //点击title，显示下拉菜单
                const titleEvent = ()=>{
                    isShow.value = isShow.value ? false: true;
                }
                //选中选项
                const itemEvent=(n)=>{
                    // console.log(n);
                    isShow.value = false;
                    title.value = n.value;
                    // console.log(title)
                    emit('itemEvent',n);
                }
                
                
            // }
            return{
                optionsList, isShow,title,titleEvent,itemEvent
                // ...methodObj,
            }
        }
    }
</script>

<style scoped>
*{
    margin: 0;
    padding: 0;
    list-style: none;
}

.wrap{
    width: auto;
    height: 100%;
    font-size: 0.1rem;
    margin-top: 1px;
}

.wrap button{
    width: 80%;
    height: 86%;
    text-align: center;
    background-color: #86BEF9;
    opacity: .9;
    cursor: pointer;
    -webkit-user-select: none;
    -moz-user-select: none;
    user-select: none;
    border-radius: 10px;
    border: 0;
    font-size: 0.1rem !important;
    margin: 0 0 0 3%;
    transition: background-color 0.25s ease-in;
}
.wrap button:hover{
    background-color:#4198f6;
    transition: background-color 0.35s ease-out;
}

.wrap ul{
    width: 8%;
    height: auto;
    margin: 0.1% 0.8%;
    position: absolute;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.16);
    z-index: 11;
}

.wrap ul li{
    width: 100%;
    height: 10%;
    line-height: 20px;
    text-align: center;
    background-color: #a0ccf9;
    color: #242424;
    cursor: pointer;
    user-select: none;
    border-radius: 6px;
    border: 1px solid #f4f4f4;
    /*边框合并*/
    margin:0 0 -1px -1px;
}

.wrap ul li:hover{
   background-color: #4198f6;
    z-index: 11;
}

/* ul的动画 */
.slide-fade-enter-active{
    transition: all 0.3s ease-out;
}
.slide-fade-leave-active{
    transition: all 1.5s cubic-bezier(1,0.5,0.5,1);
}
.slide-fade-enter-from,
.slide-fade-leave-to{
    transform: translateY(-10px);
    opacity: 0;
}


/* 下拉图标样式 */
@font-face {
  font-family: "iconfont"; /* Project id 3957748 */
  src: url('//at.alicdn.com/t/c/font_3957748_k1qnbxlizb.woff2?t=1679031417050') format('woff2'),
       url('//at.alicdn.com/t/c/font_3957748_k1qnbxlizb.woff?t=1679031417050') format('woff'),
       url('//at.alicdn.com/t/c/font_3957748_k1qnbxlizb.ttf?t=1679031417050') format('truetype');
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 20px;
  font-style: normal;
  vertical-align: sub;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  
}

.icon-xiala:before {
  content: "\e6b9";
}
</style>