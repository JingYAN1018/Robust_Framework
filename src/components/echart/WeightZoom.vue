<template>
    <div class="container" ref="container">></div>    
</template>
<script>
    import {ref,onMounted,watch} from 'vue'
    import * as echarts from 'echarts';
    import axios from 'axios'
    export default{
        name:'WeightZoom',
        props:{
            dataset:String,
            model:String,
            instance:Number
        },
        setup(props){
            var xLabel = ['0','0.1','0.2','0.3','0.4','0.5','0.6','0.7','0.8','0.9','1.0','1.1','1.2','1.3','1.4','1.5','1.6','1.7','1.8','1.9','2.0']
            var finetuning = [0,0,0,0,1.0e+3,3.0e+3,5.0e+3,1.0e+4,1.5e+4,1.8e+4,2.0e+4,1.8e+4,1.5e+4,1.0e+4,5.0e+3, 3.0e+3,1.0e+3,0,0,0,0]
            var retraining = [2.0e+4,1.8e+4,1.2e+4,1.0e+4,5.0e+3,3.0e+3,1.0e+3,0.8e+3,0.5e+3,0.2e+3,0,0,0,0,0,0,0,0,0,0,0]
            var remos = [ 8.0e+3,6.0e+3,4.0e+3,2.0e+3, 2.0e+3, 1.0e+3,0,1.0e+3,5.0e+3,1.0e+4, 1.5e+4,2.0e+4,1.5e+4, 1.0e+4,5.0e+3,1.0e+3,0,0,0,0,0,0]
            const option = {
                color: ["#3398DB"],
                backgroundColor:'#f7f8f8',
                grid: { 
                    height:'40%',
                    width:'90%',
                    left: 10, 
                    // top: 40, 
                    bottom: '20%', 
                    // right: 10,
                    containLabel: true 
                },
                legend: {
                    type: 'plain',
                    top: 10,
                    right: 20,
                    itemGap: 34,
                    itemWidth: 10,
                    icon: 'circle',
                    selectedMode: false,
                    textStyle: { padding: [0, 0, 0, 4] },
                    data: ['Fine-tuning', 'Retraining','ReMos'],
                },
                xAxis:{
                    type:'category',
                    data: xLabel,
                    // name:'Ws/Wt',
                    // nameTextStyle: {
                    //     fontSize: 12,
                    //     fontStyle:'oblique',
                    //     fontWeight:'bold',
                    //     align:'left'
                    // },
                    // axisLine: { lineStyle: { color: '#ccc' } },
                    // axisTick: { length: 3 },
                    // axisLabel: { color: '#999' },
                },
                yAxis: {
                    type: 'value',
                    name:'Weight Count',
                    nameTextStyle: {
                        fontSize: 12,
                        fontStyle:'oblique',
                        fontWeight:'bold',
                        align:'left'
                    },
                    axisLine: { 
                        show: true, 
                    },
                    // axisLabel: { color: '#999' },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            color: '#999',
                        },
                    },
                },
                tooltip: {
                    trigger: 'axis',
                    padding: [12, 17, 20, 23],
                    textStyle: { color: '#424242' },
                    renderMode: 'html',
                    className: 'tooltip',
                },
                series: [
                    {
                        name:'Fine-tuning',
                        type:'line',
                        data:finetuning,
                        smooth:true,//平滑曲线
                        showSymbol: false,
                        itemStyle: { color: '#126EFC' },
                        lineStyle: { width: 2, color: '#126EFC' },
                        areaStyle: { color: 'rgba(0, 110, 254, 0.1)' },
                    },{
                        name:'Retraining',
                        type:'line',
                        data:retraining,
                        smooth:true,
                        showSymbol: false,
                        itemStyle: { color: '#1BB389' },
                        lineStyle: { width: 2, color: '#1BB389' },
                        areaStyle: { color: 'rgba(27, 179, 137, 0.1)' },
                    },{
                        name:'ReMos',
                        type:'line',
                        data:remos,
                        smooth:true,
                        showSymbol: false,
                        itemStyle: {
                            color: 'rgba(248,172,6,1)',
                        },
                        lineStyle: {
                            normal: {
                                width: 2,
                                color: 'rgba(248,172,6,1)', // 线条颜色
                            },
                        },
                        areaStyle: {
                            color:'#ffe79fff'
                        },
                    }
                ],
                dataZoom: [{
                    type:'slider',
                    show:'true',
                    // fillerColor: "rgba(150, 179, 218, 0.4)",
                    // borderColor: "rgba(214, 213, 213, 1)",
                    // handleIcon:'',
                    bottom:8,
                    height: 12,
                    handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                    handleSize: '120%'

                }],
            }
            const container = ref(null)
            var myChart = null
            onMounted(()=>{
                myChart = echarts.init(container.value)
                myChart.setOption(option)
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                        myChart.resize();
                });
            })
            var datasetVal = ref(null), modelVal = ref(null), instanceVal = ref(null)
            datasetVal.value = props.dataset
            modelVal.value = props.model
            instanceVal.value = props.instance
            watch(()=>[props.dataset,props.model, props.instance],()=>{
                datasetVal.value = props.dataset
                modelVal.value = props.model
                instanceVal.value = props.instance
                // console.log('weightzoom-dataset--',datasetVal.value)
                // console.log('weightzoom-model--',modelVal.value)
                // console.log('weightzoom-instance--',instanceVal.value)
                axios.get('/categoryData/instanceWeiCount.json').then(res=>{
                    for(let i=0; i<res.data.length; i++){
                        if(datasetVal.value == res.data[i].dataset && modelVal.value == res.data[i].model && instanceVal.value == res.data[i].instance){
                            // console.log("weightZoom--res--",res.data[i])
                            option.series[0].data = res.data[i].finetuning
                            option.series[1].data = res.data[i].retraining
                            option.series[2].data = res.data[i].remos
                            // console.log('option.series[1]--',option.series[1])
                            myChart= echarts.init(container.value)
                            myChart.setOption(option)
                            break;
                        }
                    }
                }).catch(err=>{console.log(err)})
            },{deep:true})

        return{
            option,container,myChart,
            datasetVal, modelVal, instanceVal,
        }
    }
}
</script>
<style>

</style>