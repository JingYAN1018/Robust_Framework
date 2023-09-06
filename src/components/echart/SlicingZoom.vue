<template>
    <div class="container" ref="container">></div>    
</template>
<script>
    import {ref,onMounted,watch} from 'vue'
    import * as echarts from 'echarts';
    import axios from 'axios'
    export default{
        name:'SlicingZoom',
        props:{
            dataset:String,
            model:String,
            instance:Number
        },
        setup(props){
            const option = {
                color: ["#3398DB"],
                backgroundColor:'#f7f8f8',
                grid:{
                    width:'90%',
                    left:20,
                    right:10
                },
                tooltip: {
                    trigger: "axis"
                },
                xAxis: {
                    data: ["1","2","3","4","5","6","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"],
                },
                yAxis: {
                    type:'value',
                    name:'Weight Magnitude',
                    nameTextStyle:{
                        fontStyle:'oblique',
                        fontWeight:'bold',
                        fontSize:12,
                        align:'left'
                    },
                    axisLabel:{
                        show:false
                    },
                    formatter: '{value}',
                    splitLine: {
                        show: false
                    },
                    axisLine:{
                        show:false,
                    }
                },
                dataZoom: [{
                    type:'slider',
                    show:'true',
                    // fillerColor: "rgba(150, 179, 218, 0.4)",
                    // borderColor: "rgba(214, 213, 213, 1)",
                    // handleIcon:'',
                    top: 80,
                    height: 16,
                    handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                    handleSize: '120%'

                }],
                series: {
                    name: "weight mag",
                    type: "bar",
                    data: [0.2,0.4,0.5,1.2,1.4,1.6,1.72,1.8,1.6,1.68,3.9,4.7,5.4,1.8,3.6,7.9,6.4,5.8,6.7,7.1,6.4]
                }
            }
            const container = ref(null)
            var myChart = null
            var datasetVal = ref(null), modelVal = ref(null), instanceVal= ref(null)
            datasetVal.value = props.dataset
            modelVal.value = props.model
            instanceVal.value = props.instance
            onMounted(()=>{
                myChart = echarts.init(container.value)
                myChart.setOption(option)
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                        myChart.resize();
                });
            })
            watch(()=>[props.dataset,props.model,props.instance],()=>{
                datasetVal.value = props.dataset
                modelVal.value = props.model
                instanceVal.value = props.instance
                // console.log('slicingzoom-dataset--',datasetVal.value)
                // console.log('slicingzoom-model--',modelVal.value)
                // console.log('slicingzoom-instance--',instanceVal.value)
                axios.get('/categoryData/instanceSlicing.json').then(res=>{
                    for(let i = 0; i<res.data.length;i++){
                        if(datasetVal.value == res.data[i].dataset && modelVal.value == res.data[i].model && instanceVal.value == res.data[i].instance){
                            option.series.data = res.data[i].data
                            myChart= echarts.init(container.value)
                            myChart.setOption(option)
                            break;
                        }
                    }
                }).catch(err=>{console.log(err)})
            },{deep:true})

          
        return{
            option,container,myChart,
            datasetVal,modelVal,instanceVal,
        }
    }
}
</script>
<style>

</style>