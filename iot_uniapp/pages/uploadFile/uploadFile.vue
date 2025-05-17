<template>
  <view class="container">
    <!-- 文件选择区域 -->
    <view class="file-selector">
      <!-- 选择文件按钮 -->
      <button class="button" @click="chooseFile">选择文件</button>
      <!-- 上传文件按钮 -->
      <button class="button upload-button" @click="uploadFile" v-if="fileName">上传文件</button>
      <!-- 显示文件名 -->
      <view v-if="fileName" class="file-name">{{ fileName }}</view>
    </view>
  </view>
</template>

<script>
import { upload } from '../../common/api.js';

export default {
  data() {
    return {
      filePath: '', // 文件路径
      fileName: '' // 文件名
    };
  },
  methods: {
    // 选择文件方法
    chooseFile() {
      uni.chooseFile({
        count: 1, // 只允许选择一个文件
        success: (res) => {
          this.filePath = res.tempFiles[0].path; // 设置文件路径
          this.fileName = res.tempFiles[0].name; // 设置文件名
        }
      });
    },
    // 上传文件方法
    uploadFile() {
      // 检查是否选择了文件
      if (!this.filePath) {
        uni.showToast({
          title: '请选择文件', // 提示信息
          icon: 'none'
        });
        return;
      }

      // 调用上传 API
      upload(this.filePath).then((res) => {
        uni.showToast({
          title: '上传成功', // 上传成功提示
          icon: 'success'
        });
      }).catch((err) => {
        uni.showToast({
          title: '上传失败', // 上传失败提示
          icon: 'none'
        });
      });
    }
  }
};
</script>

<style lang="scss">
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-color: #f8f8f8; // 浅灰背景
  height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; // 系统字体
}

.file-selector {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff; // 白色背景
  padding: 20px;
  border-radius: 12px; // 更大圆角
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); // 柔和阴影
}

.button {
  background-color: #4EA8EC; // 蓝色背景
  color: #fff; // 白色文字
  padding: 12px 24px;
  border: none;
  border-radius: 12px; // 圆角
  margin: 10px 0; // 按钮间距
  font-size: 17px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s; // 背景颜色和阴影过渡效果
}

.button:hover {
  background-color: #005bb5; // 悬停时的背景颜色
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2); // 悬停时的阴影
}

.upload-button {
  background-color: #34c759; // 绿色背景
}

.upload-button:hover {
  background-color: #28a745; // 悬停时的绿色背景
}

.file-name {
  margin-top: 15px; // 上方间距
  font-size: 16px;
  color: #333; // 深色文字
  font-weight: 500; // 字体加粗
  text-align: center; // 文字居中
  animation: fadeIn 0.5s ease-in-out, glow 1.5s infinite alternate; // 渐入和闪烁动画
}
</style>
