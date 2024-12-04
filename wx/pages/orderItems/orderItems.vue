<template>
	<view style="padding: 20rpx;">
		<view class="box">
			<view style="margin-bottom: 30rpx; color: #666; display: flex; align-items: baseline;">
				<view style="font-size: 32rpx; flex: 1;">{{ orders.businessName }}</view>
				<view style="text-align: right;">{{ orders.status }}</view>
			</view>
			
			<view style="margin-bottom: 20rpx;">
				<view v-for="item in orderItems" :key="item.id" style="display: flex; margin-bottom: 20rpx; padding: 0 20rpx;">
					<view style="width: 100rpx; height: 100rpx;">
						<image :src="item.goodsImg" style="width: 100%; height: 100%; display: block;"></image>
					</view>
					<view style="flex: 1; margin-left: 20rpx; display: flex; flex-direction: column; justify-content: space-between;">
						<view style="font-weight: bold;">{{ item.goodsName }}</view>
						<view style="color: #999; font-size: 24rpx;">x {{ item.num }}</view>
					</view>
					<view style="color: red;">￥{{ Math.floor(item.price * item.num * 100) / 100 }}</view>
				</view>
			</view>
			
			<view style="text-align: right; font-size: 24rpx; margin-bottom: 30rpx;">
				<text style="margin-right: 10rpx;" v-if="orders.discount">已优惠 <text style="color: red;">￥{{ orders.discount }}</text></text>
				<text>小计￥<text style="color: red; font-weight: bold; font-size: 36rpx;">{{ orders.actual }}</text></text>
			</view>
			
			<view style="text-align: right; margin-bottom: 30rpx;">
				<text class="small-btn" style="background-color: red; margin-right: 10rpx;" v-if="orders.status === '待支付'" @click="changeStatus('已取消')">取消</text>
				<text class="small-btn" v-if="orders.status === '待支付'" @click="changeStatus('待发货')">支付</text>
				<text class="small-btn" v-if="orders.status === '待收货'" style="background-color: coral;" @click="changeStatus('待评价')">确认收货</text>
				<text class="small-btn" v-if="orders.status === '待评价'" style="background-color: darkseagreen;" @click="changeStatus('已评价')">评价</text>
				<!-- <text class="small-btn" v-if="orders.status === '待发货'" style="background-color: orange;" @click="changeStatus('已退款')">申请退款</text> -->
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderId: 0,
				orders: {},
				orderItems: []
			}
		},
		onLoad(option) {
			this.orderId = option.orderId
			this.loadOrder()
			this.loadOrderItems()
		},
		// 触发返回就会使用当前方法，这里实现的是禁用物理返回，顶部导航栏的自定义返回 uni.navigateBack 仍可使用
		onUnload() {
		   uni.switchTab({
		   	url: '/pages/orders/orders'
		   })
		},  
		methods: {
			changeStatus(status) {
				let form = JSON.parse(JSON.stringify(this.orders))
				form.status = status
				this.$request.put('/orders/update/', form).then(res => {
					if (res.code === '200') {
						uni.showToast({
							title: '操作成功'
						})
						this.loadOrder()
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			},
			loadOrder() {
				this.$request.get('/orders/selectById/' + this.orderId).then(res => {
					this.orders = res.data || {}
				})
			},
			loadOrderItems() {
				this.$request.get('/ordersItem/selectAll?orderId=' + this.orderId).then(res => {
					this.orderItems = res.data || []
				})
			}
		}
	}
</script>

<style>

</style>
