<template>
  <view class="container">
    <!-- 显示加载中的提示 -->
    <view v-if="loading" class="loading">Loading...</view>
    <!-- 如果不在加载中，显示内容 -->
    <view v-else>
      <!-- 如果有错误信息，显示错误 -->
      <view v-if="error" class="error">{{ error }}</view>
      <!-- 否则，显示用户信息 -->
      <view v-else>
        <select v-model="selectedUserId" @change="handleUserChange" class="user-select">
          <option value="">选择用户</option>
          <option v-for="user in users" :key="user.userId" :value="user.userId">
            {{ user.userId }}
          </option>
        </select>
        <button @click="exportToExcel" class="export-button">导出为 Excel</button>
        <table class="user-table">
          <thead>
            <tr>
              <th>用户ID</th>
              <th>钱</th>
              <th>时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(record, index) in records" :key="record.userId + '-' + index">
              <td>{{ record.userId }}</td>
              <td>{{ record.amount }}元</td>
              <td>{{ formatTimestamp(record.timestamp) }}</td>
            </tr>
          </tbody>
        </table>
      </view>
    </view>
  </view>
</template>

<script>
import { getAllRecord, getRecordByUserId } from '@/common/api.js';
import * as XLSX from 'xlsx';

export default {
  data() {
    return {
      records: [], // 存储用户信息
      users: [], // 存储所有用户
      selectedUserId: '', // 存储选定的用户ID
      loading: true, // 标识是否正在加载
      error: null, // 存储错误信息
      intervalId: null ,// 存储定时器ID
	  flash: 30000
    };
  },
  methods: {
    async fetchRecords() {
      try {
        const res = await getAllRecord();
        // 检查请求是否成功
        if (res.statusCode === 200 && res.data && res.data.code === 1) {
          this.records = res.data.data; // 更新用户数据
          // 提取唯一用户ID
          const userSet = new Set(this.records.map(record => record.userId));
          this.users = Array.from(userSet).map(userId => ({ userId }));
        } else {
          // 请求失败，设置错误信息
          this.error = 'Failed to fetch records: ' + (res.data.message || 'Unknown error');
        }
      } catch (error) {
        // 捕获并设置错误信息
        this.error = 'Failed to fetch records: ' + error.message;
        console.error('Failed to fetch records:', error);
      } finally {
        // 设置加载状态为完成
        this.loading = false;
      }
    },
    async fetchRecordData(userId) {
      this.loading = true;
      try {
        const res = await getRecordByUserId(userId);
        console.log(res);
        if (res.statusCode === 200 && res.data && res.data.code === 1) {
          this.records = res.data.data; // 更新用户信息
          // uni.showToast({ title: '用户消费记录数据获取成功', icon: 'none' });
        } else {
          this.error = res.data ? res.data.msg || '获取用户消费记录数据失败' : '获取用户消费记录数据失败';
          uni.showToast({ title: this.error, icon: 'none' });
        }
      } catch (err) {
        this.error = '获取用户消费记录数据失败，请重试';
        uni.showToast({ title: this.error, icon: 'none' });
      } finally {
        this.loading = false;
      }
    },
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
    exportToExcel() {
      // 创建工作簿和工作表
      const ws = XLSX.utils.json_to_sheet(this.records.map(record => ({
        用户ID: record.userId,
        钱: record.amount + '元',
        时间: this.formatTimestamp(record.timestamp)
      })));

      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "用户记录");

      // 生成并下载 Excel 文件
      XLSX.writeFile(wb, "用户记录.xlsx");
    },
    handleUserChange() {
      if (this.intervalId) {
        clearInterval(this.intervalId);
      }
      if (this.selectedUserId) {
        this.fetchRecordData(this.selectedUserId);
        this.intervalId = setInterval(() => {
          this.fetchRecordData(this.selectedUserId);
        }, this.flash);
      } else {
        this.fetchRecords();
        this.intervalId = setInterval(this.fetchRecords, this.flash);
      }
    }
  },
  mounted() {
    // 组件挂载后获取用户数据
    this.fetchRecords();
    // 启动定时器每隔4秒刷新数据
    this.intervalId = setInterval(this.fetchRecords, this.flash);
  },
  beforeDestroy() {
    // 清除定时器
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }
}
</script>

<style lang="scss">
.container {
  padding: 20px;
}

.loading {
  text-align: center;
  font-size: 18px;
}

.error {
  color: red;
  text-align: center;
  font-size: 18px;
}

.user-select {
  margin-bottom: 20px;
  padding: 10px;
  width: 100%;
  font-size: 16px;
}

.export-button {
  margin-bottom: 20px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.export-button:hover {
  background-color: #45a049;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.user-table th,
.user-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.user-table th {
  background-color: #f2f2f2;
  color: #333;
}

.user-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.user-table tr:hover {
  background-color: #ddd;
}

</style>
