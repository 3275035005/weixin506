<template>
	<view style="padding: 20rpx;">
		<view class="box" v-for="item in browsingList" :key="item.id">
			<view style="display: flex; margin-bottom: 10rpx;" @click="goDetail(item.id)">
				<view style="width: 160rpx; height: 160rpx;">
					<image :src="item.avatar" mode="scaleToFill" style="width: 100%; height: 100%; border-radius: 10rpx;"></image>
				</view>
				<view style="flex: 1; display: flex; flex-direction: column; margin-left: 10px;">
					<view style="font-size: 32rpx; font-weight: bold; margin-bottom: 10rpx;">{{ item.name }}</view>
					<view style=" margin-bottom: 6rpx; display: flex; justify-content: space-between;">
						<view style="flex: 1;">
							<text style="color: orange; font-weight: 700;">{{ item.star }}</text>
							<text style="color: #999; margin-left: 10rpx;">已售{{ item.saleCount }}</text>
						</view>
						<view style="color: #999;">30分钟内送达</view>
					</view>
					<view style="color: orange; margin-bottom: 6rpx; ">免配送费</view>
					<view style="color: brown; font-size: 12px; background-color: #ffe6b8; border-radius: 4rpx; 
						padding: 0 6rpx; width: fit-content;">{{ item.info }}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				browsingList: [],
				user: uni.getStorageSync('xm-user')
			}
		},
		onLoad() {
			this.loadBrowsing()
		},
		methods: {
			goDetail(businessId) {
				uni.navigateTo({
					url: '/pages/detail/detail?businessId=' + businessId
				})
			},
			loadBrowsing() {
				let params = { userId: this.user.id  }
				this.$request.get('/browsing/selectAll', params).then(res => {
					this.browsingList = res.data || []
				})
			},
		}
	}
</script>

<style>

</style>
