<template>
    <p class="analysis_title">&nbsp;&nbsp; Model Feature view</p>
    <!-- <canvas id="analysis_view"></canvas> -->
    <!-- <div  @click="show_1()" style="flex:9.7"> -->
        <!-- <input class="virtual_1" type="button" v-show="isShow_1" /> -->
        <!-- <img src="../assets/layerFeature.png" style="width:40%;height: 30%;margin:3% 4%;"/> -->
        <!-- <img src="../assets/neuronWeight.png" style="width:40%;height: 30%;"/> -->
        <!-- <img src="../assets/ReMos.png" style="width: 80%;height: 80%;margin-top: 2%;margin-left: 6%;"/> -->
    <!-- </div> -->
    <network-view style="flex:9.7;" :model="modelObj" :dataset="datasetObj" :instance="instanceID"/>

</template>

<script>
    import * as d3 from 'd3'
    import NetworkView from '../components/svg/NetworkView.vue'
    import {ref,onMounted,watch} from 'vue'
    export default {
        name:'AnalysisView',
        props:{
            dataset:String,
            model:String,
            instance:Number
        },
        components:{
            NetworkView
        },
        setup(props){
            var is_show = ref(true)
            const show_1 = ()=>{
                is_show.value = !is_show.value
            }

            //接收父组件传递的响应式变量
            var datasetObj = ref(null)
            var modelObj = ref(null)
            var instanceID = ref(null)
            datasetObj.value = props.dataset
            modelObj.value = props.model
            instanceID.value = props.instance

            onMounted(()=>{
                 
            })

            watch(()=>[props.dataset,props.model,props.instance],()=>{
                datasetObj.value = props.dataset
                modelObj.value = props.model
                instanceID.value = props.instance
                // console.log('AnalysisView监听父组件传递的dataset--',datasetObj.value)
                // console.log('AnalysisView监听父组件传递的model--',modelObj.value)
                // console.log('AnalysisView监听父组件传递的instance--',instanceID.value)
            })
            return {
                datasetObj,modelObj,instanceID
            }
        }

    }
</script>

<style>
    .analysis_title{
        flex:0.2;
        height: 7%;
        background-color: #f1f5cd;
        opacity: .9;
        width: auto;
        font-size: 14px;
        font-family: fantasy;
        font-weight: bold;
        box-shadow: 2px 0px 2px 0 rgba(0, 0, 0, 0.16);
        z-index: 11;
    }
    .virtual_1{
    height: 52%;
    width: 62%;
    position: absolute;
    background-color: #f7f8f8;
    border: 0;
    }
</style>