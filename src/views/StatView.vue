<template>
  <div class="outbox">
    <!-- <br/><checkbox /> -->
    <div class="radarbox">
      <p class="radarbox_title">&nbsp;&nbsp;Model overview</p>
      <div class="buttonbox">
        <dropdown 
        :dataOptions=datasets 
        :titleVal=dataset_title 
        @itemEvent="dataSetEvent" 
        style="flex:1"
        />
        <dropdown 
          :dataOptions=models
          :titleVal=model_title 
          @itemEvent="modelEvent"
          style="flex:1"
          />
        <model-slider 
        :titleVal = pertu_title
        @updateValue = 'pertuEvent'
        style="flex:1"
        />
      </div>
      <div class="chartbox" style="flex:9; display: flex;">
        <radar-charts 
          :datasetName="datasetName"
          :modelName="modelName" 
          :pertuName="pertuName"
          style="flex:1.5;"
          />
          <line-charts 
          :datasetName="datasetName"
          :modelName="modelName" 
          style="flex:1;"/>
      </div>
     
    </div>
    <div class="modelbox">
      <p class="modelbox_title">&nbsp;&nbsp;Model similarity analysis view</p>
      <div class="buttonbox">
        <div class="checkbox" style="flex:1;display: flex;">
          <checkbox 
          :spantitle=bottomLayer_title
          @checkboxBtn = "bot_isDisabled"
          :botShow = bot_show
          :allShow = all_show
          />
          <checkbox 
            :spantitle=allLayer_title
            @checkboxBtn = all_isDisabled 
            :botShow = bot_show
            :allShow = all_show
            />
          
        </div>
        <div class="matrixbox" style="flex:9; position: relative;">
          <matrix-charts 
          id="matrix_1"
          :show = bot_show
          :datasetName = datasetName
          :layerName="bottomLayer_title"
          :modelName= modelName
          style="position: absolute;top:0;left:0;width: 100%;height: 100%;z-index: 1;"
          />
          <matrix-charts 
          id="matrix_2"
          :show = all_show
          :datasetName = datasetName
          :layerName="allLayer_title"
          :modelName= modelName
          style="position: absolute;top:-100;left:0;width: 100%;height: 100%;z-index:0;"
          />
        </div>
       
      </div>
        <!-- <div class="bottomLayerbox"> -->
         
         
        <!-- </div> -->
        <!-- <div class="allLayerbox"> -->
          
         
        <!-- </div> -->
    </div>
    <div class="instancebox">
      <p class="instancebox_title">&nbsp;&nbsp;Instances view</p>
      <div class="tablebox">
        <table-charts 
        @chooseObj="chooseInstance"  
        :datasetName="datasetName"
        :modelName="modelName" 
        :pertuName="pertuName"/>
      </div>

    </div>
  </div>
</template>

<script>
  import Dropdown from '../components/dropdown/Dropdown.vue'
  import Checkbox from '../components/checkbox/Checkbox.vue'
  import ModelSlider from '../components/slider/ModelSlider.vue'
  import { reactive, toRefs, toRef,ref,watch, inject, provide, } from "vue";
  import * as echarts from 'echarts';
  // import BarCharts from '../components/echart/BarCharts.vue'
  import RadarCharts from '../components/echart/RadarCharts.vue'
  import MatrixCharts from '../components/echart/MatrixCharts.vue'
  import TableCharts from '../components/echart/TableCharts.vue'
  import LineCharts from '../components/echart/LineCharts.vue'
  export default{
    name:"StatView",
    components:{
      Dropdown,Checkbox,RadarCharts,MatrixCharts,TableCharts,ModelSlider,LineCharts
    },
    props:{
      datasets: Object,
      models:Object,
      pertur_epsilons:Object
    },
    setup(props,{emit}){
        //自定义数据集下拉菜单
        let datasets = reactive({})
        datasets = props.datasets
        //自定义源模型下拉菜单
        let models = reactive({})
        models = props.models
        //自定义扰动参数
        let pertur_epsilons = reactive({})
        pertur_epsilons = props.pertur_epsilons

        //定义数据集按钮名称
        let dataset_title=ref("Target Dataset")
        //定义迁移学习源模型按钮名称
        let model_title = ref("Source Model")
        //定义扰动参数按钮名称
        let pertu_title = ref("Perturbation")

        //定义变量，用于兄弟组件radarCharts和matrixCharts使用兄弟组件dropdown传递的值
        let datasetName = ref()
        let modelName = ref()
        let pertuName = ref()
        //定义复选框名称
        let bottomLayer_title = ref("Bottom-Layer Feature")
        let allLayer_title = ref("All-Layer Feature")
        //定义响应式变量。控制混淆矩阵的显示与否
        let bot_show = ref(false)
        let all_show = ref(false)
      
        //接收子组件dropdown传递过来的值
        const dataSetEvent = (val)=>{
          // console.log('dataSetval---',val)
          datasetName.value = val.name
          emit('dataSetEvent',datasetName)
          // defineExpose({datasetName}) 
          // console.log("拿到了datasetItem--",datasetName.value)
        }
        
        const modelEvent=(val)=>{
          // console.log('modelval---',val)
          modelName.value = val.name
          
          emit('modelEvent',val)
          // defineExpose({modelName}) 
          // console.log("拿到了modelItem--",modelName.value)
        }

        const pertuEvent = (val)=>{
          // console.log("val--",val)
          pertuName.value = val
          // console.log("拿到了pertuItem--",pertuName.value)
        }
        const bot_isDisabled=(obj)=>{
          // console.log("obj--",obj)
          bot_show.value = obj.value
          
          if(bot_show.value){
            all_show.value = false
            document.getElementById('matrix_1').style.zIndex = 2;
          }else{
            all_show.value = true
            document.getElementById('matrix_1').style.zIndex = 0;
          }
          // console.log("bottom-bot_show--",bot_show)
          // console.log("bottom-all_show--",all_show)
        }
        const all_isDisabled=(obj)=>{
          all_show.value = obj.value
          if(all_show.value){
            bot_show.value = false
            document.getElementById('matrix_2').style.zIndex = 2;
          }else{
            bot_show.value = true
            document.getElementById('matrix_2').style.zIndex = 0;
          }
          // console.log("all_isDisabled--",obj)
          // console.log("all-bot_show--",bot_show)
          // console.log("all-all_show--",all_show)
        }
        
        const chooseInstance = (instance)=>{
          // console.log("父组件statview收到的instance",instance)
          emit('chooseInstance',instance)
        }
      
        return {
          datasets,dataset_title,datasetName,
          models,model_title,modelName,
          pertur_epsilons,pertu_title,pertuName,
          bottomLayer_title,allLayer_title,bot_show,all_show,
          dataSetEvent,modelEvent,pertuEvent,bot_isDisabled,all_isDisabled,
          chooseInstance
        }
    }
    
  }
</script>

<style>
  .outbox{
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    /* align-items: stretch; */
  }
  .outbox .radarbox{
    flex:3.6;
    /* background-color:#66f7bf; */
    display: flex;
    flex-direction: column;
    border-radius: 12px 0px 0px 12px;
    border-right: 1px solid #d9dbc4;
  }
  .outbox .modelbox{
    flex:2.4;
    /* background-color:#d3ac40; */
    /* border-radius: 0px 12px 12px 0px; */
    display: flex;
    flex-direction: column;
    border-right: 1px solid #d9dbc4;
  }
  .outbox .instancebox{
    flex:4;
    /* display: flex; */
    /* flex-direction: column; */
  }

  .radarbox .radarbox_title{
    flex: 0.2;
    background-color: #f1f5cd;
    opacity: .9;
    width: auto;
    height: 7%;
    font-size: 14px;
    font-family: fantasy;
    font-weight: bold;
    border-radius: 12px 0px 0px 0px;
    box-shadow: 2px 0px 2px 0 rgba(0, 0, 0, 0.16);
    z-index: 11;
  }
  
  .radarbox .buttonbox{
    flex:0.8;
    margin-top: 0.2%;
    /* background-color:#e3f3d7; */
    display: flex;
    justify-content:space-around;
  }

  .modelbox .modelbox_title{
    flex: 0.2;
    background-color: #f1f5cd;
    opacity: .9;
    width: auto;
    height: 7%;
    font-size: 14px;
    font-family: fantasy;
    font-weight: bold;
    box-shadow: 2px 0px 2px 0 rgba(0, 0, 0, 0.16);
    z-index: 11;
    /* border-radius: 0px 12px 0px 0px; */
  }

  .modelbox .buttonbox{
    flex:9.7; 
    height: 100%;
    display: flex;
    flex-direction: column;
    /* margin-top: 0.6%; */
    /* background-color:#89dc48; */
  }

  .instancebox .instancebox_title{
    /* flex: 0.2; */
    background-color: #f1f5cd;
    opacity: .9;
    width: auto;
    height: 7%;
    font-size: 14px;
    font-family: fantasy;
    font-weight: bold;
    box-shadow: 2px 0px 2px 0 rgba(0, 0, 0, 0.16);
    z-index: 11;
  }
  .instancebox .tablebox{
    /* flex:9.6;  */
    height: 86%;
    /* width: calc(97%); */
    margin-top: 0.3%;
    display: flex;
    background-color: #E3E8EC;
  }
  .resize-bar:hover ~ .resize-line,
.resize-bar:active ~ .resize-line {
    border-left: 1px dashed skyblue;
}
.resize-bar::-webkit-scrollbar {
    width: 200px; height: inherit;
}

</style>