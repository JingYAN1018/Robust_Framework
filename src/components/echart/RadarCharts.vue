<template>
    <!-- <p>选中的数据集：{{  state.datasetValue }}。模型：{{  state.modelValue }}</p> -->
    <!--当父组件传递过来的值为空时，使图表不可见，可以用一个按钮覆盖在内部div盒子上，因为按钮有v-show功能 -->
    
    <div 
        ref="radarEcharts" 
        id="radarEcharts" 
        >
    </div>
    <!-- <input type="button" class="no_chart" v-show="isDisabled"> -->
    
</template>

<script>
    import * as echarts from 'echarts';
    import { ref, onMounted,reactive,watch,onUnmounted } from 'vue';
    import axios from 'axios';
 
    export default {
        name:'RadarCharts',
        props:{
            //接收父组件传递过来的值
            datasetName:String,
            modelName:String,
            pertuName:Number,
        },
        setup(props){
            //当进入页面时，datasetValue和modelValue为空，就不显示初始化图表
            //控制图表的显示,默认值true
            let isDisabled = ref(true)
            //定义响应式变量，使用兄弟组件传递过来的值
            const state = {
                datasetValue: ref(),
                modelValue: ref(),
                pertuValue: ref()
            }
            
            state.datasetValue.value = props.datasetName
            state.modelValue.value = props.modelName
            state.pertuValue.value = props.pertuName
            
            
            //设置echarts数据
            const dataValues = reactive({
                noPretrained: [],
                PartFinetuned: [],
                AllFinetuned: []
            })
            const list = [
                {
                    "max": 1,
                    "min": 0,
                    "name": "Robust"
                },
                {
                    "max": 1,
                    "min": 0,
                    "name": "AOI_acc"
                },
                {
                    "max": 1,
                    "min": 0,
                    "name": "AOI_f1"
                },
                {
                    "max": 1,
                    "min": 0,
                    "name": "AOI_recall"
                },
                {
                    "max": 1,
                    "min": 0,
                    "name": "AAI_recall"
                },
                {
                    "max": 1,
                    "min": 0,
                    "name": "AAI_f1"
                },
                {
                    "max": 1,
                    "min": 0,
                    "name": "AAI_acc"
                }
            ]

            // 设置每个轴对应的系列值
            function contains(arr, val) {
                var i = arr.length;
                while (i--) {
                    if (arr[i].name === val) {
                        return i;
                    }
                }
                return false;
            }
            
            const option = {
                "tooltip": {
                    "show": false,
                    // "trigger": "item",
                    // "confine": true
                },
                // 图例组件展现了不同系列的标记(symbol)，颜色和名字。可以通过点击图例控制哪些系列不显示。
                "legend": {
                    // 图标
                    "icon": "roundRect",
                    "orient":'vertical',
                    "left": 'left',
                    "top": "middle",
                    // "marginLeft":20,
                    // 图例标记的图形高度
                    // "itemHeight": 2,
                    "backgroundColor":'#E0EFF7',
                    "borderColor":'#8DB1FA',
                    "borderWidth":1,
                    "borderRadius":4,
                    "borderType":'solid',
                    "itemWidth": 6,
                    "itemHeight": 10,
                    "textStyle": {
                        "fontSize": 9,
                        // "color": "#088AD5",
                        // "backgroundColor":'#E0EFF7',
                       
                    },
                  
                },
                "radar": {
                    // 中心（圆心）坐标，数组的第一项是横坐标，第二项是纵坐标。
                    "center": [
                        "64%",
                        "56%"
                    ],
                    "radius": "50%",
                    // 坐标轴的分割段数，需要注意的是这个分割段数只是个预估值，最后实际显示的段数会在这个基础上根据分割后坐标轴刻度显示的易读程度作调整。在类目轴中无效。
                    "splitNumber": 6,
                    // 雷达图绘制类型，支持 'polygon' 和 'circle'
                    "shape": "polygon",
                    // 分隔区域的样式设置。
                    "splitArea": {
                        "areaStyle": {
                            "color": [
                                "#ddedf6",
                                "#e1f0f8",
                                "#e6f3fa",
                                "#f0f8fc"
                            ]
                        }
                    },
                    "axisLine": {
                        //是否显示坐标轴轴线。
                        "show": true,
                        "lineStyle": {
                            "color": "#088AD5"
                        }
                    },
                    "splitLine": {
                        // 是否显示分隔线。默认数值轴显示，类目轴不显示。
                        "show": false,
                        "lineStyle": {
                            "color": "#088AD5"
                        }
                    },
                    // "nameGap":'15',
                     // 雷达图的指示器，用来指定雷达图中的多个变量（维度）
                    "indicator": list,
                    "axisName":{
                        "show":true,
                        // "fomatter":"[{value}]",
                        // "fontSize":'12',
                        "rich": {
                            "a": {
                                fontSize: 12,
                                color: '#2C2E35',
                                nameGap: '10',
                                align: 'center',
                            },
                            "b": {
                                fontSize: 11,
                                color: '#5470C6',
                                fontWeight: 'bold',
                                align: 'center',
                            },
                            "c":{
                                fontSize: 11,
                                color: '#91CC75',
                                fontWeight: 'bold',
                                align: 'center',
                            },
                            "d":{
                                fontSize: 11,
                                color: '#FAC858',
                                fontWeight: 'bold',
                                align: 'center',
                            }
                        },
                        "formatter": (a) => {
                            // console.log(a)
                            let i = contains(list, a); // 处理对应要显示的样式
                            return `{a| ${a}}`;
                        },

                    },
                   
                },
                "series": {
                    "name": "雷达图",
                    "type": "radar",
                    "symbol": "none",
                    // "label": {
                    //     "show": true,
                    //     "formatter": function(params) {
                    //         return params.value; // 返回系列值
                    //     }
                    // },
                    "itemStyle": {
                        "show": false
                    },
                    "lineStyle": {
                        "normal": {
                            "width": 2
                        }
                    },
                    "areaStyle": {
                        "normal": {
                            "color": "rgba(0, 0, 0, 0)"
                        }
                    },
                    "data": [
                        {
                            "name": "noPretrained",
                            "value": [
                                0,0,0,0,0,0,0
                            ]
                        },
                        {
                            "name": "PartFinetuned",
                            "value": [
                                0,0,0,0,0,0,0
                            ]
                        },
                        {
                            "name": "AllFinetuned",
                            "value": [
                                0,0,0,0,0,0,0
                            ]
                        }
                    ]
                }
            }

            //获取图表实例
            const radarEcharts = ref(null)
            // 定义绘制图表用的盒子
            let myChart = null
            onMounted(()=>{
                //设置绘制图表
                myChart=  echarts.init(radarEcharts.value)
                // console.log('渲染')
                myChart.setOption(option)

                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            });
            //监听响应式变量的变化
            watch(()=>[props.datasetName,props.modelName,props.pertuName], (newValues, oldValues)=>{
                // 注意：这里重新赋值的含义是，外层赋值得到值之后，就不再发生改变
                //所以当watch监听到父组件传递的值发生变化，需要重新再进行复制
                state.datasetValue.value = props.datasetName
                state.modelValue.value = props.modelName
                state.pertuValue.value = props.pertuName
                // console.log('扰动变化--',props.pertuName)
                //点击按钮，获取图表相应的值，显示图表isDisabled=false
                isDisabled.value = !(props.datasetName && props.modelName)
                axios.get("/statData/modelview.json").then((res)=>{
                    // 判断两个按钮的值是否为空，不为空的情况下，对echarts图表进行更新
                    if(state.datasetValue.value && state.modelValue.value && state.pertuValue.value){
                        for(var i=0; i<res.data.length;i++){
                            if(res.data[i].dataset_name == state.datasetValue.value && res.data[i].model_name == state.modelValue.value && (res.data[i].pertu_name[0] <= state.pertuValue.value) && (state.pertuValue.value<res.data[i].pertu_name[1])){
                                dataValues.noPretrained = res.data[i].noPretrained
                                dataValues.PartFinetuned = res.data[i].PartFinetuned
                                dataValues.AllFinetuned = res.data[i].AllFinetuned
                                break;
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
            })

           
            function updateChart(){

                // console.log("dataValues.noPretrained--",dataValues.noPretrained)
                // console.log(" Object.values(dataValues.noPretrained)--", Object.values(dataValues.noPretrained))
                // console.log("option.series.data[0]--",option.series.data[0])
                //重新渲染
                option.series.data[0].value = Object.values(dataValues.noPretrained)
                option.series.data[1].value = Object.values(dataValues.PartFinetuned)
                option.series.data[2].value = Object.values(dataValues.AllFinetuned)
                option.radar.axisName.formatter =  (a) => {
                    let i = contains(list, a); // 处理对应要显示的样式
                    return `{a| ${a}} \n {b| ${option.series.data[0].value[i]}} \n {c| ${option.series.data[1].value[i]}} \n {d|${option.series.data[2].value[i]}}`;
                },
                myChart.setOption(
                    //将原先值给覆盖
                    option
                )
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            }
          
            // onUnmounted(()=>{
            //     myChart.destroy()
            // })
            return {
                radarEcharts,state,isDisabled,dataValues
            }
            
        }
       
    }


</script>

<style scoped>
    #radarEcharts{
        width: 100%;
        height: 100%;
        /* margin-left: 6px; */
        /* top: -2÷%; */
        padding-left: 6px;
        border-right: 1px solid rgb(188, 221, 246);
        background-color: #f7f8f8;
        /* box-shadow: 0px 10px 20px #9c886d66; */
        border-radius: 10px 0px 0px 10px;
        box-shadow: 10px 10px 20px #e4e1de66;
    }
    .no_chart{
        top:12%;
        height: 22%;
        width:  24.4%;
        position: absolute;
        z-index: 9;
        border: 0;
        background-color: #f7f8f8;
        opacity: 1;
        /* box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.16);  */
        cursor: no-drop;
        border-radius: 0 0 0 12px;
    }
</style>