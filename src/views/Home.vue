<template>
  <div class="container">
    <p class="title">
      A visual Framework for the Robustness and Security of Teacher/Student Models in the Visual Transfer Learning Process
      <span v-if="dataset && model">Target Dataset -> {{dataset}}&nbsp;&nbsp;&nbsp;&nbsp;Source Model -> {{model}}</span>
      <!-- <span v-if="model" style="float:right;padding-right: 20px;color: darkolivegreen;"></span> -->
    </p>
    <div class="statical-box" >
      <stat-view
      :datasets = datasets
      :models = models
      :pertur_epsilons = pertur_epsilons
      @dataSetEvent = datasetEven
      @modelEvent = modelEven
      @chooseInstance = instanceEven
      />
     
    </div>
    <div class="main-content">
      <div class="analysis-box">
        <analysis-view
        :dataset="dataset"
        :model="model"
        :instance="instance"/>
      </div>
      <div class="improve-box">
        <improve-view
        :dataset="dataset"
        :model="model"
        :instance="instance"/>
      </div>
    </div>
 
  </div>
</template>

<script>
  import StatView from '../views/StatView.vue'
  import AnalysisView from '../views/AnalysisView.vue'
  import ImproveView from '../views/ImproveView.vue'
  import {reactive,ref,inject,watch} from 'vue'
  export default{
    components:{
      StatView,
      AnalysisView,
      ImproveView
    },
    setup(){
      var datasets = reactive({})
      //在实际工作中，这个数据是通过axios回调.then()获得
      datasets =[{
        value:'flowers',
        numbers:12000,
        name:'flowers'
      },
      {
        value:'cars',
        numbers:12000,
        name:'cars'
      },
      {
        value:'food',
        numbers:12000,
        name:'food'
      },
      {
        value:'pets',
        numbers:12000,
        name:'pets'
      },
      {
        value:'nico',
        numbers:12000,
        name:'nico'
      },
      {
        value:'CIFAR',
        numbers:12000,
        name:'CIFAR'
      },
      {
        value:'alphabet',
        numbers:12000,
        name:'alphabet'
      }]

      //自定义下拉菜单
      var models = reactive({})
      //在实际工作中，这个数据是通过axios回调.then()获得
      models =[{
        value:'Resnet18',
        name:'Resnet18'
      },
      {
        value:'Resnet34',
        name:'Resnet34'
      },
      {
        value:'Resnet50',
        name:'Resnet50'
      },
      {
        value:'Resnet52',
        name:'Resnet152'
      },
      {
        value:'Resnet101',
        name:'Resnet101'
      },
      {
        value:'Resnet152',
        name:'Resnet152'
      }]
      
      //自定义扰动参数
      var pertur_epsilons = reactive({})
      pertur_epsilons = [{
        value: '扰动值0.01',
        name:'0.01'
      },{
        value: '扰动值0.15',
        name: '0.15'
      },{
        value: '扰动值0.2',
        name:'0.2'
      },{
        value: '扰动值0.25',
        name:'0.25'
      },{
        value: '扰动值0.3',
        name:'0.3'
      },{
        value: '扰动值0.35',
        name: '0.35'
      },{
        value: '扰动值0.4',
        name:'0.4'
      },{
        value: '扰动值0.5',
        name: '0.5'
      }]
      
      //定义响应式数据
      var dataset = ref('')
      var model = ref('')
      var instance = ref(null)
      
      const datasetEven = (val)=>{
        dataset.value = val.value
        // console.log("dataVal",val)
        // console.log("得到dataset",dataset.value)
      }
      const modelEven = (val)=>{
        model.value = val.value
        // console.log("得到model",model.value)
      }

      const instanceEven = (val)=>{
        //  console.log('父组件home收到的instance',val)
         instance.value = val
      }
      return {
        datasets,models,pertur_epsilons,
        dataset,model,instance,
        datasetEven,modelEven,instanceEven
      }
    }
  }
</script>


<style>
  *{
    margin:0;
    padding: 0;
  }
/* 
  div > .el-container {
    margin-bottom: 20px;
    height: 100%;
    width:100%;
  } */

  .container,body,html{
		height: auto;
    width:100%;
    background-color: #D3DCE6;
    font-size: 10px;
	}


  .title{
    /* background-color: rgb(125, 164, 235); */
    background: linear-gradient(to bottom, rgb(125, 164, 235), rgb(201, 216, 244));
    /* color: #fff; */
    height: 2.4vh;
    padding-left: 1%;
    font-family: fantasy;
    font-weight: bold;
    font-size:16px;
    line-height: 30px;
    padding-top: 2px;
    padding-bottom: 2px;
    /* resize: both;
    overflow: auto; */
    /* font-size: calc(0.5 * 5%); 相对字体大小 */
  }
  .title span{
    padding-top: 1px;
    float:right;
    padding-right: 20px;
    color: #2670a6;
    line-height: 30px;
    height: 2.4vh;
  }

  .statical-box{
    height: 30vh;
    margin:0.3%;
    border-radius: 12px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.16);
    background-color: #f7f8f8;
  }

  .main-content{
    display: flex;
    justify-content: center;
    align-items: stretch;
    height: 66vh;
    margin: 0.3%;
  }

  .analysis-box{
    flex: 7;
    border-radius: 12px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.16);
    margin-right:0.3%;
    background-color: #f7f8f8;
    display: flex;
    flex-direction: column;
  }

  .improve-box{
    flex: 3;
    border-radius: 12px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.16);
    background-color: #f7f8f8;
    display: flex;
    flex-direction: column;
  }


 .virtual_2{
  height: 50%;
  width: 29%;
  position: absolute;
  background-color: #f7f8f8;
  border: 0;
 }

</style>