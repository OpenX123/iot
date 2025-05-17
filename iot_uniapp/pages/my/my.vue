<template>
  <view class="container">
    <!-- 顶部用户信息区域 -->
    <view class="user-info">
      <!-- 用户头像 -->
      <image class="avatar" :src="user.avatar || '../../static/avatar.jpg'" />
      <view class="user-details">
        <!-- 用户名 -->
        <text class="username">{{ user.userName }}</text>
        <!-- 用户ID -->
        <text class="user-id">ID: {{ user.userId }}</text>
      </view>
      <!-- 编辑信息按钮 -->
      <image src="@/static/editUser.png" class="edit-button" @click="showEditModal" />
    </view>

    <!-- 菜单项 -->
    <view class="menu">
		<view class="menu-item" @click="cameraData">
		  <text class="menu-text">设备监控</text>
		  <image class="menu-icon" src="../../static/Camera.png" />
		</view>
      <view class="menu-item" @click="showAddModal">
        <text class="menu-text">增加电量</text>
        <image class="menu-icon" src="../../static/addMoney.png?v=1" />
      </view>
      <view class="menu-item" @click="toUsers">
        <text class="menu-text">设备信息</text>
        <image class="menu-icon" src="../../static/allUsers.png?v=1" />
      </view>
	  <view class="menu-item" @click="moneyRecord">
	    <text class="menu-text">消费记录</text>
	    <image class="menu-icon" src="../../static/moneyRecord.png?v=1" />
	  </view>
	  <view class="menu-item" @click="uploadFile">
	    <text class="menu-text">上传文件</text>
	    <image class="file-icon" src="../../static/uploadFile(1).png" />
	  </view>
	  <view class="menu-item" @click="drawmap">
	    <text class="menu-text">地图</text>
	    <image class="file-icon" src="../../static/MAP.png" />
	  </view>
    </view>

    <!-- 编辑信息的模态窗口 -->
    <view v-if="isEditModalVisible" class="modal-overlay">
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">编辑用户信息</text>
          <button @click="hideEditModal" class="close-button">X</button>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">用户名:</text>
            <input class="form-input" v-model="editUser.userName" placeholder="请输入新的用户名" />
          </view>
          <view class="form-item">
            <text class="form-label">密码:</text>
            <input class="form-input" v-model="editUser.password" type="password" placeholder="请输入新的密码" />
          </view>
        </view>
        <view class="modal-footer">
          <button @click="updateUserInfo" class="submit-button">提交</button>
        </view>
      </view>
    </view>

    <!-- 增加金额的模态窗口 -->
    <view v-if="isAddModalVisible" class="modal-overlay">
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">增加电量</text>
          <button @click="hideAddModal" class="close-button">X</button>
        </view>
        <view class="modal-body">
          <view class="amount-options">
            <button v-for="amount in amountOptions" :key="amount" class="amount-button" :class="{ selected: selectedAmount === amount }" @click="selectAmount(amount)">
              {{ amount }}
            </button>
          </view>
          <view class="form-item">
            <text class="form-label">自定义电量:</text>
            <input class="form-input" type="number" v-model="customAmount" placeholder="请输入自定义电量" />
          </view>
        </view>
        <view class="modal-footer">
          <button @click="submitAmount" class="submit-button">充值</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { findByUserId, update, addMoney, publishMessage } from '@/common/api.js'; // 引入findByUserId、update和addMoney方法

export default {
  data() {
    return {
      user: {}, // 存储用户信息
      editUser: {
        userName: '', // 编辑用户名
        password: '' // 编辑密码
      },
      isEditModalVisible: false, // 控制编辑模态窗口的显示与隐藏
      isAddModalVisible: false, // 控制增加模态窗口的显示与隐藏
      amountOptions: [10, 20, 50, 100], // 预设金额选项
      selectedAmount: 100, // 选中的预设金额
      customAmount: 0 // 自定义金额
    };
  },
  onLoad() {
    const userId = uni.getStorageSync('userId'); // 从本地存储中获取用户ID
    if (userId) {
      this.fetchUserData(userId); // 获取用户数据
    } else {
      uni.showToast({ title: '用户未登录', icon: 'none' }); // 用户未登录提示
    }
  },
  methods: {
	cameraData(){
		uni.navigateTo({
			url: '/pages/camera/camera'
		});
	},
    toUsers() {
		uni.navigateTo({
			url: '/pages/users/users'
		});
    },
	moneyRecord() {
		uni.navigateTo({
		  url: '/pages/moneyRecord/moneyRecord'
		});
	},
	uploadFile(){
	uni.navigateTo({
	  url: '/pages/uploadFile/uploadFile'
	});	
	},
	drawmap(){
	uni.navigateTo({
	  url: '/pages/map/map'
	});	
	},
    // 获取用户数据
    fetchUserData(userId) {
      findByUserId(userId)
        .then((res) => {
          if (res.statusCode === 200 && res.data && res.data.code === 1) {
            this.user = res.data.data; // 更新用户信息
            this.editUser.userName = this.user.userName; // 初始化编辑用户名
            // uni.showToast({ title: '用户数据获取成功', icon: 'none' });
          } else {
            uni.showToast({ title: res.data ? res.data.msg || '获取用户数据失败' : '获取用户数据失败', icon: 'none' });
          }
        })
        .catch((err) => {
          uni.showToast({ title: '获取用户数据失败，请重试', icon: 'none' });
        });
    },
    // 显示编辑模态窗口
    showEditModal() {
      this.isEditModalVisible = true;
    },
    // 隐藏编辑模态窗口
    hideEditModal() {
      this.isEditModalVisible = false;
    },
    // 显示增加模态窗口
    showAddModal() {
      this.isAddModalVisible = true;
    },
    // 隐藏增加模态窗口
    hideAddModal() {
      this.isAddModalVisible = false;
    },
    // 选择金额
    selectAmount(amount) {
      this.selectedAmount = amount;
      this.customAmount = 0; // 清空自定义金额
    },
    // 提交金额
    submitAmount() {
      const amount = this.customAmount > 0 ? this.customAmount : this.selectedAmount;
      const message = {
        topic: '/post',
        msg: JSON.stringify({ Money_add: amount }),
        qos: 2,
      };

      // 发送请求的方法
      const sendRequest = () => {
        return publishMessage(message).then((res) => {
          if (res.statusCode === 200 && res.data && res.data.code === 1) {
            uni.showToast({ title: '充值成功', icon: 'none' });
            this.hideAddModal(); // 关闭增加金额模态窗口
            return true; // 请求成功
          } else {
            uni.showToast({ title: res.data ? res.data.msg || '充值失败' : '充值失败', icon: 'none' });
            return false; // 请求失败
          }
        }).catch((err) => {
          console.error('请求失败:', err);
          uni.showToast({ title: '充值失败，请重试', icon: 'none' });
          return false; // 请求失败
        });
      };
      sendRequest();
    }
  }
};
</script>

<style>
.container {
  padding: 20rpx; /* 设置容器的内边距 */
  background-color: #f5f6fa; /* 设置容器的背景颜色 */
  min-height: 100vh; /* 设置容器的最小高度为100vh，确保全屏 */
}

.user-info {
  display: flex; /* 设置为弹性盒子布局 */
  align-items: center; /* 垂直方向居中对齐 */
  padding: 40rpx; /* 设置内边距 */
  background-color: #ffffff; /* 设置背景颜色 */
  border-radius: 8rpx; /* 设置圆角 */
  margin-bottom: 20rpx; /* 设置底部外边距 */
  position: relative; /* 使编辑按钮可以相对定位 */
}

.avatar {
  width: 120rpx; /* 设置头像宽度 */
  height: 120rpx; /* 设置头像高度 */
  border-radius: 60rpx; /* 设置圆形 */
  margin-right: 20rpx; /* 设置右边距 */
}

.user-details {
  display: flex; /* 设置为弹性盒子布局 */
  flex-direction: column; /* 设置为纵向排列 */
}

.username {
  font-size: 36rpx; /* 设置字体大小 */
  font-weight: bold; /* 设置字体加粗 */
  color: #333333; /* 设置字体颜色 */
}

.user-id {
  font-size: 28rpx; /* 设置字体大小 */
  color: #999999; /* 设置字体颜色 */
}

.edit-button {
  position: absolute; /* 绝对定位 */
  top: 76rpx; /* 距离顶部20rpx */
  right: 20rpx; /* 距离右侧20rpx */
  width: 50rpx; /* 设置宽度 */
  height: 50rpx; /* 设置高度 */
  cursor: pointer; /* 设置鼠标指针样式 */
}

.menu {
  background-color: #ffffff; /* 设置背景颜色 */
  border-radius: 8rpx; /* 设置圆角 */
  display: flex; /* 设置为弹性盒子布局 */
  flex-direction: column; /* 设置为纵向排列 */
  gap: 10rpx; /* 设置各项间距 */
}

.menu-item {
  display: flex; /* 设置为弹性盒子布局 */
  align-items: center; /* 垂直方向居中对齐 */
  justify-content: space-between; /* 水平方向两端对齐 */
  padding: 30rpx; /* 设置内边距 */
  border-bottom: 1px solid #eeeeee; /* 设置底部边框 */
}

.menu-item:last-child {
  border-bottom: none; /* 移除最后一个菜单项的底部边框 */
}

.menu-text {
  font-size: 32rpx; /* 设置字体大小 */
  color: #333333; /* 设置字体颜色 */
}

.menu-icon {
  width: 42rpx; /* 设置图标宽度 */
  height: 42rpx; /* 设置图标高度 */
}
.file-icon {
  width: 48rpx; /* 设置图标宽度 */
  height: 48rpx; /* 设置图标高度 */
}

.modal-overlay {
  position: fixed; /* 固定定位 */
  top: 0;
  left: 0;
  width: 100%; /* 设置宽度为100% */
  height: 100%; /* 设置高度为100% */
  background-color: rgba(0, 0, 0, 0.5); /* 设置背景颜色和透明度 */
  display: flex; /* 设置为弹性盒子布局 */
  align-items: center; /* 垂直方向居中对齐 */
  justify-content: center; /* 水平方向居中对齐 */
  z-index: 1000; /* 设置层级 */
}

.modal-content {
  background-color: #ffffff; /* 设置背景颜色 */
  width: 80%; /* 设置宽度为80% */
  padding: 20rpx; /* 设置内边距 */
  border-radius: 10rpx; /* 设置圆角 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 设置阴影 */
  position: relative; /* 相对定位 */
}

.modal-header {
  display: flex; /* 设置为弹性盒子布局 */
  justify-content: space-between; /* 水平方向两端对齐 */
  align-items: center; /* 垂直方向居中对齐 */
  margin-bottom: 20rpx; /* 设置底部外边距 */
}

.modal-title {
  font-size: 36rpx; /* 设置字体大小 */
  font-weight: bold; /* 设置字体加粗 */
}

.close-button {
  font-size: 30rpx; /* 设置字体大小 */
  background: none; /* 移除背景 */
  border: none; /* 移除边框 */
  position: absolute; /* 绝对定位 */
  top: 10rpx; /* 距离顶部10rpx */
  right: 10rpx; /* 距离右侧10rpx */
}

.modal-body {
  margin-bottom: 20rpx; /* 设置底部外边距 */
}

.form-item {
  margin-bottom: 20rpx; /* 设置底部外边距 */
}

.form-label {
  font-size: 32rpx; /* 设置字体大小 */
  color: #333333; /* 设置字体颜色 */
}

.form-input {
  width: 90%; /* 设置宽度为100% */
  padding: 20rpx; /* 设置内边距 */
  font-size: 28rpx; /* 设置字体大小 */
  border: 1px solid #eeeeee; /* 设置边框 */
  border-radius: 8rpx; /* 设置圆角 */
  background-color: #ffffff; /* 设置背景颜色 */
}

.submit-button {
  width: 35%; /* 设置宽度为100% */
  padding: 8rpx; /* 设置内边距 */
  font-size: 30rpx; /* 设置字体大小 */
  color: #ffffff; /* 设置字体颜色 */
  background-color: #007aff; /* 设置背景颜色 */
  border: none; /* 移除边框 */
  border-radius: 8rpx; /* 设置圆角 */
  text-align: center; /* 文本居中 */
}

.amount-options {
  display: flex; /* 设置为弹性盒子布局 */
  flex-wrap: wrap; /* 自动换行 */
  margin-bottom: 20rpx; /* 设置底部外边距 */
}

.amount-button {
  flex: 1; /* 每个按钮平分剩余空间 */
  padding: 20rpx; /* 设置内边距 */
  margin: 5rpx; /* 设置外边距 */
  font-size: 32rpx; /* 设置字体大小 */
  text-align: center; /* 文本居中 */
  background-color: #f0f0f0; /* 设置背景颜色 */
  border: 1px solid #dcdcdc; /* 设置边框 */
  border-radius: 8rpx; /* 设置圆角 */
}

.amount-button.selected {
  background-color: #007aff; /* 设置选中状态背景颜色 */
  color: #ffffff; /* 设置选中状态字体颜色 */
}

/* 响应式布局 */
@media (min-width: 600px) {
  .modal-content {
    width: 60%; /* 中屏幕时宽度为60% */
  }
}

@media (min-width: 900px) {
  .modal-content {
    width: 40%; /* 大屏幕时宽度为40% */
  }
}
</style>
