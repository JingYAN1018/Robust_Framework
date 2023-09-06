<template>
    <div class="o_wrap">
        <!--当复选框不可用时，用一个按钮覆盖在内部div盒子上，因为按钮有v-show功能 -->
        <!-- <input type="button" class="o_input" v-show="isDisabled"> -->
        <div 
            class="o_checkbox" 
            @click="checkboxBtn">
            <i :class="['o_icon iconfont icon-duihao',isTouch ===1 ? 'o_icon_in' : 'o_icon_out']" ></i>
            <span class="o_span" :class=" isTouch===1 ? 'o_span_in':'o_span_out'" >{{spantitle}}</span>
        </div>
    </div>
    
</template>

<script>
    import {ref,watch} from 'vue'
    export default{
        name:'checkbox',
        props:{
            spantitle:String,
            botShow:Boolean,
            allShow:Boolean
        },
        setup(props,{emit}){
            //禁用开关,默认值false
            let isDisabled = ref(false)
            //颜色开关
            let isTouch = ref(0)
            //整个的时间对象
            // let  methodObj = {
                //点击事件
                const checkboxBtn=()=>{
                    if(isTouch.value === 0){
                        // console.log("选中，开关开启")
                        isTouch.value = 1
                        isDisabled.value = true 
                        
                        // console.log("选中-props.is_show--",props.is_show)
                        // console.log("选中-isDisabled--",isDisabled.value)
                    }else{
                        // console.log("取消，开关关闭")
                        isTouch.value = 0
                        isDisabled.value = false
                        // console.log("取消-props.is_show--",props.is_show)
                        // console.log("取消-isDisabled--",isDisabled.value)
                    }
                    // console.log("isDisabled---",isDisabled)
                    // console.log("finally-isDisabled.value---",isDisabled.value)
                    emit('checkboxBtn',isDisabled)
                }
           
            // }
            //监听点击事件的改变
            watch(()=>[props.botShow,props.allShow],()=>{
                // console.log("props.botshow--",props.botShow)
                // console.log("props.allShow--",props.allShow)
                if(props.botShow ==  false){
                    if(props.allShow == true){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }
                if(props.botShow ==  false){
                    if(props.allShow == false){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }
                if(props.botShow ==  true){
                    if(props.allShow == true){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }
                if(props.botShow == true){
                    if(props.allShow == false){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }

                if(props.allShow == true){
                    if(props.botShow == true){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }
                if(props.allShow == false){
                    if(props.botShow == true){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }
                if(props.allShow == false){
                    if(props.botShow == false){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }
                if(props.allShow == true){
                    if(props.botShow == false){
                        isTouch.value = 0
                        isDisabled.value = false
                    }
                }
               
            })
            return {
                // ...methodObj,
                isTouch,isDisabled,checkboxBtn
            }
        }
    }
</script>

<style scoped>
     /* 外层div */
     .o_wrap{
        height:8%;
        width: auto;
        position: relative; 
        margin-top:5px;
    }
    .o_checkbox{
        height:100%;
        width: 100%;
        /* background-color: #E9EEF3 !important; */
        margin:0px 10px;
        cursor: pointer;
        /* margin: 0 auto; */
        
    }
    /* 复选框图标样式 */
    .o_checkbox i.o_icon{
        width: 1.2em;
        height: 1.2em;
        background-color: #E9EEF3;
        border-radius: 100%;
        float: left;
        text-align: center;
        border: 1px solid #666;
    }
    .o_checkbox i.o_icon_in{
        background-color: rgb(235, 164, 71);
    }
    .o_checkbox i.o_icon_out{
        background-color: #E9EEF3;
    }
    /* 复选框旁边文字样式 */
    .o_checkbox span.o_span{
        float: left;
        user-select: none;
        margin: 0 0 0 2px;
        font-size: 10px;
        /* font-family: fantasy; */
        font-weight: bold;
        background-color: #9cc5f0;
        border-left: 5px solid #3888de;
        padding:0 6px;
        border-radius: 0 6px 6px 0;
    }
    .o_checkbox span.o_span_in{
        background-color:#4198f6;
    }
    .o_checkbox span.o_span_out{
        background-color: #9cc5f0;
    }

    /* 禁用 通过控制类名*/
    /* :class = '{"o_checkbox_disabled":isDisabled}' */
    /* .o_checkbox_disabled i.o_icon{
        background: #eee;
        border: 1px solid #8c8a8a;
        color: #979696;
    }
    .o_checkbox_disabled span.o_span{
        color: #979696;
    } */

   /* 禁用框 */
    /* .o_input{
        height:100%;
        width: 100%;
        position: absolute;
        z-index: 9;
        border-radius: 10%;
        border:0;
        background-color: #8c8a8a;
        opacity: 0.2;
        cursor: no-drop;
    } */
    



    /* 引入对号图标的样式 */
    @font-face {
        font-family: "iconfont"; /* Project id 3957748 */
        src: url('//at.alicdn.com/t/c/font_3957748_nrqcodgblar.woff2?t=1678971483434') format('woff2'),
            url('//at.alicdn.com/t/c/font_3957748_nrqcodgblar.woff?t=1678971483434') format('woff'),
            url('//at.alicdn.com/t/c/font_3957748_nrqcodgblar.ttf?t=1678971483434') format('truetype');
    }

    .iconfont {
        font-family: "iconfont" !important;
        font-size: 0.8em;
        font-style: normal;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        line-height: 1.3em;
    }

    .icon-duihao:before {
        content: "\e62f";
    }


</style>