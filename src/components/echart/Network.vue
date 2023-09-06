<template>
    <div id="container" ref="container"></div>
    <!-- <svg ref="container" id="container" width="960" height="500"></svg> -->
</template>

<script>
    import { ref, reactive, computed, onMounted, watch, emit } from 'vue'
    import axios from 'axios';
    import * as echarts from 'echarts';
    export default {
        name: 'Network',
        props: {
            dataset: String,
            model: String,
            instance: Number,
            axisLeft: Number,
            axisRight: Number,
            weights: Number,
            yuzhi: Number,
            methodName: Boolean,
            modelOptim: Boolean
        },
        setup(props, { emit }) {
            //获取响应式实例
            const container = ref(null)
            var datasetObj = ref(null)
            var modelObj = ref(null)
            var instanceID = ref(null)
            datasetObj.value = props.dataset
            modelObj.value = props.model.substring(3)
            instanceID.value = props.instance
            // #f6f1c8('network收到父组件networkview传递的instance',instanceID.value)

            var layerLeft = ref(0), layerRight = ref(0)
            layerLeft.value = props.axisLeft
            layerRight.value = props.axisRight
            // console.log('network收到父组件networkview传递的axisLeft',layerLeft.value)
            // console.log('network收到父组件networkview传递的axisRight',layerRight.value)

            //接收父组件传递的params_3中滑块的属性
            var methodVal = ref(false)
            methodVal.value = props.methodName

            // 定义绘制图表用的盒子
            let myChart = null
            // let finetunedColors = ['#5da8f7ff', '#4d9ef5ff', '#1a75ebff','#987ef7ff','#7672f7ff'];
            // let finetunedColors = ['#6B76FF', '#26BAEE', '#3D6CB9', '#A3D8F4', '#82ACFF']
            let finetunedColors = ['#99CCFF', '#66B2FF', '#3399FF', '#85B6FF', '#57A0FF']
            let slicingColors = ['#FFAB73', '#FDA769', '#F49D1A', '#F7A76C', '#FFC288'];
            let sourceModelColors = ['#FF8000', '#FFB366', '#FFAC4D', '#FFB570', '#FFCE9F']
            const option = {
                tooltip: {
                    trigger: 'item',
                    triggerOn: 'mousemove',
                    formatter: (params) => {
                        // console.log(params)
                        let sourceText = ''
                        let targetText = ''
                        let nodeText = ''
                        if (params.data.source && params.data.target) {
                            sourceText = params.data.source
                            targetText = params.data.target
                            return sourceText + "--" + targetText + ":" + "&nbsp;" + params.value
                        } else if (params.data.name) {
                            nodeText = params.data.name
                            return nodeText + ":" + "&nbsp;" + params.value
                        }
                    }
                },
                series: [
                    {
                        type: 'sankey',
                        left: 100,
                        top: 20,
                        right: 20,
                        bottom: 20,
                        //布局的迭代次数，目的是不断迭代优化图中节点和边的位置，以减少节点和边之间的相互遮盖，默认值是 32。
                        //如果希望图中节点的顺序是按照原始 data 中的顺序排列的，可设该值为 0。
                        layoutIterations: 0,
                        nodeWidth: 10,
                        nodeGap: 30,
                        data: [
                            {
                                name: 'neuron_L11',
                                itemStyle: {
                                    color: '#f18bbf',
                                    borderColor: '#f18bbf'
                                }
                            },
                            {
                                name: 'neuron_L12',
                                itemStyle: {
                                    color: '#0078D7',
                                    borderColor: '#0078D7'
                                }
                            },
                            {
                                name: 'neuron_L13',
                                itemStyle: {
                                    color: '#3891A7',
                                    borderColor: '#3891A7'
                                }
                            },
                            {
                                name: 'neuron_L14',
                                itemStyle: {
                                    color: '#0037DA',
                                    borderColor: '#0037DA'
                                }
                            },
                            {
                                name: 'neuron_L15',
                                itemStyle: {
                                    color: '#C0BEAF',
                                    borderColor: '#C0BEAF'
                                }
                            },
                            {
                                name: 'neuron_L16',
                                itemStyle: {
                                    color: '#EA005E',
                                    borderColor: '#EA005E'
                                }
                            },
                            {
                                name: 'neuron_L21',
                                itemStyle: {
                                    color: '#D13438',
                                    borderColor: '#D13438'
                                }
                            },
                            {
                                name: 'neuron_L22',
                                itemStyle: {
                                    color: '#567C73',
                                    borderColor: '#567C73'
                                }
                            },
                            {
                                name: 'neuron_L23',
                                itemStyle: {
                                    color: '#9ed566',
                                    borderColor: '#9ed566'
                                }
                            },
                            {
                                name: 'neuron_L24',
                                itemStyle: {
                                    color: '#2BCC7F',
                                    borderColor: '#2BCC7F'
                                }
                            },
                            {
                                name: 'neuron_L25',
                                itemStyle: {
                                    color: '#809B48',
                                    borderColor: '#809B48'
                                }
                            },
                            {
                                name: 'neuron_L26',
                                itemStyle: {
                                    color: '#9B2D1F',
                                    borderColor: '#9B2D1F'
                                }
                            },
                            {
                                name: 'neuron_L27',
                                itemStyle: {
                                    color: '#604878',
                                    borderColor: '#604878'
                                }
                            },
                            {
                                name: 'neuron_L28',
                                itemStyle: {
                                    color: '#A5644E',
                                    borderColor: '#A5644E'
                                }
                            },
                            {
                                name: 'neuron_L29',
                                itemStyle: {
                                    color: '#2D3F3A',
                                    borderColor: '#2D3F3A'
                                }
                            },
                            {
                                name: 'neuron_L210',
                                itemStyle: {
                                    color: '#761721',
                                    borderColor: '#761721'
                                }
                            },
                            {
                                name: 'neuron_L31',
                                itemStyle: {
                                    color: '#B1BADD',
                                    borderColor: '#B1BADD'
                                }
                            },
                            {
                                name: 'neuron_L32',
                                itemStyle: {
                                    color: '#B0CCB0',
                                    borderColor: '#B0CCB0'
                                }
                            },
                            {
                                name: 'neuron_L33',
                                itemStyle: {
                                    color: '#8164A3',
                                    borderColor: '#8164A3'
                                }
                            },
                            {
                                name: 'neuron_L34',
                                itemStyle: {
                                    color: '#8E562E',
                                    borderColor: '#8E562E'
                                }
                            },
                            {
                                name: 'neuron_L35',
                                itemStyle: {
                                    color: '#C1504D',
                                    borderColor: '#C1504D'
                                }
                            },
                            {
                                name: 'neuron_L36',
                                itemStyle: {
                                    color: '#CCAF0A',
                                    borderColor: '#CCAF0A',
                                }
                            },
                            {
                                name: 'neuron_L37',
                                itemStyle: {
                                    color: '#956251',
                                    borderColor: '#956251'
                                }
                            },
                            {
                                name: 'neuron_L38',
                                itemStyle: {
                                    color: '#C17529',
                                    borderColor: '#C17529'
                                }
                            },
                            {
                                name: 'neuron_L39',
                                itemStyle: {
                                    color: '#CEC597',
                                    borderColor: '#CEC597'
                                }
                            },
                            {
                                name: 'neuron_L310',
                                itemStyle: {
                                    color: '#9F2936',
                                    borderColor: '#9F2936'
                                }
                            },
                            {
                                name: 'neuron_L311',
                                itemStyle: {
                                    color: 'rgba(128,155,72,255)',
                                    borderColor: 'rgba(128,155,72,255)'
                                }
                            },
                            //
                            {
                                name: 'neuron_L312',
                                itemStyle: {
                                    color: '#ac7430',
                                    borderColor: '#ac7430'
                                }
                            },
                            {
                                name: 'neuron_L41',
                                itemStyle: {
                                    color: '#00BCF2',
                                    borderColor: '#00BCF2'
                                }
                            },
                            {
                                name: 'neuron_L42',
                                itemStyle: {
                                    color: '#CD7B38',
                                    borderColor: '#CD7B38'
                                }
                            },
                            {
                                name: 'neuron_L43',
                                itemStyle: {
                                    color: '#424242',
                                    borderColor: '#424242'
                                }
                            },
                            {
                                name: 'neuron_L44',
                                itemStyle: {
                                    color: '#f63185',
                                    borderColor: '#f63185'
                                }
                            },
                            {
                                name: 'neuron_L45',
                                itemStyle: {
                                    color: '#9CBC59',
                                    borderColor: '#9CBC59'
                                }
                            },
                            {
                                name: 'neuron_L46',
                                itemStyle: {
                                    color: '#4F4BD9',
                                    borderColor: '#4F4BD9'
                                }
                            },
                            {
                                name: 'neuron_L47',
                                itemStyle: {
                                    color: '#3EC562',
                                    borderColor: '#3EC562'
                                }
                            },
                            {
                                name: 'neuron_L48',
                                itemStyle: {
                                    color: '#F06F2E',
                                    borderColor: '#F06F2E'
                                }
                            },
                            {
                                name: 'neuron_L51',
                                itemStyle: {
                                    color: '#C3986D',
                                    borderColor: '#C3986D'
                                }
                            },
                            {
                                name: 'neuron_L52',
                                itemStyle: {
                                    color: '#4D291C',
                                    borderColor: '#4D291C'
                                }
                            },
                            {
                                name: 'neuron_L53',
                                itemStyle: {
                                    color: '#009c7a',
                                    borderColor: '#009c7a'
                                }
                            },
                            {
                                name: 'neuron_L54',
                                itemStyle: {
                                    color: '#986F0B',
                                    borderColor: '#986F0B'
                                }
                            },
                            {
                                name: 'neuron_L55',
                                itemStyle: {
                                    color: '#3C8EA4',
                                    borderColor: '#3C8EA4'
                                }
                            },
                            {
                                name: 'neuron_L56',
                                itemStyle: {
                                    color: '#4F82BE',
                                    borderColor: '#4F82BE'
                                }
                            },

                        ],
                        links: [
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L21',
                                value: 1
                            },
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L22',
                                value: 3
                            },
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L23',
                                value: 4
                            },
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L24',
                                value: 5
                            },

                            {
                                source: 'neuron_L11',
                                target: 'neuron_L25',
                                value: 8
                            },
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L26',
                                value: 9
                            },

                            {
                                source: 'neuron_L11',
                                target: "neuron_L27",
                                value: 12
                            },
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L28',
                                value: 13
                            },
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L29',
                                value: 14
                            },
                            {
                                source: 'neuron_L11',
                                target: 'neuron_L210',
                                value: 15
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L21',
                                value: 16
                            },

                            {
                                source: 'neuron_L12',
                                target: 'neuron_L22',
                                value: 20
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L23',
                                value: 21
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L24',
                                value: 22
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L25',
                                value: 23
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L26',
                                value: 24
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L27',
                                value: 25
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L28',
                                value: 26
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L29',
                                value: 27
                            },
                            {
                                source: 'neuron_L12',
                                target: 'neuron_L210',
                                value: 28
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L21',
                                value: 29
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L22',
                                value: 30
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L23',
                                value: 31
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L24',
                                value: 32
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L25',
                                value: 33
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L26',
                                value: 34
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L27',
                                value: 35
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L28',
                                value: 36
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L29',
                                value: 37
                            },
                            {
                                source: 'neuron_L13',
                                target: 'neuron_L210',
                                value: 38
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L21',
                                value: 39
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L22',
                                value: 40
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L23',
                                value: 41
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L24',
                                value: 42
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L25',
                                value: 43
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L26',
                                value: 44
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L27',
                                value: 45
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L28',
                                value: 46
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L29',
                                value: 47
                            },
                            {
                                source: 'neuron_L14',
                                target: 'neuron_L210',
                                value: 48
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L21',
                                value: 49
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L22',
                                value: 50
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L23',
                                value: 51
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L24',
                                value: 52
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L26',
                                value: 53
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L27',
                                value: 54
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L28',
                                value: 55
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L29',
                                value: 56
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L210',
                                value: 57
                            },
                            {
                                source: 'neuron_L15',
                                target: 'neuron_L25',
                                value: 58
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L21',
                                value: 59
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L22',
                                value: 60
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L23',
                                value: 2
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L24',
                                value: 6
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L25',
                                value: 7
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L26',
                                value: 10
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L27',
                                value: 11
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L28',
                                value: 17
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L29',
                                value: 18
                            },
                            {
                                source: 'neuron_L16',
                                target: 'neuron_L210',
                                value: 19
                            },


                            {
                                source: 'neuron_L21',
                                target: 'neuron_L31',
                                value: 1
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L32',
                                value: 2
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L33',
                                value: 3
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L34',
                                value: 4
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L35',
                                value: 5
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L36',
                                value: 6
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L37',
                                value: 7
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L38',
                                value: 8
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L39',
                                value: -2
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L310',
                                value: -8
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L311',
                                value: 9
                            },
                            {
                                source: 'neuron_L21',
                                target: 'neuron_L312',
                                value: 10
                            },
                            {
                                source: 'neuron_L22',
                                target: 'neuron_L33',
                                value: -4
                            },
                            {
                                source: 'neuron_L22',
                                target: 'neuron_L38',
                                value: -11
                            },
                            {
                                source: 'neuron_L22',
                                target: 'neuron_L310',
                                value: -12
                            },

                            {
                                source: 'neuron_L22',
                                target: 'neuron_L312',
                                value: 11
                            },
                            {
                                source: 'neuron_L23',
                                target: 'neuron_L32',
                                value: 12
                            },
                            {
                                source: 'neuron_L23',
                                target: 'neuron_L34',
                                value: 13
                            },
                            {
                                source: 'neuron_L23',
                                target: 'neuron_L38',
                                value: 18
                            },
                            {
                                source: 'neuron_L24',
                                target: 'neuron_L39',
                                value: 14
                            },
                            {
                                source: 'neuron_L24',
                                target: 'neuron_L31',
                                value: 15
                            },
                            {
                                source: 'neuron_L24',
                                target: 'neuron_L310',
                                value: 16
                            },
                            {
                                source: 'neuron_L25',
                                target: 'neuron_L35',
                                value: -20
                            },
                            {
                                source: 'neuron_L25',
                                target: 'neuron_L31',
                                value: -23
                            },
                            {
                                source: 'neuron_L25',
                                target: 'neuron_L39',
                                value: 17
                            },
                            {
                                source: 'neuron_L26',
                                target: 'neuron_L32',
                                value: 21
                            },
                            {
                                source: 'neuron_L26',
                                target: 'neuron_L33',
                                value: 22
                            },
                            {
                                source: 'neuron_L27',
                                target: 'neuron_L31',
                                value: 23
                            },
                            {
                                source: 'neuron_L27',
                                target: 'neuron_L33',
                                value: 24
                            },

                            {
                                source: 'neuron_L28',
                                target: 'neuron_L34',
                                value: 25
                            },
                            {
                                source: 'neuron_L29',
                                target: 'neuron_L36',
                                value: -16
                            },
                            {
                                source: 'neuron_L29',
                                target: 'neuron_L39',
                                value: 27
                            },
                            {
                                source: 'neuron_L210',
                                target: 'neuron_L32',
                                value: -10
                            },
                            {
                                source: 'neuron_L210',
                                target: 'neuron_L38',
                                value: -11
                            },
                            {
                                source: 'neuron_L31',
                                target: 'neuron_L42',
                                value: 28
                            },
                            {
                                source: 'neuron_L32',
                                target: 'neuron_L41',
                                value: 29
                            },
                            {
                                source: 'neuron_L33',
                                target: 'neuron_L44',
                                value: 30
                            },
                            {
                                source: 'neuron_L34',
                                target: 'neuron_L42',
                                value: 1
                            },
                            {
                                source: 'neuron_L34',
                                target: 'neuron_L45',
                                value: 2
                            },
                            {
                                source: 'neuron_L34',
                                target: 'neuron_L48',
                                value: 3
                            },
                            {
                                source: 'neuron_L35',
                                target: 'neuron_L43',
                                value: 4
                            },
                            {
                                source: 'neuron_L35',
                                target: 'neuron_L47',
                                value: 5
                            },
                            {
                                source: 'neuron_L35',
                                target: 'neuron_L41',
                                value: 6
                            },
                            {
                                source: 'neuron_L36',
                                target: 'neuron_L41',
                                value: 7
                            },
                            {
                                source: 'neuron_L36',
                                target: 'neuron_L45',
                                value: 8
                            },
                            {
                                source: 'neuron_L36',
                                target: 'neuron_L47',
                                value: 10
                            },
                            {
                                source: 'neuron_L36',
                                target: 'neuron_L48',
                                value: 11
                            },
                            {
                                source: 'neuron_L37',
                                target: 'neuron_L41',
                                value: -16
                            },
                            {
                                source: 'neuron_L37',
                                target: 'neuron_L42',
                                value: -13
                            },
                            {
                                source: 'neuron_L37',
                                target: 'neuron_L47',
                                value: 12
                            },
                            {
                                source: 'neuron_L38',
                                target: 'neuron_L41',
                                value: -21
                            },
                            {
                                source: 'neuron_L38',
                                target: 'neuron_L42',
                                value: 13
                            },
                            {
                                source: 'neuron_L38',
                                target: 'neuron_L45',
                                value: 14
                            },
                            {
                                source: 'neuron_L39',
                                target: 'neuron_L46',
                                value: 15
                            },
                            {
                                source: 'neuron_L310',
                                target: 'neuron_L43',
                                value: 16
                            },
                            {
                                source: 'neuron_L311',
                                target: 'neuron_L44',
                                value: 17
                            },
                            {
                                source: 'neuron_L312',
                                target: 'neuron_L47',
                                value: -20
                            },
                            {
                                source: 'neuron_L311',
                                target: 'neuron_L42',
                                value: 19
                            },
                            {
                                source: 'neuron_L312',
                                target: 'neuron_L44',
                                value: 20
                            },
                            {
                                source: 'neuron_L312',
                                target: 'neuron_L46',
                                value: 21
                            },
                            {
                                source: 'neuron_L41',
                                target: 'neuron_L51',
                                value: 1
                            },
                            {
                                source: 'neuron_L41',
                                target: 'neuron_L52',
                                value: 2
                            },
                            {
                                source: 'neuron_L41',
                                target: 'neuron_L53',
                                value: 3
                            },
                            {
                                source: 'neuron_L41',
                                target: 'neuron_L55',
                                value: 4
                            },
                            {
                                source: 'neuron_L42',
                                target: 'neuron_L52',
                                value: 5
                            },
                            {
                                source: 'neuron_L42',
                                target: 'neuron_L56',
                                value: 6
                            },
                            {
                                source: 'neuron_L43',
                                target: 'neuron_L51',
                                value: 7
                            },
                            {
                                source: 'neuron_L43',
                                target: 'neuron_L53',
                                value: 8
                            },
                            {
                                source: 'neuron_L43',
                                target: 'neuron_L54',
                                value: 9
                            },
                            {
                                source: 'neuron_L44',
                                target: 'neuron_L52',
                                value: 10
                            },
                            {
                                source: 'neuron_L44',
                                target: 'neuron_L53',
                                value: 11
                            },
                            {
                                source: 'neuron_L44',
                                target: 'neuron_L56',
                                value: 12
                            },
                            {
                                source: 'neuron_L45',
                                target: 'neuron_L54',
                                value: 13
                            },
                            {
                                source: 'neuron_L46',
                                target: 'neuron_L53',
                                value: -9
                            },
                            {
                                source: 'neuron_L47',
                                target: 'neuron_L51',
                                value: 14
                            },
                            {
                                source: 'neuron_L48',
                                target: 'neuron_L56',
                                value: -7
                            },
                            {
                                source: 'neuron_L48',
                                target: 'neuron_L51',
                                value: 15
                            },
                            {
                                source: 'neuron_L47',
                                target: 'neuron_L52',
                                value: 16
                            },
                            {
                                source: 'neuron_L47',
                                target: 'neuron_L55',
                                value: -3
                            },
                            {
                                source: 'neuron_L46',
                                target: 'neuron_L54',
                                value: 17
                            },
                            {
                                source: 'neuron_L46',
                                target: 'neuron_L56',
                                value: 18
                            },
                            {
                                source: 'neuron_L45',
                                target: 'neuron_L53',
                                value: -19
                            }
                        ],
                        lineStyle: {
                            color: 'gradient',
                            curveness: 0.5
                        },
                        itemStyle: {
                            color: '#1f77b4',
                            borderColor: '#1f77b4'
                        },
                        label: {
                            color: 'rgba(0,0,0,0.7)',
                            fontFamily: 'Arial',
                            fontSize: 10,
                            position: "left",
                            formatter: '{b}'
                        },
                        // nodeAlign:'left',
                        levels: [{
                            depth: 4,
                            itemStyle: {
                                color: '#fbb4ae'
                            },
                            lineStyle: {
                                color: 'source',
                                opacity: 0.6
                            }
                        }, {
                            depth: 5,
                            itemStyle: {
                                color: '#b3cde3'
                            },
                            lineStyle: {
                                color: 'source',
                                opacity: 0.6
                            }
                        }],
                        emphasis: {
                            focus: 'none',
                            blurScope: 'coordinateSystem'
                        }
                    }
                ]
            };
            onMounted(() => {
                //设置绘制图表
                myChart = echarts.init(container.value)
                myChart.setOption(option)


                //监听窗口大小变化事件
                window.addEventListener("resize", function () {
                    myChart.resize();
                });
            })
            // 封装神经元点击展示特征图的函数
            // 左边预训练模型（源模型）网络关系图
            function clickPretrainModel(dom) {
                dom.on('click', (e) => {
                    console.log('点击事件', e)
                    if (e.dataType === 'node') {

                        var dialog = document.getElementById(`img_dialog_${e.data.name}_${e.data.value}`);
                        if (dialog) {
                            console.log('获取图片dom', dialog)
                            // 第三次点击，需要重新显示弹出框
                            if (dialog.style.display === 'none') {
                                dialog.style.display = 'block'
                                return
                            } else { // 第二次点击，若存在弹出框，需要隐藏弹出框
                                dialog.style.display = 'none';
                                return
                            }
                        }

                        // 第一次点击时，创建对话框
                        var dialog = document.createElement('div');
                        dialog.setAttribute('id', `img_dialog_${e.data.name}_${e.data.value}`)
                        dialog.style.position = 'fixed';
                        // dialog.style.top = '50%';
                        dialog.style.left = eval(`9+${e.data.depth}*13`) + '%';
                        // 找到神经元的纵向顺序
                        var underStr = e.data.name.indexOf("_", e.data.name.indexOf('_') + 1)
                        var index = e.data.name.substr(underStr + 1)
                        dialog.style.bottom = eval(`50-${parseInt(index)}*4`) + '%';
                        // dialog.style.transform = 'translate(-50%, -50%)';
                        dialog.style.width = '100px';
                        dialog.style.height = '100px';
                        dialog.style.backgroundColor = '#fff';
                        dialog.style.border = '1px solid #ccc';
                        dialog.style.boxShadow = '0 0 10px rgba(0, 0, 0, 0.5)';
                        dialog.style.borderRadius = '6px'
                        var img = document.createElement('img')
                        img.src = require(`../../assets/features/flowers/roses/allFinetuned/1_n/layer${e.data.depth-1}_${index}.jpg`)
                        img.style.width = '100%';
                        img.style.height = '100%';
                        dialog.appendChild(img)
                        document.body.appendChild(dialog)
                        return
                    }

                })
            }
            // 右边目标模型网络关系图
            function clickStudentModel(dom) {
                dom.on('click', (e) => {
                    console.log('点击事件', e)
                    if (e.dataType === 'node') {
                        var dialog = document.getElementById(`img_dialog_${e.data.name}_${e.data.value}`);
                        if (dialog) {
                            console.log('dom', dialog)
                            console.log('获取图片display', dialog.style.display)
                            // 第三次点击，需要重新显示弹出框
                            if (dialog.style.display === 'none') {
                                console.log('显示', dialog.style.display)
                                dialog.style.display = 'block'
                                console.log('修改display后', dialog.style.display)
                                return;
                            } else if (dialog.style.display === 'block') { // 第二次点击，若存在弹出框，需要隐藏弹出框
                                console.log('隐藏', dialog.style.display)
                                dialog.style.display = 'none';
                                console.log('修改display后', dialog.style.display)
                                return;
                            }
                        } else {
                            // 第一次点击时，创建对话框
                            var dialog = document.createElement('div');
                            dialog.setAttribute('id', `img_dialog_${e.data.name}_${e.data.value}`)
                            dialog.style.position = 'fixed';
                            // dialog.style.top = '50%';
                            // console.log(eval(`43+${e.data.depth}*5`)+'%')
                            dialog.style.left = eval(`43+${e.data.depth}*13`) + '%';
                            // 找到神经元的纵向顺序
                            var underStr = e.data.name.indexOf("_", e.data.name.indexOf('_') + 1)
                            var index = e.data.name.substr(underStr + 1)
                            dialog.style.bottom = eval(`50-${parseInt(index)}*4`) + '%';
                            // dialog.style.transform = 'translate(-50%, -50%)';
                            dialog.style.width = '100px';
                            dialog.style.height = '100px';
                            dialog.style.backgroundColor = '#fff';
                            dialog.style.border = '1px solid #ccc';
                            dialog.style.boxShadow = '0 0 10px rgba(0, 0, 0, 0.5)';
                            dialog.style.borderRadius = '6px'
                            dialog.style.display = 'block'
                            var img = document.createElement('img')
                            img.src = require(`../../assets/features/flowers/tulips/allFinetuned/3c3/layer${e.data.depth-1}_${index}.jpg`)
                            img.style.width = '100%';
                            img.style.height = '100%';
                            dialog.appendChild(img)
                            document.body.appendChild(dialog)
                            console.log('生成dom', dialog)
                            return;
                        }

                    }

                })
            }
            var networkData = reactive([])
            var networkLinks = reactive([])
            watch(() => [props.dataset, props.model, props.instance, props.methodName], () => {
                datasetObj.value = props.dataset
                modelObj.value = props.model
                instanceID.value = props.instance
                methodVal.value = props.methodName
                // console.log('network收到父组件networkview传递的dataset',datasetObj.value)
                // console.log('network收到父组件networkview传递的model',modelObj.value)
                // console.log('network收到父组件networkview传递的instance',instanceID.value)
                // console.log('network收到父组件networkview传递的method', methodVal.value)
                // console.log('network收到父组件networkview传递的axisLeft',layerLeft.value)
                // console.log('network收到父组件networkview传递的axi sRight',layerRight.value)

                if (methodVal.value && props.modelOptim) {
                    // console.log('符合条件1')
                    axios.get('/modelData/slicing_model.json').then(res => {
                        // console.log("res.data--",res.data)
                        // console.log("res.data[0]--",res.data[0])
                        for (let i = 0; i < res.data.length; i++) {
                            if (res.data[i].dataset_name == datasetObj.value && res.data[i].model_name == modelObj.value && res.data[i].instance_id == instanceID.value) {
                                networkData = res.data[i].data
                                networkLinks = res.data[i].links
                                // console.log('networkData-true--',networkData)
                                // console.log('networkLinks--',networkLinks)
                                for (let j = 0; j < networkData.length; j++) {
                                    networkData[j].itemStyle.color = finetunedColors[networkData[j].depth % 5]
                                }
                                //初始化展现0到3层的网络桑葚图
                                option.series[0].data = []
                                option.series[0].links = []
                                var filterData = networkData.filter(item => (layerLeft.value <= item.depth && item.depth <= layerRight.value))
                                option.series[0].data = filterData
                                var filterLinks = networkLinks.filter(item => {
                                    if (item.source) {
                                        // 找到第一个下划线的位置
                                        var firstStr = item.source.indexOf("_");
                                        // 找到第二个下划线的位置
                                        var secondStr = item.source.indexOf("_", firstStr + 1)
                                        var layerNum = item.source.substring(firstStr + 2, secondStr)
                                        // console.log('layerNum',layerNum)
                                    }
                                    return ((item.value && item.value >= 20) && layerLeft.value <= layerNum && layerNum <= layerRight.value)
                                })
                                option.series[0].links = filterLinks
                                // console.log('首次初始化option.series[0].data--',option.series[0].data)
                                // console.log('首次初始化option.series[0].links--', option.series[0].links)
                                myChart = echarts.init(container.value)
                                myChart.setOption(option)
                                break;
                            }

                        }
                    }, { deep: true }).catch((error) => {
                        console.error("请求出错了：", error)
                    })
                }
                else if (!methodVal.value && props.modelOptim) {
                    // console.log('符合条件2')
                    axios.get('/modelData/finetuned_model.json').then(res => {
                        // console.log("res.data--",res.data)
                        // console.log("res.data[0]--",res.data[0])
                        for (let i = 0; i < res.data.length; i++) {
                            if (res.data[i].dataset_name == datasetObj.value && res.data[i].model_name == modelObj.value && res.data[i].instance_id == instanceID.value) {
                                networkData = res.data[i].data
                                networkLinks = res.data[i].links
                                // console.log('networkData--',networkData)
                                // console.log('networkLinks--',networkLinks)
                                for (let j = 0; j < networkData.length; j++) {
                                    // console.log('networkData[89].depth--',networkData[j].depth)
                                    networkData[j].itemStyle.color = finetunedColors[networkData[j].depth % 5]
                                    // console.log('networkData[j].itemStyle.color--',networkData[j].itemStyle.color)
                                }
                                option.series[0].data = []
                                option.series[0].links = []
                                var filterData = networkData.filter(item => (layerLeft.value <= item.depth && item.depth <= layerRight.value))
                                option.series[0].data = filterData
                                var filterLinks = networkLinks.filter(item => {
                                    if (item.source) {
                                        // 找到第一个下划线的位置
                                        var firstStr = item.source.indexOf("_");
                                        // 找到第二个下划线的位置
                                        var secondStr = item.source.indexOf("_", firstStr + 1)
                                        var layerNum = item.source.substring(firstStr + 2, secondStr)
                                        // console.log('layerNum', layerNum)
                                    }
                                    return layerLeft.value <= layerNum && layerNum <= layerRight.value
                                })
                                option.series[0].links = filterLinks
                                // console.log('首次初始化option.series[0].data--',option.series[0].data)
                                // console.log('首次初始化option.series[0].links--',option.series[0].links)
                                myChart = echarts.init(container.value)
                                myChart.setOption(option)
                                clickStudentModel(myChart)
                                // clickStudentModel(myChart)
                                // console.log('finetune_model_create_img')
                                break;
                            }

                        }
                    }, { deep: true }).catch((error) => {
                        console.error("请求出错了：", error)
                    })
                }
                else if (!methodVal.value && !props.modelOptim) {
                    // console.log('符合条件3')
                    axios.get('/modelData/pretrained_model.json').then(res => {
                        // console.log("res.data--",res.data)
                        // console.log("res.data[0]--",res.data[0])
                        for (let i = 0; i < res.data.length; i++) {
                            if (res.data[i].dataset_name == datasetObj.value && res.data[i].model_name == modelObj.value && res.data[i].instance_id == instanceID.value) {
                                networkData = res.data[i].data
                                networkLinks = res.data[i].links
                                // console.log('networkData--',networkData)
                                // console.log('networkLinks--',networkLinks)

                                for (let j = 0; j < networkData.length; j++) {
                                    // console.log('networkData[89].depth--',networkData[j].depth)
                                    networkData[j].itemStyle.color = sourceModelColors[networkData[j].depth % 5]
                                    // console.log('networkData[j].itemStyle.color--',networkData[j].itemStyle.color)
                                }
                                option.series[0].data = []
                                option.series[0].links = []
                                var filterData = networkData.filter(item => (layerLeft.value <= item.depth && item.depth <= layerRight.value))
                                option.series[0].data = filterData
                                var filterLinks = networkLinks.filter(item => {
                                    if (item.source) {
                                        // 找到第一个下划线的位置
                                        var firstStr = item.source.indexOf("_");
                                        // 找到第二个下划线的位置
                                        var secondStr = item.source.indexOf("_", firstStr + 1)
                                        var layerNum = item.source.substring(firstStr + 2, secondStr)
                                        // console.log('layerNum', layerNum)
                                    }
                                    return layerLeft.value <= layerNum && layerNum <= layerRight.value
                                })
                                option.series[0].links = filterLinks
                                // console.log('首次初始化option.series[0].data--',option.series[0].data)
                                // console.log('首次初始化option.series[0].links--',option.series[0].links)
                                myChart = echarts.init(container.value)
                                myChart.setOption(option)
                                clickPretrainModel(myChart)
                                // console.log('source_model_create_img')
                                break;
                            }

                        }
                    }, { deep: true }).catch((error) => {
                        console.error("请求出错了：", error)
                    })
                }

            })

            var weightsVal = ref(null)
            var yuzhiVal = ref(null)
            weightsVal.value = props.weights
            yuzhiVal.value = props.yuzhi
            // console.log("子组件Network收到weights",weightsVal.value)
            const updateSlider = () => {
                //图表更新后，控制参数的滑块更新为最大值
                // console.log('调用updateSlider')
                weightsVal.value = 100
                yuzhiVal.value = 300
                emit('updateSlider', [weightsVal.value, yuzhiVal.value])
            }
            watch(() => [props.axisLeft, props.axisRight], () => {
                layerLeft.value = props.axisLeft
                layerRight.value = props.axisRight
                // console.log('network收到父组件networkview传递的axisLeft',layerLeft.value)
                // console.log('network收到父组件networkview传递的axisRight',layerRight.value)

                // option.series[0].data = []
                // option.series[0].links = []
                // console.log('初始化option.series[0].data--',option.series[0].data)
                // console.log('初始化option.series[0].links--',option.series[0].links)

                var filterData = networkData.filter(item => (layerLeft.value <= item.depth && item.depth <= layerRight.value))
                option.series[0].data = filterData
                var filterLinks = networkLinks.filter(item => {
                    if (item.source) {
                        // 找到第一个下划线的位置
                        var firstStr = item.source.indexOf("_");
                        // 找到第二个下划线的位置
                        var secondStr = item.source.indexOf("_", firstStr + 1)
                        var layerNum = item.source.substring(firstStr + 2, secondStr)
                        // console.log('layerNum', layerNum)
                    }
                    return layerLeft.value <= layerNum && layerNum <= layerRight.value
                })
                option.series[0].links = filterLinks
                // console.log('更新的option.series[0].data--',option.series[0].data)
                // console.log('更新的option.series[0].links--',option.series[0].links)
                myChart = echarts.init(container.value)
                myChart.setOption(option)
                updateSlider()
                if(!methodVal.value && !props.modelOptim){
                    clickPretrainModel(myChart)
                }
                if(!methodVal.value && props.modelOptim){
                    clickStudentModel(myChart)
                }
            })
            watch(() => [props.weights,props.yuzhi], () => {
                weightsVal.value = props.weights
                yuzhiVal.value = props.yuzhi
                // console.log("监听weights--",weightsVal.value)
                // var filterWeights = option.series[0].links.filter(item => (item.value <= weightsVal.value))
                var links = option.series[0].links
                var filterWeights = option.series[0].links.sort((a, b) => b.value - a.value).slice(0, weightsVal.value);
                option.series[0].links = filterWeights
                console.log('links',links)
                console.log('filterWeights--',filterWeights)

                var neurons = option.series[0].data
                var filerData = option.series[0].data.filter(item=>{
                   return item.value && item.value>= yuzhiVal.value
                })
                option.series[0].data = filerData

                myChart = echarts.init(container.value)
                myChart.setOption(option)

                if(!methodVal.value && !props.modelOptim){
                    clickPretrainModel(myChart)
                }
                if(!methodVal.value && props.modelOptim){
                    clickStudentModel(myChart)
                }
                option.series[0].links = links
                option.series[0].data = neurons
            })
            //监听阈值的改变，控制网络层之间的连线数量
            // 如果一个神经元激活值大于一个给定的阈值，则我们称其为一个对学生任务有用的神经元
            // watch(() => [props.yuzhi], () => {
            //     yuzhiVal.value = props.yuzhi
            //     var filerData = option.series[0].data.filter(item=>{
            //        return item.value && item.value>= yuzhiVal.value
            //     })
            //     var neurons = option.series[0].data
            //     option.series[0].data = filerData
            //     myChart = echarts.init(container.value)
            //     myChart.setOption(option)
            //     if(!methodVal.value && !props.modelOptim){
            //         clickPretrainModel(myChart)
            //     }
            //     if(!methodVal.value && props.modelOptim){
            //         clickStudentModel(myChart)
            //     }
            //     option.series[0].data = neurons
            // })
            return {
                container, myChart, option, finetunedColors, slicingColors, sourceModelColors,
                datasetObj, modelObj, instanceID, layerLeft, layerRight,
                networkData, networkLinks,
                weightsVal, yuzhiVal, updateSlider,
                methodVal,
                clickPretrainModel, clickStudentModel
            }
        }
    }

</script>

<style>
    #container {
        margin-bottom: 2px;
        box-shadow: 0px 0px 4px #9c886d66;
        border-radius: 10px;
        /* background-color: #e0cdcd; */

        /* border: 1px solid red; */
    }
</style>