<template>
  <view class="container">
	<view @click="goback()"> < 返回上一页</view>
    <view class="header">
      <!-- 显示灯泡图片，根据开关状态动态切换图片 -->
      <image class="logo" :src="bulbImage" mode="aspectFit"></image>
    </view>
    <view class="switch-container">
      <!-- 显示开关标签 -->
      <text class="label">LED 开关</text>
      <!-- 开关控件，绑定switchState，切换时触发toggleSwitch方法 -->
      <switch @change="toggleSwitch" :checked="switchState"></switch>
    </view>
    <view class="status-container">
      <!-- 显示当前状态标签 -->
      <text class="status-label">当前状态: </text>
      <!-- 根据开关状态显示当前状态（开/关）并设置相应的CSS类 -->
      <text class="status" :class="{'on': switchState, 'off': !switchState}">{{ switchState ? '开' : '关' }}</text>
    </view>
  </view>
</template>

<script>
// 引入发布消息的API方法
import { publishMessage } from '../../utils/api.js';

export default {
  data() {
    return {
      // 定义开关状态，初始为关闭
      switchState: false,
      // 定义灯泡图片路径，根据开关状态切换
      bulbImages: {
        on: '/static/led/led1.png', // 替换为实际的灯泡打开图片路径
        off: '/static/led/led0.png' // 替换为实际的灯泡关闭图片路径
      }
    };
  },
  computed: {
    // 计算属性，根据开关状态返回相应的灯泡图片路径
    bulbImage() {
      return this.switchState ? this.bulbImages.on : this.bulbImages.off;
    }
  },
  methods: {
	  goback(){
	  	uni.navigateBack();
	  },
    // 切换开关状态的方法
    toggleSwitch(event) {
      // 更新开关状态为当前开关控件的值
      this.switchState = event.detail.value;
      // 创建要发布的消息对象
      const message = {
        topic: '/post',
        msg: JSON.stringify({ led: this.switchState ? 1 : 0 }),
        qos: 2
      };
      // 调用publishMessage方法发布消息
      publishMessage(message).then((res) => {
        // 如果消息发布成功，显示成功提示
        if (res.status === 'success') {
          uni.showToast({
            title: '消息发布成功',
            icon: 'success'
          });
        } else {
          // 如果消息发布失败，显示错误提示
          uni.showToast({
            title: res.message || '消息发布失败',
            icon: 'none'
          });
        }
      }).catch((err) => {
        // 如果请求失败，显示请求失败提示并打印错误信息
        uni.showToast({
          title: '请求失败',
          icon: 'none'
        });
        console.error(err);
      });
    }
  },
  // mounted() {
  // 	console.log(this.$route.query.wd)
  // }
  //获取参数
  onLoad(e){
  	console.log(e);
	console.log(getCurrentPages());
  }
};
</script>

<style scoped>
/* 主容器样式，使用flex布局居中对齐 */
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 20px;
  background-color: #f5f5f5;
}

/* 头部样式，包含灯泡图片 */
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

/* 灯泡图片样式 */
.logo {
  width: 300px;
  height: 300px;
  margin-right: 10px;
}

/* 标题样式 */
.title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

/* 开关容器样式 */
.switch-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

/* 开关标签样式 */
.label {
  margin-right: 10px;
  font-size: 18px;
}

/* 状态容器样式 */
.status-container {
  display: flex;
  align-items: center;
  margin-top: 20px;
}

/* 状态标签样式 */
.status-label {
  font-size: 18px;
  margin-right: 10px;
}

/* 状态文本样式 */
.status {
  font-size: 18px;
  font-weight: bold;
}

/* 开状态样式 */
.status.on {
  color: green;
}

/* 关状态样式 */
.status.off {
  color: red;
}
</style>
