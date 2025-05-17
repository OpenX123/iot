// common/api.js
// const BASE_URL = 'http://114.132.83.76:1666';
const BASE_URL = 'http://localhost:8088';
const request = (url, method, data) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}${url}`,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res);
        } else {
          reject(res);
        }
      },
      fail: (err) => {
        reject(err);
      }
    });
  });
};
const uploadFile = (url, filePath, name) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: `${BASE_URL}${url}`,
      filePath: filePath,
      name: name,
      success: (uploadFileRes) => {
        if (uploadFileRes.statusCode === 200) {
          resolve(JSON.parse(uploadFileRes.data));
        } else {
          reject(uploadFileRes);
        }
      },
      fail: (error) => {
        reject(error);
      }
    });
  });
};
export const subscribeTopic = (data) => request('/SubsribeController/subscribe', 'POST', data);
export const unsubscribeTopic = (data) => request('/SubsribeController/unsubscribe', 'POST', data);
export const publishMessage = (data) => request('/publish', 'POST', data);
export const hello = () => request('/SubsribeController/hello', 'GET');
export const login = (data) => request('/user/login', 'POST', data);  // 登录
export const register = (data) => request('/user/register', 'POST', data);  // 注册
export const findByUserId = (userId) => request(`/user/find/${userId}`, 'GET');  // 查找
export const update = (data) => request('/user/update', 'PUT', data);  // 更新
export const sensorData = (data) => request('/sensor/getAll', 'GET', data); 
export const getAllUsers = () => request('/user/getAll', 'GET');//获取所有用户
export const getAllRecord = () => request('/user/getAllRecord', 'GET');//获取所有用户消费记录
export const getRecordByUserId = (userId) => request(`/user/getRecordByUserId/${userId}`, 'GET');//获取指定用户消费记录
// 新增的文件上传方法
export const upload = (filePath) => uploadFile('/file/upload', filePath, 'file');