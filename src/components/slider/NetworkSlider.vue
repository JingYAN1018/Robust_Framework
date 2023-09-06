<template>
    <div class="slider" :style="{top:(windowHeight <= '900'? '10.7%':'10.9%'),left:(windowWidth <= '1600'? '-4.2%':'-4%')}">
        <input type="range" :min="min" :max="max" step="1" v-model="rangeVal" @change="onRangeChange">
        <div class="bar" style="width:140px">
            <div class="progressBar" :style="{ width: (rangeVal/max)*87 + '%'}"></div>
        </div>
        <p id="rangeVal">{{rangeVal}}</p>
    </div>
</template>
<script>
    import {ref,onMounted,emit,watch} from 'vue'
    export default{
        name:"Slider",
        props:{
            maxVal:Number,
            minVal:Number,
            changeVal:Number//控制图标底部滑块变动之后，顶部控制参数的滑块恢复到原位
        },
        setup(props,{emit}){
            var min=ref(null)
            var max=ref(null)
            min.value = props.minVal
            max.value = props.maxVal

            var rangeVal = ref(max.value)

            var windowWidth = ref(document.body.clientWidth)
            var windowHeight = ref(document.body.clientHeight)

            onMounted(()=>{

            })
             
            const onRangeChange = ()=>{
                console.log("rangeVal--",rangeVal.value)
                emit('onRangeChange',rangeVal.value)
            }
            //该参数用于底部滑块滑动之后，顶部滑块的更新，即顶部滑块恢复原位最大值
            watch(()=>[props.changeVal],()=>{
                rangeVal.value = props.changeVal
                // console.log("监听rangeVal的变化--",rangeVal.value)
            })
           return{
            rangeVal,max,min,windowWidth,windowHeight,onRangeChange
           }
            
        }
    }
</script>
<style>
    
    .slider {
        position: relative;
        /* top: 40%; */
        /* left: 4%; */
        width: 180px;
        min-width: 80px;
        height: 13px;
        min-height: 4px;
        padding: 6px;
        background: #86BEF9;
        border-radius: 20px;
        display: flex;
        align-items: center;
        box-shadow: 0px 10px 20px #9c886d66;
    }
 
    .slider p {
        font-family:'Times New Roman', Times, serif;
        font-size: 14px;
        font-weight: 500;
        width: 10px;
        padding-left: 16px;
        color: black;
    }
    
    .slider input[type="range"] {
        appearance:none;
        -moz-appearance:none; /* Firefox */
        -webkit-appearance:none !important; /* Safari and Chrome */
        width: 140px;
        height: 30%;
        background: #EED57F;
        border: none;
        outline: none;
    }
 
    .slider input[type="range"]::-webkit-slider-thumb {
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
 
    .slider input[type="range"]::-webkit-slider-thumb:hover {
        background: #f8a460;
        z-index: 3;
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