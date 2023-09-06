<template>
    <div class="outer_box" style="display: flex;justify-content: center;margin-top: 1px;">
        <span class="params_title" >{{title}}</span>
        <div id="slider" :style="{left:'-6%'}">
            <input id="range" type="range" :min="min" :max="max" step="0.01" v-model="rangeValue" @change="updateValue">
            <div class="bar" style="width:60px" >
                <div class="progressBar" :style="{ width: rangeValue*84 + '%'}"></div>
            </div>
            <p id="rangeValue">{{rangeValue}}</p>
        </div>
    </div>
</template>
<script>
    import {ref,reative,defineComponent } from 'vue'
    export default defineComponent({
        name:"ModelSlider",
        props:{
            //接收从父组件传递的值
            titleVal: String,
        },
        setup(props,{emit}){
            let min=ref(0)
            let max=ref(1)
            let rangeValue = ref('1')
            let title = ref(props.titleVal)
            const updateValue=()=>{
                // rangeValue = document.getElementById("range").value;
                // console.log(rangeValue)
                emit('updateValue',rangeValue.value);
            }
           return{
            rangeValue,max,min,
            title,updateValue
           }
            
        }
    })
</script>
<style>
  
    .params_title{
        display: inline-block;
        /* font-family:'Times New Roman', Times, serif; */
        font-size: 10px;
        width: 90px;
        /* min-width: 40px; */
        height: 12px;
        min-height: 8px;
        background-color: #DDEDF6;
        /* box-shadow:1px 1px 3px #9c886d66; */
        border:2px #86BEF9 solid;
        padding: 2px; 
        padding-bottom: 4px;
        text-align: center;
        border-radius: 20px 0 0 20px;

    }
    
    #slider {
        position: relative;
        /* top: 40%; */
        /* left: 4%; */
        width: 100px;
        min-width: 40px;
        height: 10px;
        min-height: 4px;
        padding: 6px;
        background: #86BEF9;
        border-radius: 20px;
        display: flex;
        align-items: center;
        /* box-shadow: 0px 10px 20px #9c886d66; */
    }
 
    #slider p {
        font-family:'Times New Roman', Times, serif;
        font-size: 14px;
        font-weight: 500;
        width: 10px;
        /* height: 9px; */
        padding-left: 6px;
        color: black;
    }
    
    #slider input[type="range"] {
        appearance:none;
        -moz-appearance:none; /* Firefox */
        -webkit-appearance:none !important; /* Safari and Chrome */
        width: 70px;
        height: 30%;
        background: #EED57F;
        border: none;
        outline: none;
    }
 
    #slider input[type="range"]::-webkit-slider-thumb {
        appearance:none;
        -moz-appearance:none; /* Firefox */
        -webkit-appearance:none !important; /* Safari and Chrome */
        -webkit-box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.4);
        box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.4);
        width: 20px;
        height: 20px;
        background: #EED57F !important;
        /* background-image: url(../../assets/selector.png); */
        border: 2px solid #457DED !important;
        border-radius: 50%;
        cursor: pointer;
        z-index: 3;
    }
 
    #slider input[type="range"]::-webkit-slider-thumb:hover {
        background: #f8a460;
        z-index: 30;
    }
    .bar{
        height: 20%;
        border: none;
        outline: none;
        left: 10;
        top:10;
        position: absolute;
        padding-right: 10px;
    }
    .progressBar{
        background: #f8a460;
        opacity: 0.8;
        z-index: 2;
        height: 100%;
    }
</style>