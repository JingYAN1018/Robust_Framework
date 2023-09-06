<template>
    
    <p class="improve_title">&nbsp;&nbsp;Model Behavior Comparison View</p>
    <div class="charts" style="flex:9.7;display: flex; flex-direction: column;">
        <slicing-zoom style="flex:2" :dataset="datasetName" :model="modelName" :instance="instanceVal"></slicing-zoom>
        <weight-zoom style="flex:3" :dataset="datasetName" :model="modelName" :instance="instanceVal"></weight-zoom>
        <div class="container" style="flex:5">
            <el-switch v-model="isDataUpdate" inline-prompt
                class="container_button"
                active-text="Updated" inactive-text="Initialized" @change='updateData' />
            <div class="scatterplot_container" ref="container">1111</div>
        </div>
      
    </div>
   
</template>

<script>
    import * as d3 from "d3"
    import { ref, onMounted,reactive,watch} from 'vue';
    import axios from 'axios';
    import * as echarts from 'echarts';
    import WeightZoom from '../components/echart/WeightZoom.vue'
    import SlicingZoom from '../components/echart/SlicingZoom.vue'
    export default{
        name:'ImproveView',
        props:{
            dataset:String,
            model:String,
            instance:Number
        },
        components:{
            WeightZoom,SlicingZoom 
        },
        setup(props){
            //获取响应实例
            const container = ref(null)
            // 定义绘制图表用的盒子
            var myChart = null
            const data = []
            var isDataUpdate = ref(false)
            const updateData = () =>{
                if(isDataUpdate === true){
                    isDataUpdate.value = false
                }else if(isDataUpdate === false){
                    isDataUpdate.value = true
                }
                // console.log('isDataUpdate',isDataUpdate.value)
            }
            const option = {
                grid:{
                    // top:30,
                    bottom:30,
                    right:20,
                    height:'80%'
                },
                xAxis: {
                    scale: true,
                    show: false
                },
                yAxis: {
                    scale: true,
                    show: false
                },
                series: [
                    {
                        type: 'effectScatter',
                        symbolSize: 20,
                        data: [
                        ]
                    },
                    {
                        name:'分类1',
                        type: 'scatter',
                        // prettier-ignore
                        data: [],
                    },
                    {
                        name: '分类2',
                        type:'scatter',
                        data: []
                    }
                ]
            };
           //响应式变量
           const datasetName = ref(null)
           const modelName = ref(null)
           const instanceVal = ref(null)
           datasetName.value = props.dataset
           modelName.value = props.model.substring(3)
           instanceVal.value = props.instance
            onMounted(()=>{
                //设置绘制图表
                myChart= echarts.init(container.value)
                myChart.setOption(option)

                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            })
            watch((props.dataset,props.model,props.instance,isDataUpdate),()=>{
                datasetName.value = props.dataset
                modelName.value = props.model
                instanceVal.value = props.instance
                // console.log(isDataUpdate.value)
                // console.log(datasetName.value)
                // console.log(modelName.value)
                // console.log(instanceVal.value)
                if(datasetName.value && modelName.value && instanceVal.value){
                    // console.log('进入条件判断---')
                    axios.get('/categoryData/instanceScatter.json').then(res=>{
                        for(let i=0; i<res.data.length;i++){
                            // console.log('res.data--',res.data)
                            if(res.data[i].dataset_name==datasetName.value && res.data[i].model_name==modelName.value && res.data[i].instance_id==instanceVal.value){
                                if(isDataUpdate.value === true){
                                    console.log("true")
                                    option.series[0].data[0] = res.data[i].data_after[0]
                                    // option.series[0].data[1] = res.data[i].data_after[1]
                                    option.series[1].data= res.data[i].data_after.filter(item=>item[2]===1)
                                    option.series[2].data= res.data[i].data_after.filter(item=>item[2]===2)
                                    myChart= echarts.init(container.value)
                                    myChart.setOption(option)
                                    myChart.on('click',function(params) {
                                        console.log(params.data)
                                    })
                                    return 
                                }else if(isDataUpdate.value === false){
                                    console.log("false")
                                    // option.series[0].data[0] = res.data[i].data_before[0]
                                    option.series[0].data[0] = res.data[i].data_before[1]
                                    option.series[1].data= res.data[i].data_before.filter(item=>item[2]===1)
                                    option.series[2].data= res.data[i].data_before.filter(item=>item[2]===2)
                                    console.log(option.series)
                                    myChart= echarts.init(container.value)
                                    myChart.setOption(option)
                                    myChart.on('click',function(params) {
                                        console.log(params.data)
                                    })
                                    return 
                                }
                            }
                        }
                    }).catch((error)=>{
                        console.error("请求出错了：", error)
                    },{
                        deep:true
                    })
                   
                    // return
                }
                
            })

            return {
                container,option,myChart,datasetName,modelName,instanceVal,isDataUpdate,updateData
            }
        }
    }

</script>

<style>
.improve_title{
    flex:0.2;
    background-color: #f1f5cd;
    opacity: .9;
    width: auto;
    font-size: 14px;
    font-family: fantasy;
    font-weight: bold;
    box-shadow: 2px 0px 2px 0 rgba(0, 0, 0, 0.16);
    z-index: 11;
}
.charts .container{
    background-color: #f7f8f8;
}
.charts .container .container_button{
    --el-switch-on-color: #86BEF9; 
    --el-switch-off-color: #DBEBF3; 
    margin-left: 70%; 
    margin-top: 20px;
}
.charts .scatterplot_container {
    /* flex: 5; */
    display: flex;
    justify-content: center;
    align-items: center;
    height: 86%;
    margin-top: -20px;
    /* background-color: darkorange; */
}
</style>