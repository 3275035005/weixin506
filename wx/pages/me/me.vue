<template>
	<view style="padding: 20rpx;">
		<view class="box" style="display: flex; align-items: center; margin-bottom: 20rpx;">
			<view style="width: 120rpx; height: 120rpx;">
				<image :src="user.avatar" style="width: 100%; height: 100%; border-radius: 50%;"></image>
			</view>
			 <view style="margin-left: 30rpx;">
				 <view style="font-size: 32rpx;">{{ user.username }}</view>
				 <view style="color: #999;">{{ user.name }}</view>
			 </view>
		</view>
		
		<view class="box" style="margin-bottom: 20rpx;">
			<view style="margin-bottom: 20rpx; font-size: 28rpx;">我的功能</view>
			<view style="display: flex; align-items: center;">
				<view style="flex: 1; text-align: center;" @click="goPage('/pages/address/address')">
					<image mode="widthFix" src="@/static/icons/地址.png" style="width: 40%;"></image>
					<view>收货地址</view>
				</view>
				<view style="flex: 1; text-align: center;" @click="goPage('/pages/collect/collect')">
					<image mode="widthFix" src="@/static/icons/收藏.png" style="width: 40%;"></image>
					<view>我的收藏</view>
				</view>
				<view style="flex: 1; text-align: center;" @click="goPage('/pages/comment/comment')">
					<image mode="widthFix" src="@/static/icons/评价.png" style="width: 40%;"></image>
					<view>评价中心</view>
				</view>
				<view style="flex: 1; text-align: center;" @click="goPage('/pages/browsing/browsing')">
					<image mode="widthFix" src="@/static/icons/查看.png" style="width: 40%;"></image>
					<view>浏览历史</view>
				</view>
			</view>
		</view>
		
		<view class="box">
			<view style="border-bottom: 2rpx solid #eee; padding: 15rpx 0; 
				display: flex; align-items: center; justify-content: space-between;" @click="goPage('/pages/person/person')">
				<view>
					<uni-icons type="person" style="margin-right: 10rpx; position: relative; top: 2rpx;"></uni-icons>个人信息
				</view>
				<uni-icons type="forward" color="#999"></uni-icons>
			</view>
			<view style="border-bottom: 2rpx solid #eee; padding: 15rpx 0; 
				display: flex; align-items: center; justify-content: space-between;" @click="goPage('/pages/about/about')">
				<view>
					<uni-icons type="help" style="margin-right: 10rpx; position: relative; top: 2rpx;"></uni-icons>关于我们
				</view>
				<uni-icons type="forward" color="#999"></uni-icons>
			</view>
			<view style="border-bottom: 2rpx solid #eee; padding: 15rpx 0; 
				display: flex; align-items: center; justify-content: space-between;" @click="goPage('/pages/agreement/agreement')">
				<view>
					<uni-icons type="info" style="margin-right: 10rpx; position: relative; top: 2rpx;"></uni-icons>用户协议
				</view>
				<uni-icons type="forward" color="#999"></uni-icons>
			</view>
			<view style="border-bottom: 2rpx solid #eee; padding: 15rpx 0; 
				display: flex; align-items: center; justify-content: space-between;" v-if="user.id" @click="logout">
				<view>
					<uni-icons type="undo" style="margin-right: 10rpx; position: relative; top: 2rpx;"></uni-icons>退出登录
				</view>
				<uni-icons type="forward" color="#999"></uni-icons>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: {}
			}
		},
		onShow() {
			this.user = uni.getStorageSync('xm-user')
		},
		methods: {
			unDo() {
				uni.showToast({
					icon: 'none',
					title: '功能开发中'
				})
			},
			logout() {
				uni.removeStorageSync('xm-user')
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			goPage(url) {
				uni.navigateTo({
					url: url
				})
			}
		}
	}
</script>

<style>

</style>
