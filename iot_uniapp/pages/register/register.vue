<!-- 蓝色注册页面 -->
<template>
	<view>
		<view class="img-a">
			<view class="t-b">
				您好，
				<br />
				欢迎注册XXX
			</view>
		</view>
		<view class="login-view">
			<view class="t-login">
				<form class="cl">
					<view class="t-a">
						<text class="txt">用户ID</text>
						<input type="text" name="userId" placeholder="请输入您的用户ID" v-model="userId" />
					</view>
					<view class="t-a">
						<text class="txt">用户名</text>
						<input type="text" name="userName" placeholder="请输入您的用户名" v-model="userName" />
					</view>
					<view class="t-a">
						<text class="txt">密码</text>
						<input type="password" name="password" maxlength="18" placeholder="请输入您的密码" v-model="password" />
					</view>
					<view class="t-a">
						<text class="txt">确认密码</text>
						<input type="password" name="confirmPassword" maxlength="18" placeholder="请确认您的密码" v-model="confirmPassword" />
					</view>
<!-- 					<view class="t-a">
						<text class="txt">金额</text>
						<input type="number" name="money" placeholder="请输入金额" v-model="money" />
					</view> -->
					<button type="button" @tap="register">注 册</button>
				</form>
				<view class="t-f"><text>—————— 第三方账号注册 ——————</text></view>
				<view class="t-e cl">
					<view class="t-g" @tap="wxRegister"><image src="@/static/wx.png"></image></view>
					<view class="t-g" @tap="zfbRegister"><image src="@/static/qq.png"></image></view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { login, register } from '@/common/api.js';  // 引入login方法
export default {
	data() {
		return {
			userId: '', // 用户ID
			userName: '', // 用户名
			password: '', // 密码
			confirmPassword: '', // 确认密码
			money: 0 // 金额
		};
	},
	methods: {
		// 当前注册按钮操作
		register() {
			if (!this.userId) {
				uni.showToast({ title: '请输入您的用户ID', icon: 'none' });
				return;
			}
			if (!this.userName) {
				uni.showToast({ title: '请输入您的用户名', icon: 'none' });
				return;
			}
			if (!this.password) {
				uni.showToast({ title: '请输入您的密码', icon: 'none' });
				return;
			}
			if (this.password !== this.confirmPassword) {
				uni.showToast({ title: '两次输入的密码不一致', icon: 'none' });
				return;
			}
			// if (this.money === null || this.money === '') {
			// 	uni.showToast({ title: '请输入金额', icon: 'none' });
			// 	return;
			// }
			const message = {
			  topic: '/post',
			  msg: JSON.stringify({
				   userId: this.userId,
				   userName: this.userName,
				   password: this.password,
				   money: this.money
				  }),
			  qos: 2,
			};
			register({userId: this.userId,userName: this.userName,
				   password: this.password,money: this.money})
						  .then((res) => {
						    console.log(res); // 输出返回数据
						    // 确保 res 和 res.data 已经正确定义
						    if (res.statusCode === 200 && res.data && res.data.code === 1) {
						      uni.showToast({ title: '注册成功！', icon: 'none' });
						      // 注册成功后跳转到主页面
						      setTimeout(() => {
						        uni.switchTab({
						          url: '/pages/login/login'
						        });
						      }, 1500); // 延迟1.5秒后跳转
						    } else {
						      uni.showToast({ title: res.data ? res.data.msg || '注册失败，请重试' : '注册失败，请重试', icon: 'none' });
						    }
						  })
						  .catch((err) => {
						    uni.showToast({ title: '注册失败，请重试', icon: 'none' });
						  });
			
					},
					wxLogin() {
						uni.showToast({ title: '微信登录', icon: 'none' });
					},
					zfbLogin() {
						uni.showToast({ title: '支付宝登录', icon: 'none' });
					}
				}
			};
</script>

<style>
.txt {
	font-size: 32rpx;
	font-weight: bold;
	color: #333333;
}
input {
	border-radius: 30rpx;
}
.img-a {
	width: 100%;
	height: 450rpx;
	background-image: url(../../static/head.png);
	background-size: 100%;
}
.reg {
	font-size: 28rpx;
	color: #fff;
	height: 90rpx;
	line-height: 90rpx;
	border-radius: 50rpx;
	font-weight: bold;
	background: #f5f6fa;
	color: #000000;
	text-align: center;
	margin-top: 30rpx;
}
.login-view {
	width: 100%;
	position: relative;
	margin-top: -120rpx;
	background-color: #ffffff;
	border-radius: 8% 8% 0% 0;
}
.t-login {
	width: 600rpx;
	margin: 0 auto;
	font-size: 28rpx;
	padding-top: 80rpx;
}
.t-login button {
	font-size: 28rpx;
	background: #2796f2;
	color: #fff;
	height: 90rpx;
	line-height: 90rpx;
	border-radius: 50rpx;
	font-weight: bold;
}
.t-login input {
	height: 90rpx;
	line-height: 90rpx;
	margin-bottom: 50rpx;
	border-bottom: 1px solid #e9e9e9;
	font-size: 28rpx;
}
.t-login .t-a {
	position: relative;
}
.t-b {
	text-align: left;
	font-size: 42rpx;
	color: #ffffff;
	padding: 130rpx 0 0 70rpx;
	font-weight: bold;
	line-height: 70rpx;
}
.t-login .t-c {
	position: absolute;
	right: 22rpx;
	top: 22rpx;
	background: #5677fc;
	color: #fff;
	font-size: 24rpx;
	border-radius: 50rpx;
	height: 50rpx;
	line-height: 50rpx;
	padding: 0 25rpx;
}
.t-login .t-d {
	text-align: center;
	color: #999;
	margin: 80rpx 0;
}
.t-login .t-e {
	text-align: center;
	width: 250rpx;
	margin: 80rpx auto 0;
}
.t-login .t-g {
	float: left;
	width: 50%;
}
.t-login .t-e image {
	width: 50rpx;
	height: 50rpx;
}
.t-login .t-f {
	text-align: center;
	margin: 150rpx 0 0 0;
	color: #666;
}
.t-login .t-f text {
	margin-left: 20rpx;
	color: #aaaaaa;
	font-size: 27rpx;
}
.t-login .uni-input-placeholder {
	color: #aeaeae;
}
.cl {
	zoom: 1;
}
.cl:after {
	clear: both;
	display: block;
	visibility: hidden;
	height: 0;
	content: '\20';
}
</style>
