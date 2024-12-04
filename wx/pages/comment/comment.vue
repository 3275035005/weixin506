<template>
	<view style="padding: 20rpx;">
		<view class="box" v-if="comments.length">
			<view v-for="item in comments" :key="item.id" style=" border-bottom: 1rpx solid #eee; padding: 20rpx 0;">
				<view style="flex: 1; margin-bottom: 20rpx;" @click="goBusiness(item.businessId)">
					<text style="font-size: 32rpx; flex: 1; color: #000;">{{ item.business.name }}</text>
					<uni-icons type="forward" color="#999"></uni-icons>
				</view>
				<view style="display: flex; margin-bottom: 20rpx;">
					<view style="width: 100rpx; height: 100rpx; margin-right: 30rpx;">
						<image :src="item.user.avatar" style="width: 100%; height: 100%; border-radius: 50%;"></image>
					</view>
					<view style="flex: 1; display: flex; flex-direction: column; justify-content: space-between;">
						<view style="margin-bottom: 10rpx;">{{ item.content }}</view>
						<view style="display: flex; align-items: center; justify-content: space-between;">
							<view style="flex: 1;"><uni-rate :readonly="true" size="14" :max="5" :value="item.star" /></view>
							<view style="color: #999;">{{ item.time }}</view>
						</view>
					</view>
				</view>	
				<view style="text-align: right;" @click="delComment(item.id)">
					<uni-icons type="trash" style="position: relative; top: 3rpx;" color="#999"></uni-icons>
					<text style="color: #999;">删除</text>
				</view>	
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				comments: [],
				user: uni.getStorageSync('xm-user')
			}
		},
		onLoad() {
			this.loadComments()
		},
		methods: {
			delComment(id) {
				this.$request.del('/comment/delete/' + id).then(res => {
					uni.showToast({
						title: '操作成功'
					})
					this.loadComments()
				})
			},
			goBusiness(businessId) {
				uni.navigateTo({
					url: '/pages/detail/detail?businessId=' + businessId
				})
			},
			loadComments() {
				this.$request.get('/comment/selectAll?userId=' + this.user.id).then(res => {
					this.comments = res.data || []
				})
			},
		}
	}
</script>

<style>

</style>
