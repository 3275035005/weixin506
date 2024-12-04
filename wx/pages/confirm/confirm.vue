<template>
	<view>
		<view style="padding: 20rpx; padding-bottom: 120rpx;">
			<view class="box" style="background-color: #fff3e2; color: orange; padding: 10rpx 20rpx; margin-bottom: 20rpx;">
				<uni-icons type="sound" color="orange" style="position: relative; top: 3rpx;"></uni-icons>
				<text style="margin-left: 5rpx;">温馨提示: 请适量点餐, 避免浪费</text>
			</view>
			<view @click="goAddress()" v-if="!address.id" class="box" style="font-size: 24rpx; color: #009eff; margin-bottom: 20rpx;">
				<uni-icons type="plus" style="position: relative; top: 4rpx;" color="#009eff"></uni-icons>
				<text>添加收货地址</text>
			</view>
			<view @click="goAddress()" v-else class="box" style="margin-bottom: 20rpx;">
				<view style="display: flex; align-items: center; margin-bottom: 20rpx;">
					<view style="flex: 1;">
						<view style="font-weight: bold; flex: 1; font-size: 32rpx;">{{ address.address }}</view>
						<view style="color: #999; margin: 10rpx 0; font-size: 24rpx;">
							<text style="margin-right: 10rpx;">{{ address.user }} </text>
							<text>{{ address.phone }}</text>
						</view>
					</view>
					<view>
						<uni-icons type="forward" style="width: 100rpx;" color="#999"></uni-icons>
					</view>
				</view>
				<view style="display: flex; align-items: center; font-size: 24rpx; ">
					<view style="flex: 1; font-weight: bold; ">平台配送</view>
					<view style="color: #009eff;">预计30分钟内送达</view>
				</view>
			</view>
			
			<view class="box" style="margin-bottom: 20rpx;">
				<view style="color: #666; margin-bottom: 20rpx; font-size: 32rpx;">{{ business.name }}</view>
				
				<view style="margin-bottom: 20rpx;">
					<view v-for="item in cart" :key="item.id" style="display: flex; margin-bottom: 20rpx; padding: 0 20rpx;" v-if="item.goods">
						<view style="width: 100rpx; height: 100rpx;">
							<image :src="item.goods.img" style="width: 100%; height: 100%; display: block;"></image>
						</view>
						<view style="flex: 1; margin-left: 20rpx; display: flex; flex-direction: column; justify-content: space-between;">
							<view style="font-weight: bold;">{{ item.goods.name }}</view>
							<view style="color: #999; font-size: 24rpx;">x {{ item.num }}</view>
						</view>
						<view style="color: red;">￥{{ Math.floor(item.goods.actualPrice * item.num * 100) / 100 }}</view>
					</view>
				</view>
				
				<view style="text-align: right; font-size: 24rpx;">
					<text style="margin-right: 10rpx;" v-if="amount.discount">已优惠 <text style="color: red;">￥{{ amount.discount }}</text></text>
					<text>小计￥<text style="color: red; font-weight: bold; font-size: 36rpx;">{{ amount.actual }}</text></text>
				</view>
			</view>
			
			<view class="box" style="margin-bottom: 20rpx;">
				<view style="display: flex; justify-content: space-between; margin-bottom: 20rpx;">
					<view style="font-weight: bold;">商家承诺</view>
					<view>售后无忧 · 食无忧</view>
				</view>
				<view style="display: flex; justify-content: space-between; margin-bottom: 20rpx;">
					<view style="font-weight: bold;">隐私保护</view>
					<view>号码保护 · 隐私无忧</view>
				</view>
				<view style="display: flex; justify-content: space-between; margin-bottom: 20rpx;">
					<view style="font-weight: bold;">备注</view>
					<view style="color: #999;" @click="goOrderComment">
						<text v-if="orders.comment">{{ orders.comment }}</text>
						<text v-else>填写口味偏好</text>
						<uni-icons size="12" type="forward" color="#999"></uni-icons>
					</view>
				</view>
			</view>
			
			<view class="box">
				<view>
					<uni-data-checkbox v-model="orders.payType" :localdata="payTypes" />
				</view>
			</view>
		</view>
		
		<!-- 固定底部 -->
		<view style="width: 100vw; box-shadow: 0 0 10px rgba(0, 0, 0, .1); background-color: #fff; 
			position: fixed; bottom: 0;">
			<view style="display: flex; align-items: flex-end; padding: 20rpx;">
				<view style="flex: 1; padding-right: 20rpx; text-align: right;">
					已优惠 ￥<text style="color: red; margin-right: 20rpx;">{{ amount.discount }}</text>
					小计￥<text style="color: red; font-weight: bold; font-size: 36rpx;">{{ amount.actual }}</text></view>
				<button type="primary" size="mini" @click="addOrder()">提交订单</button>
			</view>
			
		</view>
		
	</view>
</template>

<script>
import { title } from 'process'
	export default {
		data() {
			return {
				businessId: 0,
				addressId: 0,
				business: {},
				address: {},
				cart: [],
				amount: {},
				orders: { comment: '', payType: '支付宝'},
				payTypes: [{ text: '支付宝', value: '支付宝' }, { text: '微信支付', value: '微信支付' }],
				user: uni.getStorageSync('xm-user')
			}
		},
		onShow() {
			let allPages = getCurrentPages() //获取当前页面栈的实例；
			let lastPages = allPages.length - 1 // 获得倒数第二个元素的索引；
			let option = allPages[lastPages].options // 获得上个页面传递的参数；
			console.log(option)
			
			this.businessId = option.businessId || 0
			this.addressId = option.addressId || 0
			this.orders.comment = option.comment || ''
			this.loadAddress()
			this.loadBusiness()
			this.loadCart()
			
		},
		methods: {
			goOrderComment() {
				uni.navigateTo({
					url: '/pages/orderComment/orderComment?businessId=' + this.businessId + '&addressId=' + this.addressId + '&comment=' + this.orders.comment
				})
			},
			addOrder() {
				if (!this.address.id) {
					uni.showToast({
						icon: 'none',
						title: '请添加收货地址'
					})
					return
				}
				if (!this.cart.length) {
					uni.showToast({
						icon: 'none',
						title: '请添加餐品'
					})
					return
				}
				this.orders.user = this.address.user
				this.orders.phone = this.address.phone
				this.orders.address = this.address.address
				this.orders.cartList = this.cart
				this.$request.post('/orders/addOrder', this.orders).then(res => {
					if (res.code === '200') {
						uni.showToast({
							title: '下单成功'
						})
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/orderItems/orderItems?orderId=' + res.data?.id
							})
						}, 1000)
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			},
			loadCart() {
				this.$request.get('/cart/selectAll', {
					businessId: this.businessId,
					userId: this.user.id
				}).then(res => {
					this.cart = res.data || []
				
					this.$request.post('/cart/calc', this.cart).then(res => {
						this.amount = res.data
					})
				})
			},
			loadBusiness() {
				this.$request.get('/business/selectById/' + this.businessId).then(res => {
					this.business = res.data || {}
				})
			},
			goAddress() {
				uni.navigateTo({
					url: '/pages/address/address?businessId=' + this.businessId + '&addressId=' + this.addressId + '&comment=' + this.orders.comment
				})
			},
			loadAddress() {
				this.$request.get('/address/selectAll', { userId: this.user.id }).then(res => {
					if (res.data?.length) {
						if (this.addressId) {
							this.address = res.data.find(v => v.id == this.addressId)
						} else {
							this.address = res.data[0]
							this.addressId = this.address.id
						}
					}
				})
			}
		}
	}
</script>

<style>

</style>
