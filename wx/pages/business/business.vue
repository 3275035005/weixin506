<template>
	<view>
		<view v-if="event === 'search'">
			<uni-search-bar @confirm="search" :focus="false" v-model="name" placeholder="搜索商家"></uni-search-bar>
		</view>
		<view style="padding: 20rpx;">
			<view class="box" v-for="item in business" :key="item.id" style="display: flex; margin-bottom: 10rpx;" @click="goDetail(item.id)">
				<view style="width: 160rpx; height: 160rpx;">
					<image :src="item.avatar" mode="scaleToFill" style="width: 100%; height: 100%; border-radius: 10rpx;"></image>
				</view>
				<view style="flex: 1; display: flex; flex-direction: column; margin-left: 10px;">
					<view style="font-size: 32rpx; font-weight: bold; margin-bottom: 10rpx;">{{ item.name }}</view>
					<view style=" margin-bottom: 6rpx; display: flex; justify-content: space-between;">
						<view style="flex: 1;">
							<text style="color: orange; font-weight: 700;">{{ item.star }}分</text>
							<text style="color: #999; margin-left: 10rpx;">月售{{ item.saleCount }}</text>
						</view>
						<view style="color: #999;">30分钟内送达</view>
					</view>
					<view style="color: orange; margin-bottom: 6rpx; ">免配送费</view>
					<view style="color: brown; font-size: 12px; background-color: #ffe6b8; border-radius: 4rpx; padding: 0 6rpx; width: fit-content;">{{ item.info }}</view>
				</view>
			</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				name: '',
				type: '',
				event: '',   // 跳转类型
				business: []
			}
		},
		onLoad(option) {
			this.name = option.name
			this.type = option.type
			this.event = option.event
			
			this.loadBusiness()
		},
		methods: {
			goDetail(businessId) {
				uni.navigateTo({
					url: '/pages/detail/detail?businessId=' + businessId
				})
			},
			search(search) {
				this.loadBusiness()
			},
			loadBusiness() {
				let params = { status: '通过' }
				if (this.name) {
					params.name = this.name
				}
				if (this.type) {
					params.type = this.type
				}
				this.$request.get('/business/selectAll', params).then(res => {
					this.business = res.data || []
				})
			},
		}
	}
</script>

<style>

</style>
