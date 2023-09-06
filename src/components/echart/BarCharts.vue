<template>
    <!-- <p>选中的数据集：{{  state.datasetValue }}。模型：{{  state.modelValue }}</p> -->
    <!--当父组件传递过来的值为空时，使图表不可见，可以用一个按钮覆盖在内部div盒子上，因为按钮有v-show功能 -->
    <input type="button" class="no_chart" v-show="isDisabled">
    <div 
        ref="barEcharts" 
        id="barEcharts" 
        >
    </div>
    
</template>

<script>
    import * as echarts from 'echarts';
    import { ref, onMounted,reactive,watch,onUnmounted } from 'vue';
    import axios from 'axios';
 
    export default {
        name:'BarCharts',
        props:{
            //接收父组件传递过来的值
            datasetName:String,
            modelName:String
        },
        setup(props){
            //当进入页面时，datasetValue和modelValue为空，就不显示初始化图表
            //控制图表的显示,默认值true
            let isDisabled = ref(true)
            //定义响应式变量，使用兄弟组件传递过来的值
            const state = {
                datasetValue: ref(),
                modelValue: ref()
            }
            
            state.datasetValue.value = props.datasetName
            state.modelValue.value = props.modelName
            
            
            //设置echarts数据
            const xData =['Standard', 'PartFinetuned', 'FullFinetuned']
            const yData = reactive({
                Original_Inputs: [],
                Adversarial_Inputs: [],
                Decline_Ratio: []
            })
            //获取图表实例
            const barEcharts = ref(null)

            //监听响应式变量的变化
            watch(()=>[props.datasetName,props.modelName], (newValues, oldValues)=>{
                // 注意：这里重新赋值的含义是，外层赋值得到值之后，就不再发生改变
                //所以当watch监听到父组件传递的值发生变化，需要重新再进行复制
                state.datasetValue.value = props.datasetName
                state.modelValue.value = props.modelName
                //点击按钮，获取图表相应的值，显示图表isDisabled=false
                isDisabled.value = !(props.datasetName && props.modelName)
                axios.get("/statData/statData.json").then((res)=>{
                    // 判断两个按钮的值是否为空，不为空的情况下，对echarts图表进行更新
                    if(state.datasetValue.value && state.modelValue.value){
                        for(var i=0; i<res.data.length;i++){
                            if(res.data[i].dataset_name == state.datasetValue.value && res.data[i].model_name == state.modelValue.value){
                                yData.Original_Inputs = res.data[i].results.Original_Inputs
                                yData.Adversarial_Inputs = res.data[i].results.Adversarial_Inputs
                                yData.Decline_Ratio = res.data[i].results.Decline_Ratio
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
            
            const option ={
                //显示Echarts图表大小、位置、显示网格线
                grid: {
                    show:true, // 显示网格线
                    // top: '20%',
                    bottom: '13%',
                    right: '5%',
                    left: '5%',
                    // bottom:10,
                    // right:10
                    containLabel:true,
                    width: '90%',
                    height: '60%',
                    
                } ,
                // 提示框组件
                tooltip: {
                    // 触发类型. axis坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用。
                    trigger: 'axis',
                    confine: true,
                    // 坐标轴指示器配置项。
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: 'red',
                            // width:20
                            // appendToBody: true,
                            confine:true

                        }
                    }
                },
                //设置组件与容器的距离
                legend:{
                    right: "30%"
                },
                // 工具栏
                toolbox: {
                    itemSize: 10,
                    left: 'right', // 工具栏距离容器右侧为 0
                    top: 'top', // 工具栏距离容器上侧为 0
                    feature: {
                        dataView: { show: true, readOnly: false },//数据视图工具，可以展现当前图表所用的数据，编辑后可以动态更新
                        magicType: { show: true, type: ['line', 'bar'] },//动态类型切换
                        restore: { show: true },//配置项还原
                        saveAsImage: { show: true }
                    }
                },
                // 图例组件
                legend: {
                    bottom:'2%',
                    left:'1%',
                    data: ['Original Inputs', 'Adversarial Inputs', 'Decline Ratio'],
                    textStyle: {
                        fontSize: 10 // 设置字体大小
                    },
                    itemWidth: 30, // 设置图例项的宽度为 30px
                    itemHeight: 10, // 设置图例项的高度为 20px
                },
                // 直角坐标系 grid 中的 x 轴
                xAxis: [
                    {
                    // 坐标轴类型,'value' 数值轴，适用于连续数据。
                    //'category' 类目轴，适用于离散的类目数据。为该类型时类目数据可自动从 series.data 或 dataset.source 中取，或者可通过 xAxis.data 设置类目数据。
                    type: 'category',
                    data: xData,
                    nameTextStyle: {
                        fontSize: 10 // 设置 x 轴坐标名称字体大小为 16px
                    },
                    nameLocation : 'center',
                    axisTick: {
                        alignWithLabel: true
                    },
                    // 坐标轴指示器配置项
                    axisPointer: {
                        // 指示器类型shadow阴影指示器
                        type: 'shadow'
                    },
                    axisLabel: {
                        textStyle: {
                            fontSize: 10 // 设置 x 轴标签字体大小为 14px
                        }
                    }
                    }
                ],
                yAxis: [
                    {
                    // 'value' 数值轴，适用于连续数据。
                    type: 'value',
                    name: 'Accuracy',
                    nameTextStyle: {
                        fontSize: 10 // 设置 x 轴坐标名称字体大小为 16px
                    },
                    min: 0,
                    max: 100,
                    // 强制设置坐标轴分割间隔,一般不建议使用
                    // interval: 50,
                    axisLabel: {
                        // 刻度标签的内容格式器，支持字符串模板和回调函数两种形式。
                        formatter: '{value} %',
                        textStyle: {
                            fontSize: 10 // 设置 x 轴标签字体大小为 14px
                        }
                    }
                    },
                    {
                    type: 'value',
                    name: '鲁棒性下降率DR',
                    nameTextStyle: {
                        fontSize: 10 // 设置 x 轴坐标名称字体大小为 16px
                    },
                    min: 0,
                    max: 100,
                    // interval: 5,
                    axisLabel: {
                        formatter: '{value} %',
                        textStyle: {
                            fontSize: 10 // 设置 x 轴标签字体大小为 14px
                        }
                    }
                    }
                ],
                series: [
                {
                    // 系列名称，用于tooltip的显示，legend 的图例筛选，在 setOption 更新数据和配置项时用于指定对应的系列。
                    name: 'Original Inputs',
                    type: 'bar',
                    tooltip: {
                        valueFormatter: function (value) {
                        return value + ' %';
                        }
                    },
                    data: [0,0,0]
                },
                {
                    name: 'Adversarial Inputs',
                    type: 'bar',
                    tooltip: {
                        valueFormatter: function (value) {
                        return value + ' %';
                        }
                    },
                    data:[0,0,0]
                },
                {
                    name: 'Decline Ratio',
                    type: 'line',
                    // 使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用。
                    yAxisIndex: 1,
                    tooltip: {
                        valueFormatter: function (value) {
                        return value + ' %';
                        }
                    },
                    data: [0,0,0]
                }]
            };
            // 定义绘制图表用的盒子
            let myChart = null
            function updateChart(){
                 //重新渲染
                option.series[0].data = Object.values(yData.Original_Inputs)
                option.series[1].data = Object.values(yData.Adversarial_Inputs)
                option.series[2].data = Object.values(yData.Decline_Ratio)
                // console.log("option---",option)
                myChart.setOption(
                    //将原先值给覆盖
                    option
                )
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            }
            onMounted(()=>{
                //设置绘制图表
                myChart=  echarts.init(barEcharts.value)
                // console.log('渲染')
                myChart.setOption(option)

                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            });
            // onUnmounted(()=>{
            //     myChart.destroy()
            // })
            return {
                yData,barEcharts,state,isDisabled
            }
            
        }
       
    }


</script>

<style scoped>
    #barEcharts{
        width: 100%;
        height: 100%;
        /* border: 1px solid rebeccapurple; */
    }
    .no_chart{
        height: 23.2%;
        width: calc("39%");
        position: absolute;
        z-index: 9;
        border: 0;
        background-color: #E9EEF3;
        opacity: 1;
        /* box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.16); */
        cursor: no-drop;
        border-radius: 0 0 0 12px;
    }
</style>