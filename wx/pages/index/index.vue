<template>
	<view>
		<view>
			<uni-search-bar @confirm="search" :focus="false" placeholder="搜索商家"></uni-search-bar>
		</view>
		
		<swiper circular autoplay :interval="3000" :duration="500" indicator-dots style="height: 350rpx;"
			indicator-color="rgba(255, 255, 255, 0.6)" indicator-active-color="#3CB371">
			<swiper-item v-for="item in banners" :key="item.id">
				<navigator :url="'/pages/detail/detail?businessId=' + item.businessId">
					<image :src="item.img" alt="" mode="widthFix" style="width: 100%;" />
				</navigator>
			</swiper-item>
		</swiper>

		<view style="padding: 20rpx;">
			<view class="box" style="display: flex; align-items: center; margin-bottom: 20rpx;">
				<uni-icons type="sound" size="18"></uni-icons>
				<view>{{ notice }}</view>
			</view>
			
			<view class="box" style="margin-bottom: 20rpx;">
				<uni-row>
					<uni-col :span="6">
						<view class="grid-item-box" @click="goBusiness('奶茶饮品')">
							<image mode="widthFix" style="width: 50%;" src="../../static/imgs/奶茶.png"></image>
							<text style="margin-top: 10rpx;">奶茶饮品</text>
						</view>
					</uni-col>
					<uni-col :span="6">
						<view class="grid-item-box" @click="goBusiness('轻食简餐')">
							<image mode="widthFix" style="width: 50%;" src="../../static/imgs/轻食.png"></image>
							<text style=" margin-top: 10rpx;">轻食简餐</text>
						</view>
					</uni-col>
					<uni-col :span="6">
						<view class="grid-item-box" @click="goBusiness('炸鸡汉堡')">
							<image mode="widthFix" style="width: 50%;" src="../../static/imgs/汉堡.png"></image>
							<text style=" margin-top: 10rpx;">炸鸡汉堡</text>
						</view>
					</uni-col>
					<uni-col :span="6">
						<view class="grid-item-box" @click="goBusiness('特色美味')">
							<image mode="widthFix" style="width: 50%;" src="../../static/imgs/美食.png"></image>
							<text style="margin-top: 10rpx;">特色美味</text>
						</view>
					</uni-col>
				</uni-row>
			</view>
			
			<view>
				<view class="box" style="margin-bottom: 10rpx; font-size: 32rpx; font-weight: bold; color: #f22942;">推荐商家</view>
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

	</view>
</template>

<script>
	export default {
		data() {
			return {
				banners: [],
				notices: [],
				notice: '',
				business: [],
			}
		},
		onShow() {
			this.loadBanner()
			this.loadNotice()
			this.loadBusiness()
		},
		methods: {
			goBusiness(type) {
				uni.navigateTo({
					url: '/pages/business/business?type=' + type + '&event=category'
				})
			},
			search(search) {
				console.log(search)
				uni.navigateTo({
					url: '/pages/business/business?name=' + search.value + '&event=search'
				})
			},
			loadBanner() {
				this.$request.get('/banner/selectAll').then(res => {
					this.banners = res.data
				})
			},
			loadNotice() {
				this.$request.get('/notice/selectAll').then(res => {
					this.notices = res.data || []
					if (this.notices.length) {
						let i = 0
						this.notice = this.notices[i++].content // 默认显示一个公告  i = 0
						setInterval(() => {
							this.notice = this.notices[i++].content // i = 1,2,3
							if (i === this.notices.length) {
								i = 0
							}
						}, 5000)
					}
				})
			},
			loadBusiness() {
				this.$request.get('/business/getHomeBusiness').then(res => {
					this.business = res.data
				})
			},
			goDetail(businessId) {
				uni.navigateTo({
					url: '/pages/detail/detail?businessId=' + businessId
				})
			}
		}
	}
</script>

<style>
	.grid-item-box {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.box {
		padding: 10px;
		background-color: white;
		border-radius: 5px;
		box-shadow: 0 2px 10px rgba(0, 0, 0, .1);
	}
</style>