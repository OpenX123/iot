<template>
  <view class="charts-box">
    <!-- 日期选择下拉按钮 -->
    <picker mode="date" @change="onDateChange">
      <view class="picker">
        {{ selectedDate }}
      </view>
    </picker>

    <!-- 导出按钮 -->
    <view class="button-container">
      <button @click="exportTodayData">导出今日数据</button>
      <button @click="exportAllData">导出所有数据</button>
    </view>

    <!-- 光照强度折线图 -->
    <view class="chart-container" ref="chartContainerLight">
      <qiun-data-charts 
        type="line"
        :opts="optsLight"
        :chartData="chartDataLight"
        :ontouch="true"
        @scrollRight="scrollRight"
      />
    </view>
    <!-- 温度折线图 -->
    <view class="chart-container" ref="chartContainerTemp">
      <qiun-data-charts 
        type="line"
        :opts="optsTemp"
        :chartData="chartDataTemp"
        :ontouch="true"
        @scrollRight="scrollRight"
      />
    </view>
    <!-- 电压折线图 -->
    <view class="chart-container" ref="chartContainerVolt">
      <qiun-data-charts 
        type="line"
        :opts="optsVolt"
        :chartData="chartDataVolt"
        :ontouch="true"
        @scrollRight="scrollRight"
      />
    </view>
    <!-- 电量折线图 -->
    <view class="chart-container" ref="chartContainerC">
      <qiun-data-charts 
        type="line"
        :opts="optsC"
        :chartData="chartDataC"
        :ontouch="true"
        @scrollRight="scrollRight"
      />
    </view>
  </view>
</template>

<script>
  import { sensorData } from '@/common/api.js'; // 引入API请求模块
  import * as XLSX from 'xlsx'; // 引入xlsx库

  export default {
    data() {
      return {
        selectedDate: '', // 选中的日期
        chartDataLight: {}, // 光照强度图表数据
        chartDataTemp: {},  // 温度图表数据
        chartDataVolt: {},  // 电压图表数据
        chartDataC: {}, // 电量图表数据
        // 图表通用配置
        commonOpts: {
          timing: "easeOut",
          duration: 1000,
          rotate: false,
          rotateLock: false,
          color: ["#1890FF", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4", "#ea7ccc"],
          padding: [15, 10, 0, 15],
          fontSize: 13,
          fontColor: "#666666",
          dataLabel: true,
          dataPointShape: true,
          dataPointShapeType: "hollow",
          touchMoveLimit: 60,
          enableScroll: true,
          enableMarkLine: false,
          legend: {
            show: true,
            position: "bottom",
            float: "center",
            padding: 5,
            margin: 5,
            backgroundColor: "rgba(0,0,0,0)",
            borderColor: "rgba(0,0,0,0)",
            borderWidth: 0,
            fontSize: 13,
            fontColor: "#666666",
            lineHeight: 11,
            hiddenColor: "#CECECE",
            itemGap: 10
          },
          xAxis: {
            disableGrid: true,
            disabled: false,
            axisLine: true,
            axisLineColor: "#CCCCCC",
            calibration: false,
            fontColor: "#666666",
            fontSize: 13,
            lineHeight: 20,
            marginTop: 0,
            rotateLabel: false,
            rotateAngle: 45,
            itemCount: 10,
            boundaryGap: "center",
            splitNumber: 5,
            gridColor: "#CCCCCC",
            gridType: "solid",
            dashLength: 4,
            gridEval: 1,
            scrollShow: true,
            scrollAlign: "right",
            scrollColor: "#A6A6A6",
            scrollBackgroundColor: "#EFEBEF",
            title: "",
            titleFontSize: 13,
            titleOffsetY: 0,
            titleOffsetX: 0,
            titleFontColor: "#666666",
            format: ""
          },
          yAxis: {
            gridType: "dash",
            dashLength: 2,
            disabled: false,
            disableGrid: false,
            splitNumber: 5,
            gridColor: "#CCCCCC",
            padding: 10,
            showTitle: false,
            data: []
          },
          extra: {
            line: {
              type: "straight",
              width: 2,
              activeType: "hollow",
              linearType: "none",
              onShadow: false,
              animation: "vertical"
            },
            tooltip: {
              showBox: true,
              showArrow: true,
              showCategory: false,
              borderWidth: 0,
              borderRadius: 0,
              borderColor: "#000000",
              borderOpacity: 0.7,
              bgColor: "#000000",
              bgOpacity: 0.7,
              gridType: "solid",
              dashLength: 4,
              gridColor: "#CCCCCC",
              boxPadding: 3,
              fontSize: 13,
              lineHeight: 20,
              fontColor: "#FFFFFF",
              legendShow: true,
              legendShape: "auto",
              splitLine: true,
              horizentalLine: false,
              xAxisLabel: false,
              yAxisLabel: false,
              labelBgColor: "#FFFFFF",
              labelBgOpacity: 0.7,
              labelFontColor: "#666666"
            },
            markLine: {
              type: "solid",
              dashLength: 4,
              data: []
            }
          }
        },
        // 光照强度图表配置
        optsLight: {},
        // 温度图表配置
        optsTemp: {},
        // 电压图表配置
        optsVolt: {},
        // 电量图表配置
        optsC: {}
      };
    },
    created() {
      // 初始化配置
      this.optsLight = { ...this.commonOpts, color: ["#1890FF", "#91CB74"] };
      this.optsTemp = { ...this.commonOpts, color: ["#EE6666", "#FF9F7F", "#FFDB5C"] };
      this.optsVolt = { ...this.commonOpts, color: ["#73C0DE", "#C4EBAD", "#FFD700"] };
      this.optsC = { ...this.commonOpts, color: ["#3CA272", "#FC8452", "#9A60B4"] };
    },
    onReady() {
      const today = new Date().toISOString().split('T')[0];
      this.selectedDate = today; // 设置初始日期为今天
      this.getServerData(today); // 页面准备好后获取数据
    },
    methods: {
      /**
       * 日期选择器更改事件处理函数
       * @param {Object} e - 事件对象
       */
      onDateChange(e) {
        const selectedDate = e.detail.value; // 获取选择的日期
        this.selectedDate = selectedDate; // 更新选中的日期
        this.getServerData(selectedDate); // 获取选择日期的数据
      },

      /**
       * 格式化时间戳
       * @param {number} timestamp - 时间戳
       * @returns {string} 格式化后的日期时间字符串
       */
      formatTimestamp(timestamp) {
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      },

      /**
       * 从服务器获取数据并处理
       * @param {string} selectedDate - 选中的日期
       */
      getServerData(selectedDate) {
        sensorData().then((res) => {
          if (res.statusCode === 200 && res.data && res.data.code === 1) {
            const serverData = res.data.data; // 获取到的数据
            let categories = []; // 类别（时间戳）
            let lightSeries = {
              name: "光照强度1",
              data: []
            };
            let light2Series = {
              name: "光照强度2",
              data: []
            };
            let tempSeries = {
              name: "温度09",
              data: []
            };
            let temp1Series = {
              name: "温度10",
              data: []
            };
            let temp2Series = {
              name: "温度11",
              data: []
            };
            let voltSeries = {
              name: "湿度1",
              data: []
            };
            let volt1Series = {
              name: "湿度2",
              data: []
            };
            let volt2Series = {
              name: "湿度3",
              data: []
            };
            let c1Series = {
              name: "空气质量浓度1",
              data: []
            };
            let c2Series = {
              name: "空气质量浓度2",
              data: []
            };
            let c3Series = {
              name: "空气质量浓度3",
              data: []
            };

            // 数据格式转换
            serverData.forEach(item => {
              const itemDate = new Date(item.timestamp);
              const itemDateString = itemDate.toISOString().split('T')[0];
              if (itemDateString === selectedDate) {
                const time = `${itemDate.getHours()}:${String(itemDate.getMinutes()).padStart(2, '0')}:${String(itemDate.getSeconds()).padStart(2, '0')}`;
                categories.push(time);
                lightSeries.data.push(item.light !== null ? item.light : 0); // 添加光照数据
                light2Series.data.push(item.light2 !== null ? item.light2 : 0); // 添加光照数据2
                tempSeries.data.push(item.temp !== null ? item.temp : 0); // 添加温度数据
                temp1Series.data.push(item.temp1 !== null ? item.temp1 : 0); // 添加温度数据1
                temp2Series.data.push(item.temp2 !== null ? item.temp2 : 0); // 添加温度数据2
                voltSeries.data.push(item.volt !== null ? parseFloat(item.volt).toFixed(2) : '0.00'); // 添加电压数据
                volt1Series.data.push(item.volt1 !== null ? parseFloat(item.volt1).toFixed(2) : '0.00'); // 添加电压数据1
                volt2Series.data.push(item.volt2 !== null ? parseFloat(item.volt2).toFixed(2) : '0.00'); // 添加电压数据2
                c1Series.data.push(item.C1 !== null ? parseFloat(item.C1).toFixed(2) : '0.00'); // 添加电量数据
                c2Series.data.push(item.C2 !== null ? parseFloat(item.C2).toFixed(2) : '0.00'); // 添加电量数据1
                c3Series.data.push(item.C3 !== null ? parseFloat(item.C3).toFixed(2) : '0.00'); // 添加电量数据2
              }
            });

            // 设置光照强度图表数据
            this.chartDataLight = {
              categories: categories,
              series: [lightSeries, light2Series]
            };

            // 设置温度图表数据
            this.chartDataTemp = {
              categories: categories,
              series: [tempSeries, temp1Series, temp2Series]
            };

            // 设置电压图表数据
            this.chartDataVolt = {
              categories: categories,
              series: [voltSeries, volt1Series, volt2Series]
            };

            // 设置电量图表数据
            this.chartDataC = {
              categories: categories,
              series: [c1Series, c2Series, c3Series]
            };

            // 自动滚动到最后
            this.$nextTick(() => {
              this.scrollToCurrentTime('chartContainerLight');
              this.scrollToCurrentTime('chartContainerTemp');
              this.scrollToCurrentTime('chartContainerVolt');
              this.scrollToCurrentTime('chartContainerC');
            });
          }
        }).catch((err) => {
          console.error('请求失败:', err); // 请求失败处理
        });
      },

      /**
       * 自动滚动到当前时间位置
       * @param {string} id - 容器ID
       */
      scrollToCurrentTime(id) {
        const container = this.$refs[id];
        if (container) {
          container.scrollLeft = container.scrollWidth;
        }
      },

      /**
       * 导出当天数据
       */
      exportTodayData() {
        this.exportData(this.selectedDate);
      },

      /**
       * 导出所有数据
       */
      exportAllData() {
        this.exportData();
      },

      /**
       * 导出数据
       * @param {string} [date=null] - 指定日期
       */
      exportData(date = null) {
        sensorData().then((res) => {
          if (res.statusCode === 200 && res.data && res.data.code === 1) {
            let serverData = res.data.data; // 获取到的数据
            serverData = serverData.map(item => ({
              ...item,
              timestamp: this.formatTimestamp(item.timestamp),
              light: item.light !== null ? item.light : 0,
              light2: item.light2 !== null ? item.light2 : 0,
              volt: item.volt !== null ? parseFloat(item.volt).toFixed(2) : '0.00',
              volt1: item.volt1 !== null ? parseFloat(item.volt1).toFixed(2) : '0.00',
              volt2: item.volt2 !== null ? parseFloat(item.volt2).toFixed(2) : '0.00',
              temp: item.temp !== null ? item.temp : 0,
              temp1: item.temp1 !== null ? item.temp1 : 0,
              temp2: item.temp2 !== null ? item.temp2 : 0,
              C1: item.C1 !== null ? parseFloat(item.C1).toFixed(2) : '0.00',
              C2: item.C2 !== null ? parseFloat(item.C2).toFixed(2) : '0.00',
              C3: item.C3 !== null ? parseFloat(item.C3).toFixed(2) : '0.00'
            }));
            let filteredData = serverData;

            if (date) {
              filteredData = serverData.filter(item => {
                const itemDate = new Date(item.timestamp).toISOString().split('T')[0];
                return itemDate === date;
              });
            }

            const worksheet = XLSX.utils.json_to_sheet(filteredData);
            const workbook = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(workbook, worksheet, 'Data');
            const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
            const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });

            // 生成文件下载链接
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', date ? `data_${date}.xlsx` : 'all_data.xlsx');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
          }
        }).catch((err) => {
          console.error('请求失败:', err); // 请求失败处理
        });
      },

      /**
       * 滚动事件处理函数
       * @param {Object} e - 事件对象
       */
      scrollRight(e) {
        console.log(e);
      }
    }
  };
</script>

<style scoped>
  .charts-box {
    width: 100%;
  }

  .picker {
    padding: 15rpx 70rpx 15rpx 90rpx;
    background-color: #fff;
    border-radius: 5px;
    text-align: center;
    font-size: 23px;
    margin-bottom: 10px;
  }

  .button-container {
    display: flex;
    justify-content: space-around;
    margin-bottom: 7px;
    background-color: #fff;
    border-radius: 10px;
    padding: 5px;
  }

  .chart-container {
    width: 100%;
    height: 300px;
    margin-bottom: 20px;
    overflow: auto;
  }

  button {
    padding: 5px 10px;
    background-color: #4EA8EC;
    color: white;
    border: none;
    border-radius: 7px;
    cursor: pointer;
    font-size: 16px;
  }
</style>
