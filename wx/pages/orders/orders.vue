<template>
	<view style="padding: 20rpx;">
		<view style="margin-bottom: 20rpx;">
			<z-tabs :list="list" :current='current' @change="changeTab"></z-tabs>
		</view>
		<view>
			<view class="box" v-for="item in orderList" :key="item.id" style="margin-bottom: 10rpx;" >
				<view style="margin-bottom: 20rpx; display: flex; align-items: center;">
					<view style="flex: 1;" @click="goBusiness(item.businessId)">
						<text style="font-size: 32rpx; flex: 1; color: #000;">{{ item.businessName }}</text>
						<uni-icons type="forward" color="#999"></uni-icons>
					</view>
					<view style="color: #666;">{{ item.status }}</view>
				</view>
				<view style="display: flex;align-items: center; margin-bottom: 10rpx;" @click="goOrderItems(item.id)">
					<view style="width: 100rpx; height: 100rpx;">
						<image :src="item.cover" style="width: 100%; height: 100%; display: inline-block;" ></image>
					</view>
					<view style="flex: 1; margin: 0 20rpx; font-size: 28rpx;">{{ item.name }}</view>
					<view>实付￥<text style="color: red; font-size: 36rpx;">{{ item.actual }}</text></view>
				</view>
				
				<view style="display: flex; align-items: center; justify-content: space-between;">
					<view style="flex: 1;" @click="deleteOrder(item.id)">
						<view v-if="item.status !== '待付款' && item.status !== '待发货' && item.status !== '待收货'">
							<uni-icons style="position: relative; top: 3rpx;" type="trash" color="#999" ></uni-icons>
							<text style="color: #999;">删除</text>
						</view>
					</view>
					<view style="text-align: right;">
						<text class="mini-btn" v-if="item.status === '待收货'" style="color: coral; border-color: coral;" @click="changeStatus(item, '待评价')">确认收货</text>
						<text class="mini-btn" v-if="item.status === '待发货'" style="color: orange; border-color: orange" @click="changeStatus(item, '已退款')">申请退款</text>
						<text class="mini-btn" v-if="item.status === '待支付'" style="margin: 0 10rpx; color: #007AFF; border-color: #007AFF" @click="changeStatus(item, '待发货')">支付</text>
						<text class="mini-btn" v-if="item.status === '待支付'" style="color: red; border-color: red" @click="changeStatus(item, '已取消')">取消</text>
						<text class="mini-btn" v-if="item.status === '待评价'" style="color: #007AFF; border-color: #007AFF" @click="goComment(item.id, item.businessId)">评价</text>
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
				list: ['全部', '进行中', '待评价', '已退款'],
				orderList: [],
				current: 0,
				user: uni.getStorageSync('xm-user')
			}
		},
		onShow() {
			this.loadOrder()
		},
		methods: {
			goComment(orderId, businessId) {
				uni.navigateTo({
					url: '/pages/addComment/addComment?orderId=' + orderId + '&businessId=' + businessId
				})
			},
			goBusiness(businessId) {
				uni.navigateTo({
					url: '/pages/detail/detail?businessId=' + businessId
				})
			},
			goOrderItems(orderId) {
				uni.navigateTo({
					url: '/pages/orderItems/orderItems?orderId=' + orderId
				})
			},
			changeTab(index) {
				this.current = index
				let status = this.list[index]
				this.loadOrder(status)
			},
			changeStatus(orders, status) {
				let form = JSON.parse(JSON.stringify(orders))
				form.status = status
				this.$request.put('/orders/update/', form).then(res => {
					if (res.code === '200') {
						uni.showToast({
							title: '操作成功'
						})
						this.loadOrder(this.list[this.current])
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			},
			deleteOrder(id) {
				this.$request.del('/orders/delete/' + id).then(res => {
					if (res.code === '200') {
						uni.showToast({
							title: '删除成功'
						})
						this.loadOrder(this.list[this.current])
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
					
				})
			},
			loadOrder(status) {
				let params = { userId: this.user.id }
				if (status) {
					params.status = status
				}
				this.$request.get('/orders/selectAll', params).then(res => {
					this.orderList = res.data || []
				})
			}
		}
	}
</script>

<style>
	
</style>
