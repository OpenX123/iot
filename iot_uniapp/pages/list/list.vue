<template>
  <view class="container">
    <view class="switch-container">
      <text class="label">自动模式</text>
      <switch @change="toggleAutoMode" :checked="autoModeState"></switch>
    </view>

    <view class="slider-container">
      <view class="slider-item">
        <text class="label">温度下限: {{ tempLL }}</text>
        <slider v-model="tempLL" :min="0" :max="200" show-value @change="updateThreshold('tempLL', $event)"></slider>
      </view>
      <view class="slider-item">
        <text class="label">温度上限: {{ tempUL }}</text>
        <slider v-model="tempUL" :min="0" :max="200" show-value @change="updateThreshold('tempUL', $event)"></slider>
      </view>
    </view>

    <view class="slider-container">
      <view class="slider-item">
        <text class="label">光照下限: {{ lightLL }}</text>
        <slider v-model="lightLL" :min="0" :max="200" show-value @change="updateThreshold('lightLL', $event)"></slider>
      </view>
      <view class="slider-item">
        <text class="label">光照上限: {{ lightUL }}</text>
        <slider v-model="lightUL" :min="0" :max="200" show-value @change="updateThreshold('lightUL', $event)"></slider>
      </view>
    </view>

    <view class="slider-container">
      <view class="slider-item">
        <text class="label">湿度下限: {{ humidityLL }}</text>
        <slider v-model="humidityLL" :min="0" :max="100" show-value @change="updateThreshold('humidityLL', $event)"></slider>
      </view>
      <view class="slider-item">
        <text class="label">湿度上限: {{ humidityUL }}</text>
        <slider v-model="humidityUL" :min="0" :max="100" show-value @change="updateThreshold('humidityUL', $event)"></slider>
      </view>
    </view>

    <view class="slider-container">
      <view class="slider-item">
        <text class="label">空气质量浓度下限: {{ airQualityLL }}</text>
        <slider v-model="airQualityLL" :min="0" :max="500" show-value @change="updateThreshold('airQualityLL', $event)"></slider>
      </view>
      <view class="slider-item">
        <text class="label">空气质量浓度上限: {{ airQualityUL }}</text>
        <slider v-model="airQualityUL" :min="0" :max="500" show-value @change="updateThreshold('airQualityUL', $event)"></slider>
      </view>
    </view>

   <view class="switch-container">
      <text class="label">近光灯(闪)</text>
      <switch @change="toggleSwitch1s" :checked="switchState1"></switch>
    </view>
    <view class="switch-container">
      <text class="label">远光灯(闪)</text>
      <switch @change="toggleSwitch2s" :checked="switchState2s"></switch>
    </view>
    <view class="switch-container">
      <text class="label">蜂鸣器开关</text>
      <switch @change="toggleBuzzer" :checked="buzzerState"></switch>
    </view>
    <view class="switch-container">
      <text class="label">复位</text>
      <switch @change="toggleReset" :checked="resetState"></switch>
    </view>
  </view>
</template>

<script>
  import { publishMessage, upload } from '../../common/api.js';

  const COUNT = 3;

  export default {
    data() {
      return {
        autoModeState: false,
        switchState1: false,
        switchState2: false,
        switchState1s: false,
        switchState2s: false,
        buzzerState: false,
        resetState: false,
        tempUL: 100,
        tempLL: 0,
        lightUL: 100,
        lightLL: 0,
        humidityUL: 100,
        humidityLL: 0,
        airQualityUL: 500,
        airQualityLL: 0,
        intervalId: null,
        topic: '/post',
        qos: 2,
        filePath: '',
        fileName: '',
      };
    },
    methods: {
      goback() {
        uni.navigateBack();
      },
      toggleAutoMode(event) {
        this.autoModeState = event.detail.value;
        const message = {
          topic: this.topic,
          msg: JSON.stringify({ psz: this.autoModeState ? 0 : 1 }),
          qos: this.qos,
        };

        this.sendMultipleRequests(message, '模式设置');
      },
      toggleSwitch1s(event) {
        this.switchState1s = event.detail.value;
        const message = {
          topic: this.topic,
          msg: JSON.stringify({ led1: this.switchState1s ? 2 : 0 }),
          qos: this.qos,
        };
        this.sendMultipleRequests(message, 'LED 设置');
      },
      toggleSwitch2s(event) {
        this.switchState2s = event.detail.value;
        const message = {
          topic: this.topic,
          msg: JSON.stringify({ led2: this.switchState2s ? 2 : 0 }),
          qos: this.qos,
        };
        this.sendMultipleRequests(message, 'LED 设置');
      },
      toggleBuzzer(event) {
        this.buzzerState = event.detail.value;
        const message = {
          topic: this.topic,
          msg: JSON.stringify({ buzzer: this.buzzerState ? 1 : 0 }),
          qos: this.qos,
        };
        this.sendMultipleRequests(message, '蜂鸣器设置');
      },
      toggleReset(event) {
        this.resetState = event.detail.value;
        const message = {
          topic: this.topic,
          msg: JSON.stringify({ reset: this.resetState ? 1 : 0 }),
          qos: this.qos,
        };
        this.sendMultipleRequests(message, '复位设置');
      },
      updateThreshold(type, event) {
        this[type] = event.detail.value;

        const thresholds = {
          tempUL: this.tempUL,
          tempLL: this.tempLL,
          lightUL: this.lightUL,
          lightLL: this.lightLL,
          humidityUL: this.humidityUL,
          humidityLL: this.humidityLL,
          airQualityUL: this.airQualityUL,
          airQualityLL: this.airQualityLL
        };

        if (thresholds.tempLL >= thresholds.tempUL) {
          uni.showToast({
            title: '温度下限必须小于温度上限',
            icon: 'none',
          });
          return;
        }

        if (thresholds.lightLL >= thresholds.lightUL) {
          uni.showToast({
            title: '光照下限必须小于光照上限',
            icon: 'none',
          });
          return;
        }

        if (thresholds.humidityLL >= thresholds.humidityUL) {
          uni.showToast({
            title: '湿度下限必须小于湿度上限',
            icon: 'none',
          });
          return;
        }

        if (thresholds.airQualityLL >= thresholds.airQualityUL) {
          uni.showToast({
            title: '空气质量浓度下限必须小于空气质量浓度上限',
            icon: 'none',
          });
          return;
        }

        const message = {
          topic: this.topic,
          msg: JSON.stringify(thresholds),
          qos: this.qos,
        };

        publishMessage(message).then((res) => {
          if (res.statusCode === 200 && res.data && res.data.code === 1) {
            uni.showToast({
              title: '阈值设置成功',
              icon: 'success',
            });
          } else {
            uni.showToast({
              title: res.message || '阈值设置失败',
              icon: 'none',
            });
          }
        }).catch((err) => {
          uni.showToast({
            title: '请求失败',
            icon: 'none',
          });
          console.error('请求失败:', err);
        });
      },
      sendTimestamp() {
        const timestamp = Math.floor(Date.now() / 1000);
        const message = {
          topic: this.topic,
          msg: JSON.stringify({ time: timestamp }),
          qos: this.qos,
        };
        publishMessage(message).then((res) => {
          if (res.statusCode === 200 && res.data && res.data.code === 1) {
            console.log('时间戳发送成功');
          } else {
            console.error('时间戳发送失败:', res.message);
          }
        }).catch((err) => {
          console.error('时间戳请求失败:', err);
        });
      },
      sendMultipleRequests(message, title) {
        const sendRequest = () => {
          return publishMessage(message).then((res) => {
            if (res.statusCode === 200 && res.data && res.data.code === 1) {
              return true;
            } else {
              return false;
            }
          }).catch((err) => {
            console.error('请求失败:', err);
            return false;
          });
        };

        const requestPromises = [];
        for (let i = 0; i < COUNT; i++) {
          requestPromises.push(sendRequest());
        }

        Promise.all(requestPromises).then((results) => {
          if (results.some(result => result)) {
            uni.showToast({
              title: `${title}成功`,
              icon: 'success',
            });
          } else {
            uni.showToast({
              title: `${title}失败`,
              icon: 'none',
            });
          }
        });
      },
      initializeStates() {
        this.toggleAutoMode({ detail: { value: false } });
      },
    },
    mounted() {
      this.intervalId = setInterval(this.sendTimestamp, 12000);
      clearInterval(this.intervalId);
      this.initializeStates();
    },
    beforeDestroy() {
      if (this.intervalId) {
        clearInterval(this.intervalId);
      }
    },
  };
</script>

<style lang="scss">
	.container {
		padding: 20px;
		text-align: center;
		font-size: 16px;
		color: #333;
	}

	.switch-container,
	.slider-container {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 20px;
	}

	.slider-item {
		width: 100%;
		margin-bottom: 10px;
	}

	.label {
		margin-right: 10px;
		font-size: 18px;
	}

	.status-container {
		display: flex;
		align-items: center;
		margin-top: 20px;
	}

	.status.on {
		color: green;
	}

	.status.off {
		color: red;
	}
</style>