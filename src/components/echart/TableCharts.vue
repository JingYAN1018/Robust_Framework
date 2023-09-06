<template>
    <el-table 
    :data="tableData" 
    :table-layout="fixed" 
    highlight-current-row="true"
    ref="highlight"
    @row-dblclick="cancelHighlight"
    style="font-size: 10px;"
    width="100%"
    height="100%"
    :default-sort="{ prop: 'id', order: 'descending' }"
    :cell-style="{ textAlign: 'center' }"
    :header-cell-style="{ 'text-align': 'center' }"
    @row-click = 'chooseObj'
    > 
        <!-- <el-table s-column type="selection" width="30" /> -->

        <el-table-column prop="id" label="ID" width="70" fixed sortable >
            <template #default="scope">
                <span >{{ scope.row.id }}</span>
              </template>
        </el-table-column>
        <el-table-column prop="acc" label="Acc"  sortable  width="90">
            <template #default="scope">
                <span>{{ scope.row.acc}} 
                    <span :class="{ 'green-text': scope.row.remos_acc>=scope.row.acc,'red-text': scope.row.remos_acc<scope.row.acc}" >
                        ({{ scope.row.remos_acc }})
                    </span>
                </span>
              </template>
        </el-table-column>
        <el-table-column prop="recall" label="Recall" sortable width="90">
            <template #default="scope">
                <span>{{ scope.row.recall}} 
                    <!-- <span v-if="scope.row.remos_recall" :class="{ 'red-text': scope.row.remos_recall }"> -->
                        <span :class="{ 'green-text': scope.row.remos_recall>=scope.row.recall,'red-text': scope.row.remos_recall<scope.row.recall}" >
                    ({{ scope.row.remos_recall }})
                  </span>
                </span>
              </template>
        </el-table-column>
        <el-table-column prop="robust" label="Robust" sortable width="90">
            <template #default="scope">
                <span>{{ scope.row.robust}} 
                    <!-- <span v-if="scope.row.remos_robust" :class="{ 'red-text': scope.row.remos_recall }"> -->
                    <span v-if="scope.row.remos_robust" :class="{ 'green-text': scope.row.remos_robust>=scope.row.robust,'red-text': scope.row.remos_robust<scope.row.robust}" >
                    ({{ scope.row.remos_robust }})
                  </span>
                </span>
              </template>
        </el-table-column>
        <el-table-column prop="label" label="Label" fixed="right" width="94">
            <template #default="scope">
                <el-tag
                  :type="scope.row.label === scope.row.updated ? 'success' : 'warning'"
                  disable-transitions
                  >{{ scope.row.label }}</el-tag
                >
              </template>
        </el-table-column>
        <el-table-column prop="predicted" label="Predicted" fixed="right" width="94">
            <template #default="scope">
                <el-tag
                  :type="scope.row.predicted === scope.row.label ? 'success' : 'warning'"
                  disable-transitions
                  >{{ scope.row.predicted }}</el-tag
                >
              </template>
        </el-table-column>
        <el-table-column prop="updated" label="Updated" fixed="right" width="94">
            <template #default="scope">
                <el-tag
                  :type="scope.row.updated === scope.row.label ? 'success' : 'warning'"
                  disable-transitions
                  >{{ scope.row.updated }}</el-tag
                >
              </template>
        </el-table-column>
    </el-table>
</template>
<script>
    import { ref, onMounted,reactive,watch,emit,nextTick} from 'vue';
    import { TableColumnCtx } from 'element-plus'
    import axios from 'axios'
    
    export default{
        
        name:"TableCharts",
        props:{
            datasetName:String,
            modelName:String,
            pertuName:Number,
        },
        setup(props,{emit}){
           
            // var tableLayout = ref('fixed')
            var tableData = ref([])
            //取消当前行高亮显示
            var highlight = ref(null)
            const cancelHighlight =(row) =>{
                // console.log(row)
                highlight.value.setCurrentRow(null);
                // console.log('highlight--',highlight)
            }
            var instanceID = ref(null)
            const chooseObj = (row) =>{
                // console.log("选择表格obj--",row)
                instanceID.value = row.id
                // console.log('选择的instanceID--',instanceID.value)
                emit('chooseObj',instanceID.value)
            }
            var datasetVal = ref()
            datasetVal.value = props.datasetName
            var modelVal = ref()
            modelVal.value = props.modelName
            var pertuVal = ref()
            pertuVal.value = props.pertuName
            watch(()=>[props.datasetName,props.modelName,props.pertuName],()=>{
                datasetVal.value = props.datasetName
                modelVal.value = props.modelName
                pertuVal.value = props.pertuName
                // console.log("table接收dataset---",datasetVal.value)
                // console.log("table接收model---",modelVal.value)
                // console.log("table接收pertuval---",pertuVal.value)
                if(datasetVal.value && modelVal.value && pertuVal.value){
                    axios.get('/statData/InstanceData.json').then(res=>{
                        // console.log('instance的res--',res)
                        for(let i=0;i<res.data.length;i++){
                            if(datasetVal.value == res.data[i].dataset_name && modelVal.value == res.data[i].model_name &&res.data[i].pertu_name[0]<= pertuVal.value && pertuVal.value< res.data[i].pertu_name[1]){
                                // console.log('res.data[i].pertu_name[1]--',res.data[i].pertu_name[1])
                                tableData.value = res.data[i].data
                                // console.log("tableData--",tableData)
                                break;
                            }
                        }
                    }).catch((error)=>{
                        console.error("请求出错了：", error)
                    })
                }
                
        },{deep:true})
        return{
            tableData,highlight,cancelHighlight,chooseObj,instanceID,
            datasetVal,modelVal,pertuVal,
        }
        }
    }
</script>

<style>
.red-text{
    color: red;
}
.green-text{
    color:#67c23aff;
}
.el-table {
    position: relative;
    overflow: hidden;
    box-sizing: border-box;
    height: -moz-fit-content;
    height: fit-content;
    width: 100%;
    max-width: 100%;
    /* background-color: var(--el-table-bg-color); */
    font-size: 14px;
    color: var(--el-table-text-color);
}
.el-table .el-table__cell {
    padding: 3px 0 !important;
}
.el-table__header th {
  background-color: #ddecf9 !important;
}
.el-table__body td {
  background-color: #f7f8f8 !important;
}
.el-table--striped .el-table__body tr.el-table__row--striped.current-row td, .el-table__body tr.current-row>td  {
    color: #fff;
    background-color: #a7cdf9!important;
}

</style>