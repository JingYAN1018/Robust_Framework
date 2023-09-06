<template>
    <!-- <input type="button" class="no_chart" v-show="isShow"> -->
    <div ref="lineEcharts" id="lineEcharts" ></div>
    <!-- <span>{{state.datasetValue}}</span> -->

</template>
<script>
    import {ref,onMounted,reactive,watch} from 'vue'
    import * as echarts from 'echarts'
    import axios from 'axios';
    export default{
        name:'StackCharts',
        props:{
            datasetName:String,
            modelName:String
        },
        setup(props){
        
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        label: {
                            backgroundColor: '#6a7985'
                        }
                    }
                },
                legend: {
                    left:'right',
                    orient:'vertical',
                    top:'top',
                    data: ['noPretained', 'partFinetuned', 'allFinetuned'],
                    textStyle: {
                        color: '#333333FF', //y轴上的字体颜色
                        fontSize: '10' // y轴字体大小
                    }
                },
                grid: {
                    top:'20%',
                    left: '3%',
                    right: '4%',
                    bottom: '1%',
                    height: '76%',
                    width:'87%',
                    containLabel: true
                },
                // toolbox: {
                //     feature: {
                //     saveAsImage: {}
                //     }
                // },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['0', '0.1', '0.2', '0.3', '0.4', '0.5', '0.6','0.7','1'],
                     // y轴的颜色和宽度
                    axisLine: {
                    show: true, //是否显示x轴
                    lineStyle: {
                        color: '#333', // y坐标轴的轴线颜色
                    }
                    },
                    axisLabel: {
                        show: true, //这行代码控制着坐标轴y轴的文字是否显示
                            textStyle: {
                                color: '#333333FF', //y轴上的字体颜色
                                fontSize: '10' // y轴字体大小
                            }
                    },
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        show: true, //这行代码控制着坐标轴y轴的文字是否显示
                            textStyle: {
                                color: '#333333FF', //y轴上的字体颜色
                                fontSize: '10' // y轴字体大小
                            }
                    },
                },
                series: [
                    
                    {
                    name: 'noPretained',
                    type: 'line',
                    smooth:true,
                    // areaStyle: {},
                    // stack: 'Total',
                    data: [81,76,73,66,65,62,60, 56,46]
                    },
                    {
                    name: 'partFinetuned',
                    type: 'line',
                    smooth:true,
                    // areaStyle: {},
                    // stack: 'Total',
                    data: [89,82,63,57,43,36,29, 21,11]
                    },
                    {
                    name: 'allFinetuned',
                    type: 'line',
                    smooth:true,
                    // areaStyle: {},
                    // stack: 'Total',
                    data: [96,86,71,64,52,41,33, 26,16]
                    },
                ]
            };
            //获取图表实例
            const lineEcharts = ref(null)
            // 定义绘制图表用的盒子
            let myChart = null

            onMounted(()=>{
                //设置绘制图表
                myChart=  echarts.init(lineEcharts.value)
                // console.log('渲染')
                myChart.setOption(option)
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            });
             //控制图表的显示,默认值true
            let isDisabled = ref(true)
            //定义响应式变量，使用兄弟组件传递过来的值
            const state = {
                datasetValue: ref(),
                modelValue: ref(),
            }
            state.datasetValue.value = props.datasetName
            state.modelValue.value = props.modelName
             //设置echarts数据
            const dataValues = reactive({
                noPretrained: [],
                PartFinetuned: [],
                AllFinetuned: []
            })
            //监听响应式变量的变化
            watch(()=>[props.datasetName,props.modelName], (newValues, oldValues)=>{
                // 注意：这里重新赋值的含义是，外层赋值得到值之后，就不再发生改变
                //所以当watch监听到父组件传递的值发生变化，需要重新再进行复制
                state.datasetValue.value = props.datasetName
                state.modelValue.value = props.modelName
                // console.log("props.datasetName--",props.datasetName)
                // console.log("state.modelValue.value--",state.modelValue.value)
                //点击按钮，获取图表相应的值，显示图表isDisabled=false
                isDisabled.value = !(props.datasetName && props.modelName)
                axios.get("/statData/pertuData.json").then((res)=>{
                    // console.log("res.data--",res.data)
                    // 判断两个按钮的值是否为空，不为空的情况下，对echarts图表进行更新
                    if(state.datasetValue.value && state.modelValue.value){
                        for(var i=0; i<res.data.length;i++){
                            if(res.data[i].dataset_name == state.datasetValue.value && res.data[i].model_name == state.modelValue.value){
                                dataValues.noPretrained = res.data[i].results.noPretrained
                                dataValues.PartFinetuned = res.data[i].results.partFinetuned
                                dataValues.AllFinetuned = res.data[i].results.allFinetuned
                            }
                            // console.log("res.data[i].results.standard_model--",res.data[0].results.standard_model)
                            // console.log("yData.standard_model",yData.standard_model)
                        }
                        // 更新图表
                        updateChart() 
                    }
                })
                .catch((error)=>{
                    console.error("请求出错了：", error)
                })
              
            },{deep:true})

           
            function updateChart(){

                // console.log("dataValues.noPretrained--",dataValues.noPretrained)
                // console.log(" Object.values(dataValues.noPretrained)--", Object.values(dataValues.noPretrained))
                // console.log("option.series--",option.series)
                //重新渲染
                option.series[0].data = dataValues.noPretrained
                option.series[1].data= dataValues.PartFinetuned
                option.series[2].data = dataValues.AllFinetuned
                myChart.setOption(
                    //将原先值给覆盖
                    option
                )
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            }

            return {
                lineEcharts,dataValues,state,isDisabled
            }
        }

    }

</script>

<style>
    #lineEcharts{
        background-color: #f7f8f8;
        height: 100%;
        /* box-shadow: 10px 10px 20px #e4e1de66; */
        /* border: 1px solid rgb(188, 221, 246); */
        /* border-radius: 0px 10px 10px; */
    }

</style>