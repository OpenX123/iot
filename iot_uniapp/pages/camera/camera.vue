<template>
  <view class="container">
    <picker mode="selector" :range="devices" @change="onDeviceChange">
      <view class="picker">
        当前设备：{{ selectedDevice }}
      </view>
    </picker>
    <view class="image-container">
      <image :src="currentImage" class="monitor-image"></image>
    </view>
	<view class="info-container">
	  <text>种类：{{ pestType }}</text>
	  <br>
	  <text>数量：{{ pestCount }}</text>
	</view>
    <view class="button-container">
      <button @click="getImage">获取监控图像</button>
      <button @click="startAnalysis">开始识别分析</button>
      <button @click="resetAnalysis">退出识别</button>
      <button @click="repelPests">害虫驱赶</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      devices: ['设备1', '设备2'],
      selectedDevice: '设备1',
      currentImage: '',
      deviceImages: ['/static/device1.png', '/static/device1-l.png'],
      currentDeviceIndex: 0,
      pestType: '',
      pestCount: '',
    };
  },
  methods: {
    onDeviceChange(event) {
      const selected = event.target.value;
      this.selectedDevice = this.devices[selected];
      if (this.selectedDevice === '设备2') {
        uni.navigateTo({
          url: '/pages/camera1/camera1'
        });
      }
    },
    getImage() {
      console.log('获取监控图像');
	  	  this.currentImage='/static/device1.png';
    },
    startAnalysis() {
      this.pestType = '蝗虫';
      this.pestCount = '1只';
      this.currentDeviceIndex = (this.currentDeviceIndex + 1) % this.deviceImages.length;
      this.currentImage = this.deviceImages[this.currentDeviceIndex];
    },
    resetAnalysis() {
      this.pestType = '';
      this.pestCount = '';
      this.currentDeviceIndex = (this.currentDeviceIndex - 1 + this.deviceImages.length) % this.deviceImages.length;
      this.currentImage = this.deviceImages[this.currentDeviceIndex];
    },
    repelPests() {
      console.log('害虫驱赶');
    }
  },
};
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.picker {
  margin-top: 20px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  width: 300px;
  text-align: center;
}
.image-container {
  margin-top: 20px;
}
.monitor-image {
  width: 300px;
  height: 200px;
}
.button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}
button {
  margin-top: 10px;
}
.info-container {
  margin-top: 20px;
}
</style>
