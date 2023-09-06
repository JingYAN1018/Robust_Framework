<template>
    <!-- {{state.modelValue.value}} -->
    <!-- <input type="button" class="no_chart" v-show="isShow"> -->
    <div 
        ref="matrixEcharts" 
        id="matrixEcharts" 
        >
    </div>
</template>

<script>
    import * as echarts from 'echarts';
    import { ref, onMounted,reactive,watch,emit} from 'vue';
    import axios from 'axios';
    import * as d3 from 'd3';
    import heatmap from 'd3-heatmap';
    export default{
        name:'MatrixCharts',
        props:{
            //接收父组件传递过来的值
            datasetName:String,
            layerName:String,
            show:Boolean,
            modelName:String
        },
        setup(props,{emit}){
            
            //定义响应式变量，使用父组件传递过来的值
            const state = {
                datasetValue: ref(),
                modelValue: ref(),
                layerValue: ref()
            }
            state.datasetValue.value = props.datasetName
            state.modelValue.value = props.modelName
            state.layerValue.value = props.layerName
            //响应式变量 使用父组件传递的混淆矩阵显示与否
            //开始进入页面，混淆矩阵不显示。使用watch监听值的变化，点击复选框，再显示复选框
            //控制图表的显示,默认值true
            let isShow = ref(true)

            //创建一个初始化的二维数组，填充混淆矩阵
            let arr = reactive()
            arr= [
                    [0, 0, 8],
                    [0, 1, 8],
                    [0, 2, 8],
                    [1, 0, 8],
                    [1, 1, 8],
                    [1, 2, 8],
                    [2, 0, 8],
                    [2, 1, 8],
                    [2, 2, 8]
                ]
            let dataObj = ['AllFinetuned','PartFinetuned','noPretrained']
            const option = {
                // 表头
                // title:'model',
                //工具栏
                toolbox: {
                    itemSize: 10,
                    right: '20', // 工具栏距离容器右侧为 0
                    top: 'top', // 工具栏距离容器上侧为 0
                    feature: {//各工具配置项
                        saveAsImage: {},//保存为图片
                    },
                },
                // 提示框组件
                tooltip: {
                    position: 'top',
                    trigger: "item",//item数据项图形触发,axis坐标轴触发
                    formatter: function(params) {
                        // console.log(params)
                        // console.log(dataObj)
                        return dataObj[params.data[0]]+ '与' + dataObj[params.data[1]]+ '的相似度: ' + params.data[2];
                        // return '<img src=“/data/AllFinetuned_PartFinetuned_l3_n13.jpg”>'

                    }
                    // formatter: (params)=>{
                    //     console.log(params)
                    // }
                },
                animation: true,
                 //设置图表位置
                grid: {
                    height: '76%',
                    width:'74%',
                    // y: '4%',
                    left:'4%',
                    top:'6%',
                    containLabel: true,
                },
                xAxis: {
                    type: 'category',
                    data: dataObj,
                    boundaryGap: false,//坐标轴两边留白策略,默认为 true
                    // 坐标轴在 grid 区域中的分隔区域，默认不显示。
                    splitArea: {
                        show: true
                    },
                    splitLine: {
                        show: true
                    },
                    axisLabel: {
                        interval: 0, //横轴信息全部显示
                        rotate: 16, //倾斜度 -90 至 90 默认为0
                        margin: 5, //刻度标签与轴线之间的距离
                        textStyle: {
                            fontSize: 10  // 设置坐标轴标签字体大小为12
                        }
                    },
                    // 坐标轴两边留白策略
                    boundaryGap: ['20%', '20%']
                   
                },
                yAxis: {
                    type: 'category',
                    data: dataObj,
                    splitArea: {
                        show: false
                    },
                    splitLine: {
                        show: true
                    },
                    axisLabel: {
                        textStyle: {
                            fontSize: 10  // 设置坐标轴标签字体大小为12
                        }
                    },
                    // 坐标轴两边留白策略
                    boundaryGap: ['20%', '20%']
                },
                // 视觉映射组件，用于进行『视觉编码』，也就是将数据映射到视觉元素（视觉通道）。
                visualMap: {
                    name:'Similarity',
                    // 指定 visualMapContinuous 组件允许的最小值和最大值
                    min: 0,
                    max: 100,
                    // 是否显示拖拽用的手柄
                    calculable: true,
                    //拖拽时，是否实时更新
                    realtime:true,                          
                    orient: 'horizontal',
                    // left: 'right',
                    right:'16',
                    bottom: '0',
                    // marginBottom:'-10',
                    textStyle: {
                        fontSize: 10  // 设置坐标轴标签字体大小为10
                    },
                    // 定义图形的宽度
                    itemWidth:'10%',
                    // 定义 在选中范围中 的视觉元素。
                    inRange: {
                            // color:['#9DC0F8', '#80bfedff', '#3498DB'],
                            color:['#EEFAFF','#2493EA'],
                            opacity:'1',
                    },
                    //标签的格式化工具。
                    formatter: function (value) {       
                        // 范围标签显示内容。         
                        return value.toFixed(2);                   
                    }


                },
                series: [{
                    // 系列名称，用于tooltip的显示，legend 的图例筛选，
                    // 在 setOption 更新数据和配置项时用于指定对应的系列。
                    name: 'Model similarity',
                    type: 'heatmap',
                    // 鼠标悬浮到 visualMap 组件上时，鼠标位置对应的数值 在 图表中对应的图形元素，会高亮。
                    hoverLink: true,
                    data: arr,
                    label: {
                        show: true,
                        color: '#333',
                        fontSize: 10
                    },
                    itemStyle: {
                        borderWidth: 1,
                        borderColor: '#ccc',
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    
                }]
            }


            //获取图表实例
            const matrixEcharts = ref(null)
            // 定义绘制图表用的盒子
            let myChart = null
            
            onMounted(()=>{
                //设置绘制图表
                myChart=  echarts.init(matrixEcharts.value)
                // console.log('渲染')
                myChart.setOption(option)
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });

                // const margin = {top: 80, right: 25, bottom: 30, left: 40},
                // width = 450 - margin.left - margin.right,
                // height = 450 - margin.top - margin.bottom;

                // const svg = d3.select(matrixEcharts.value)
                //     .append("svg")
                //     .attr("width", width + margin.left + margin.right)
                //     .attr("height", height + margin.top + margin.bottom)
                //     .append("g")
                //     .attr("transform", `translate(${margin.left}, ${margin.top})`);

                //  //Read the data
                // d3.csv("https://raw.githubusercontent.com/holtzy/D3-graph-gallery/master/DATA/heatmap_data.csv").then(function(data) {

                //     // Labels of row and columns -> unique identifier of the column called 'group' and 'variable'
                //     const xGroups = Array.from(new Set(data.map(d => d.group1)))
                //     const yGroups = Array.from(new Set(data.map(d => d.group2)))
                //     // Build X scales and axis:
                //     const x = d3.scaleBand()
                //         .range([ 0, width ])
                //         .domain(xGroups)
                //         .padding(0.05);
                //     svg.append("g")
                //         .style("font-size", 15)
                //         .attr("transform", `translate(0, ${height})`)
                //         .call(d3.axisBottom(x).tickSize(0))
                //         .select(".domain").remove()

                //     // Build Y scales and axis:
                //     const y = d3.scaleBand()
                //         .range([ height, 0 ])
                //         .domain(yGroups)
                //         .padding(0.05);
                //     svg.append("g")
                //         .style("font-size", 15)
                //         .call(d3.axisLeft(y).tickSize(0))
                //         .select(".domain").remove()

                //     // Build color scale
                //     const myColor = d3.scaleSequential()
                //         .interpolator(d3.interpolateInferno)
                //         .domain([1,100])

                //     // create a tooltip
                //     const tooltip = d3.select("#my_dataviz")
                //         .append("div")
                //         .style("opacity", 0)
                //         .attr("class", "tooltip")
                //         .style("background-color", "white")
                //         .style("border", "solid")
                //         .style("border-width", "2px")
                //         .style("border-radius", "5px")
                //         .style("padding", "5px")

                //     // Three function that change the tooltip when user hover / move / leave a cell
                //     const mouseover = function(event,d) {
                //         tooltip
                //         .style("opacity", 1)
                //         d3.select(this)
                //         .style("stroke", "black")
                //         .style("opacity", 1)
                //     }
                //     const mousemove = function(event,d) {
                //         tooltip
                //         .html("The exact value of<br>this cell is: " + d.value)
                //         .style("left", (event.x)/2 + "px")
                //         .style("top", (event.y)/2 + "px")
                //     }
                //     const mouseleave = function(event,d) {
                //         tooltip
                //         .style("opacity", 0)
                //         d3.select(this)
                //         .style("stroke", "none")
                //         .style("opacity", 0.8)
                //     }
                //     // add the squares
                //     svg.selectAll()
                //         .data(data, function(d) {return d.group+':'+d.variable;})
                //         .join("rect")
                //         .attr("x", function(d) { return x(d.group) })
                //         .attr("y", function(d) { return y(d.variable) })
                //         .attr("rx", 4)
                //         .attr("ry", 4)
                //         .attr("width", x.bandwidth() )
                //         .attr("height", y.bandwidth() )
                //         .style("fill", function(d) { return myColor(d.value)} )
                //         .style("stroke-width", 4)
                //         .style("stroke", "none")
                //         .style("opacity", 0.8)
                //         .on("mouseover", mouseover)
                //         .on("mousemove", mousemove)
                //         .on("mouseleave", mouseleave)
                //     })

                //     // Add title to graph
                //     svg.append("text")
                //             .attr("x", 0)
                //             .attr("y", -50)
                //             .attr("text-anchor", "left")
                //             .style("font-size", "22px")
                //             .text("A d3.js heatmap");

                //     // Add subtitle to graph
                //     svg.append("text")
                //             .attr("x", 0)
                //             .attr("y", -20)
                //             .attr("text-anchor", "left")
                //             .style("font-size", "14px")
                //             .style("fill", "grey")
                //             .style("max-width", 400)
                //             .text("A short description of the take-away message of this chart.");
                //     console.log('svg--',svg)
                //     console.log(d3.select("#matrixEcharts"))
                
                
            });

            // 监听源模型modelName和目标数据集datasetName的变化
            watch(()=>[props.datasetName,props.modelName], (newValues, oldValues)=>{
                 // 注意：这里重新赋值的含义是，外层赋值得到值之后，就不再发生改变
                //所以当watch监听到父组件传递的值发生变化，需要重新再进行复制
                state.datasetValue.value = props.datasetName
                state.modelValue.value = props.modelName
                state.layerValue.value = props.layerName
                if(state.layerValue.value == 'Bottom-Layer Feature'){
                    axios.get('/statData/bottomLayerData.json').then((res)=>{
                        // console.log("res--",res)
                        // console.log("axios-props.datasetName--",props.datasetName)
                        // console.log("axios-props.modelName--",props.modelName)
                        for(var i=0; i<res.data.length;i++){
                            if(res.data[i].dataset_name == state.datasetValue.value && res.data[i].model_name == state.modelValue.value){
                                arr=[
                                    [0,0,100],
                                    [0,1,res.data[i].results[0]],
                                    [0,2,res.data[i].results[1]],
                                    [1,0,res.data[i].results[0]],
                                    [1,1,100],
                                    [1,2,res.data[i].results[2]],
                                    [2,0,res.data[i].results[1]],
                                    [2,1,res.data[i].results[2]],
                                    [2,2,100]
                                ]
                                break;
                            }
                        }
                        // console.log('arr--',arr)
                        updateChart()
                    }).catch((error)=>{
                        console.error("请求出错了：", error)
                    })

                }
                else if(state.layerValue.value == 'All-Layer Feature'){
                    axios.get('/statData/allLayerData.json').then((res)=>{
                        for(var i=0; i<res.data.length;i++){
                            if(res.data[i].dataset_name == state.datasetValue.value && res.data[i].model_name == state.modelValue.value){
                                arr=[
                                    [0,0,100],
                                    [0,1,res.data[i].results[0]],
                                    [0,2,res.data[i].results[1]],
                                    [1,0,res.data[i].results[0]],
                                    [1,1,100],
                                    [1,2,res.data[i].results[2]],
                                    [2,0,res.data[i].results[1]],
                                    [2,1,res.data[i].results[2]],
                                    [2,2,100]
                                ]
                            }
                        }
                        updateChart()
                    }).catch((error)=>{
                        console.error("请求出错了：", error)
                    })
                }
               
                
            },{deep:true})

            //监听多选框的事件
            watch(()=>props.show,(newValues,oldValues)=>{
                 //当有目标数据集和源模型时，只需要判断复选框的按钮是否显示
                 if(props.datasetName && props.modelName){
                    isShow.value = !props.show
                }
            },{deep:true})
           
            function updateChart(){
                //重新渲染
                //请注意，请注意，给图表系列赋值时，必须加上数组序号，因为series是对象数组
                option.series[0].data = arr

                // dataObj[params.data[0]]+ '与' + dataObj[params.data[1]]+ '的相似度: ' + params.data[2];
                myChart.setOption(
                    //将原先值给覆盖
                    option
                )
                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            }

            function showCells(params){
                // console.log(params)
            }
            return {
                matrixEcharts,isShow,arr,state,updateChart
            }
        }
    }
 
</script>

<style scoped>
#matrixEcharts{
    width: 100%;
    height: 100%;
    margin:auto;
    border: 1px solid rebeccapurple;
}
.no_chart{
    top: 8.8%;
    height: 26%;
    width: 17.6%;
    position: absolute;
    z-index: 9;
    border: 0;
    margin-left: 0;
    padding-left: 0;
    background-color: #a8ed88;
    background-color: #E9EEF3;

    border: 1px solid red;
    opacity: 1;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.16);
    cursor: no-drop;
    border-radius: 0 0 12px 0;
}
</style>