<template>
  <view class="container">
    <!-- 显示加载中的提示 -->
    <view v-if="loading" class="loading">Loading...</view>
    <!-- 如果不在加载中，显示内容 -->
    <view v-else>
      <!-- 如果有错误信息，显示错误 -->
      <view v-if="error" class="error">{{ error }}</view>
      <!-- 否则，显示用户信息 -->
      <view class="user-list">
        <view v-for="user in users" :key="user.userId" class="user-card">
          <view class="user-info">
            <span class="label">车辆编号：</span>{{ user.userId }}
          </view>
          <view class="user-info">
            <span class="label">电池识别号：</span>{{ user.cid }}
          </view>
          <view class="user-info">
            <span class="label">用户名：</span>{{ user.userName }}
          </view>
          <view class="user-info">
            <span class="label">密码：</span>********
          </view>
          <view :class="['user-info', { 'alarm': user.tempWaring == 1 }]">
            <span class="label">温度报警：</span>{{ user.tempWaring }}
          </view>
          <view :class="['user-info', { 'alarm': user.cwaring == 1 }]">
            <span class="label">电量：</span>{{ user.cwaring }}%
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getAllUsers } from '@/common/api.js';

export default {
  data() {
    return {
      users: [], // 存储用户信息
      loading: true, // 标识是否正在加载
      error: null // 存储错误信息
    };
  },
  methods: {
    formatMoney(money) {
      return parseFloat(money).toFixed(2);
    },
    async fetchUsers() {
      try {
        const res = await getAllUsers();
        console.log(res);
        // 检查请求是否成功
        if (res.statusCode === 200 && res.data && res.data.code === 1) {
          this.users = res.data.data; // 更新用户数据
        } else {
          // 请求失败，设置错误信息
          this.error = 'Failed to fetch users: ' + (res.data.message || 'Unknown error');
        }
      } catch (error) {
        // 捕获并设置错误信息
        this.error = 'Failed to fetch users: ' + error.message;
        console.error('Failed to fetch users:', error);
      } finally {
        // 设置加载状态为完成
        this.loading = false;
      }
    }
  },
  mounted() {
    // 组件挂载后获取用户数据
    this.fetchUsers();
  }
}
</script>

<style lang="scss">
.container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f7f8fa;
  min-height: 100vh;
}

.loading, .error {
  font-size: 18px;
  color: #ff4d4f;
  margin-top: 20px;
}

.user-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  width: 100%;
}

.user-card {
  background-color: #ffffff;
  border: 1px solid #e8e8e8;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin: 15px;
  padding: 20px;
  width: 100%;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.user-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 16px;
  width: 100%;
  color: #595959;
}

.label {
  font-weight: bold;
  color: #1f1f1f;
}

.user-info span {
  margin-right: 10px;
}

.alarm {
  color: #ff4d4f; /* 红色报警 */
}

/* 响应式布局 */
@media (min-width: 600px) {
  .user-card {
    max-width: 48%;
  }
}

@media (min-width: 900px) {
  .user-card {
    max-width: 30%;
  }
}

@media (min-width: 1200px) {
  .user-card {
    max-width: 23%;
  }
}
</style>
