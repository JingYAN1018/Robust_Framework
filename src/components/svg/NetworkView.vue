<template>
    <!-- <p >&nbsp;&nbsp; test</p> -->
    <div class="containerBox" style="display: flex; flex-direction: column;">
        <div class="sliderBox">
            <div class="params_1" style="display: flex;margin-left: 10px;">
                <span class="params_title" :style="{marginTop:(windowHeight <= '900'? '1.2%':'1.4%') }"
                    style="font-family:'Times New Roman', Times, serif;">weights</span>
                <NetworkSlider :maxVal="weightsMax" :minVal="weightsMin" :changeVal="weightsVal"
                    @onRangeChange="weightsChange" />
            </div>
            <div class="params_2" style="display:flex;margin-left: 20px;">
                <span class="params_title" :style="{marginTop:(windowHeight <= '900'? '1.2%':'1.4%') }"
                    style="font-family: fantasy;">阈值</span>
                <NetworkSlider :maxVal="yuzhiMax" :minVal="yuzhiMin" :changeVal="yuzhiVal"
                    @onRangeChange="yuzhiChange" />
            </div>
            <div class="params_3" style="margin-left:20px;">
                <el-switch v-model="methodName" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #86BEF9; --el-switch-off-color: #DBEBF3"
                    active-text="Relevant Model Slicing" inactive-text="Finetuned" @change='changeMethods' />
            </div>
            <!-- <div class="params_3" style="display:flex;margin-left: 20px;">
                <span class="params_title" :style="{width: 100+'px' , marginTop:(windowHeight <= '900'? '1%':'1.2%') }" style="font-family:'Times New Roman', Times, serif;">init_x_vel</span>
                <NetworkSlider/>
            </div> -->

        </div>

        <div class="modelSvg" style="flex:9.3;padding: 6px;display: flex;flex-direction: column;">

            <div class="network" style="flex:9.5;display: flex;">
                <!-- <svg ref="svg"></svg> -->
                <Network style="width:100%; height:100%" :dataset="datasetObj" :model="modelObj" :instance="instanceID"
                    :axisLeft=leftValue
                    :axisRight=rightValue
                    :weights=weightsVal
                    :yuzhi=yuzhiVal
                    :modelOptim=false
                    :methodName=false
                    @updateSlider="updateSliderVal" />
                <Network style="width:100%; height:100%" :dataset="datasetObj" :model="modelObj" :instance="instanceID"
                    :axisLeft=leftValue
                    :axisRight=rightValue
                    :weights=weightsVal
                    :yuzhi=yuzhiVal
                    :modelOptim=true 
                    :methodName="methodName" @updateSlider="updateSliderVal" />
            </div>
            <div class="AxisData">
                <el-slider class="self_slider" :max="max" :min="0" :marks="marks" v-model="axisValue" range
                    @input='changeLayer'>
                </el-slider>
            </div>
        </div>
    </div>



</template>

<script>
    // import * as d3 from 'd3'
    import NetworkSlider from '../slider/NetworkSlider.vue'
    import Network from '../echart/Network.vue'
    import { defineComponent, ref, reactive, computed, onMounted, watch, emit } from 'vue'

    export default defineComponent({
        name: 'NetworkView',
        components: {
            NetworkSlider, Network
        },
        props: {
            model: String,
            dataset: String,
            instance: Number
        },
        setup(props, { emit }) {
            var windowWidth = ref(document.body.clientWidth)
            var windowHeight = ref(document.body.clientHeight)
            var modelObj = ref(null)
            var datasetObj = ref(null)
            var instanceID = ref(null)
            modelObj.value = props.model
            datasetObj.value = props.dataset
            instanceID.value = props.instance
            // console.log("networkview--收到model--",modelObj.value)
            // console.log("networkview--收到dataset--",datasetObj.value)

            var leftValue = ref(0), rightValue = ref(3)
            var axisValue = ref([leftValue.value, rightValue.value])
            var max = ref(30)
            var marks = computed(() => {
                let marks = {};
                for (let i = 0; i <= parseInt(props.model.substring(6))+1; i++) {
                    marks[i] = i +"";
                }
                // console.log('marks--',marks)
                return marks;
            })
            const changeLayer = (obj) => {
                // console.log("obj--",obj)
                if (obj[1] - obj[0] > 3) {
                    rightValue.value = obj[1]
                    leftValue.value = obj[1] - 3
                    axisValue.value = [leftValue.value, rightValue.value]
                } else if (obj[1] - obj[0] <= 3) {
                    rightValue.value = obj[1]
                    leftValue.value = obj[0]
                    axisValue.value = [leftValue.value, rightValue.value]
                }
            }

            var weightsMin = ref(0), weightsMax = ref(100), weightsVal = ref(0)
            var yuzhiMin = ref(0), yuzhiMax = ref(100), yuzhiVal = ref(0)
            const weightsChange = (val) => {
                // console.log('父组件networkView收到变化的slider-weights',val)
                weightsVal.value = val
            }
            const yuzhiChange = (val) => {
                yuzhiVal.value = val
            }
            //这个方法是接收子组件Network传递的顶部滑块参数的最大值，目的是为了，底部控制网络层的滑块滑动之后，顶部滑块恢复到原位“最大值”
            const updateSliderVal = (val) => {
                // console.log('updateSliderVal-val--',val)
                weightsVal.value = val[0]
                yuzhiVal.value = val[1]
            }

            //定义params_3的响应式变量
            const methodName = ref(false)
            const changeMethods = (val) => {
                // console.log("changeMethods-val",val)
                // console.log("methodName--",methodName.value)
            }

            onMounted(() => {
                // marks = computed(() => {
                //     let marks = {};
                //     for (let i = 0; i <= parseInt(props.model.substring(6))+1; i++) {
                //         marks[i] = i + "";
                //     }
                //     return marks;
                // })
                // const width= 600, height= 400, thickness = 5
                // const svg =
                // draw(network,width,height,thickness)
            })
            watch(() => [props.model, props.dataset, props.instance], () => {
                modelObj.value = props.model
                datasetObj.value = props.dataset
                instanceID.value = props.instance
                max.value = parseInt(props.model.substring(6))+1
                axisValue.value = ([0, 3])
                // console.log("networkview--监听model--", modelObj.value)
                // console.log("networkview--监听dataset--",datasetObj.value)
                // console.log("networkview--监听instance--",instanceID.value)

            })
             return {
                windowWidth, windowHeight,
                axisValue, marks, max, changeLayer,
                datasetObj, modelObj, instanceID, leftValue, rightValue,
                weightsMax, weightsMin, yuzhiMax, yuzhiMin, weightsChange, yuzhiChange, weightsVal, yuzhiVal, updateSliderVal,
                methodName, changeMethods,
                // layout,draw,network,
            }

        },
    })

</script>

<style>
    .containerBox {
        display: flex;
    }

    .sliderBox {
        flex: 0.6;
        padding-top: 2px;
        /* background-color:#eef7e8; */
        /* border: 1px solid red; */
        width: 100%;
        height: 100%;
        display: flex;
        flex-wrap: wrap;
        /* justify-content:left; */
    }

    .sliderBox .params_title {
        display: inline-block;
        font-size: 14px;
        width: 70px;
        min-width: 30px;
        height: 15px;
        min-height: 8px;
        background-color: #DDEDF6;
        box-shadow: 1px 1px 3px #9c886d66;
        border: 2px #86BEF9 solid;
        padding-bottom: 4px;
        margin: 0 auto;
        text-align: center;
        border-radius: 20px 0 0 20px;

    }

    /* 定义weights,阈值的slider滑块 */
    .slider-demo-block {
        display: flex;
        align-items: flex-start;
        width: 26%;
    }

    .slider-demo-block .el-slider {
        flex: 3;
        margin-top: 0;
        margin-left: 12px;
    }

    .slider-demo-block .demonstration {
        font-size: 14px;
        font-weight: var(--el-button-font-weight);
        color: #5F6266;
        line-height: 29px;
        flex: 2;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-bottom: 0px;
        margin-left: 10px;
        background-color: #fff;
        border-radius: 4px;
        border: 2px solid #E2E6EB;
        text-align: center;
    }

    /* 定义svg顶部el-slider的滑条颜色 */
    .slider-demo-block .el-slider .el-slider__runway {
        background-color: #adb3b6;
    }

    /* 定义滑块params_3 */
    .el-switch__core {
        border-radius: 20px !important;
        height: 90% !important;
        border: 2px #86BEF9 solid !important;
        padding: 0px 10px !important;
    }

    .el-switch__core .el-switch__inner .is-icon,
    .el-switch__core .el-switch__inner .is-text {
        font-size: 14px !important;
        /* font-family: fantasy !important; */
        color: #111415 !important;
    }

    /* 定义滑块按钮 */
    .el-switch__core .el-switch__action {
        background: #EED57F !important;
        border: 2px solid #457DED !important;
        /* margin: 0px 6px 0px 6px !important; */
    }


    /* 定义svg底部的el-slider */
    .AxisData {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 25%;
        border: 1px solid #86BEF9;
        ;
        flex: 0.5;
        position: relative;
    }

    .AxisData .el-slider .self_slider {
        width: 100%;
    }

    /* 定义底部滑块的按钮 */
    .AxisData .el-slider .el-slider__runway {
        position: relative;
        width: 50%;
        height: 100%;
        bottom: 50%;
        border: none;
        background-color: transparent;
        background-image: linear-gradient(to right,
                rgb(142, 169, 250) 50%,
                transparent 50%);
        background-size: 0.5% 40%;
        background-position: bottom;
        background-repeat: repeat-x;
        margin: 0 8px 0 4px;
    }

    .AxisData .el-slider .el-slider__stop {
        position: relative;
        bottom: 0;
        height: 80%;
        width: 0.15%;
        border-radius: 0%;
        background: rgb(142, 169, 250);
    }

    .AxisData .el-slider .el-slider__runway .el-slider__marks-text {
        margin-top: 0;
        top: 100%;
        color: #3633d8;
        font-size: 90%;
    }

    .AxisData .el-slider .el-slider__runway .el-slider__button-wrapper {
        width: 1.5em;
        height: 1.5em;
        z-index: 1;
    }

    .AxisData .el-slider .el-slider__runway .el-slider__bar {
        background-color: transparent;
        background-image: linear-gradient(to right,
                rgb(248, 201, 61) 100%,
                transparent 30%);
        background-size: 2% 80%;
        background-position: bottom;
        background-repeat: repeat-x;
        top: 50%;
        height: 50%;
        width: 50%;
    }

    .AxisData .el-slider .el-slider__runway .el-slider__button-wrapper .el-slider__button {
        position: relative;
        left: 0;
        bottom: -190%;
        width: 100%;
        height: 100%;
        border: none;
        background-color: transparent;
        background-image: radial-gradient(#e47621 45%,
                transparent 45%,
                transparent 65%,
                #e47621 65%);
    }
</style>