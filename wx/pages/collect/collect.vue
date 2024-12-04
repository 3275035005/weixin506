<template>
	<view style="padding: 20rpx;">
		<view class="box" v-for="item in collectList" :key="item.id">
			<view style="display: flex; margin-bottom: 10rpx;" @click="goDetail(item.business.id)">
				<view style="width: 160rpx; height: 160rpx;">
					<image :src="item.business.avatar" mode="scaleToFill" style="width: 100%; height: 100%; border-radius: 10rpx;"></image>
				</view>
				<view style="flex: 1; display: flex; flex-direction: column; margin-left: 10px;">
					<view style="font-size: 32rpx; font-weight: bold; margin-bottom: 10rpx;">{{ item.business.name }}</view>
					<view style=" margin-bottom: 6rpx; display: flex; justify-content: space-between;">
						<view style="flex: 1;">
							<text style="color: orange; font-weight: 700;">{{ item.business.star }}</text>
							<text style="color: #999; margin-left: 10rpx;">已售{{ item.business.saleCount }}</text>
						</view>
						<view style="color: #999;">30分钟内送达</view>
					</view>
					<view style="color: orange; margin-bottom: 6rpx; ">免配送费</view>
					<view style="color: brown; font-size: 12px; background-color: #ffe6b8; border-radius: 4rpx; 
						padding: 0 6rpx; width: fit-content;">{{ item.business.info }}</view>
				</view>
			</view>
			
			<view style="text-align: right;" @click="delCollect(item.id)">
				<uni-icons type="trash" color="#999" style="position: relative; top: 3rpx;"></uni-icons>
				<text style="color: #999;">删除</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				collectList: [],
				user: uni.getStorageSync('xm-user')
			}
		},
		onLoad() {
			this.loadCollect()
		},
		methods: {
			goDetail(businessId) {
				uni.navigateTo({
					url: '/pages/detail/detail?businessId=' + businessId
				})
			},
			delCollect(id) {
				this.$request.del('/collect/delete/' + id).then(res => {
					uni.showToast({
						icon: 'none',
						title: '操作成功'
					})
					this.loadCollect()
				})
			},
			loadCollect() {
				let params = { status: '通过', userId: this.user.id  }
				this.$request.get('/collect/selectAll', params).then(res => {
					this.collectList = res.data || []
				})
			},
		}
	}
</script>

<style>

</style>
