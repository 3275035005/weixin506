<template>
	<view style="padding: 20rpx;">
		<view>
			<view class="box" v-for="item in addressList" :key="item.id" style="margin-bottom: 10rpx;">
				<view style="display: flex; align-items: center; margin-bottom: 10rpx;"  @click="selectAddress(item.id)">
					<view style="flex: 1;">
						<view style="font-size: 32rpx; margin-bottom: 10rpx;">{{ item.address }}</view>
						<view style="color: #999;">
							<text style="margin-right: 10rpx;">{{ item.user }}</text>
							<text>{{ item.phone }}</text>
						</view>
					</view>
					<view>
						<uni-icons type="forward" color="#999"></uni-icons>
					</view>
				</view>
				<view style="text-align: right;">
					<uni-icons type="compose" color="#999" style="margin-right: 10rpx;" @click="editAddress(item.id)"></uni-icons>
					<uni-icons type="trash-filled" color="#999" @click="deleteAddress(item.id)"></uni-icons>
				</view>
			</view>
		</view>
		
		<view style="position: fixed; bottom: 20rpx; width: calc(100% - 40rpx)">
			<button plain type="primary" style="font-size: 32rpx; padding: 0;" @click="addAddress()">新增收货地址</button>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: uni.getStorageSync('xm-user'),
				addressList: [],
				orderComment: '',
				businessId: 0,
				addressId: 0,
			}
		},
		onShow() {
			let allPages = getCurrentPages() //获取当前页面栈的实例；
			let lastPages = allPages.length - 1 // 获得倒数第二个元素的索引；
			let option = allPages[lastPages].options // 获得上个页面传递的参数；
			this.businessId = option.businessId
			this.addressId = option.addressId
			this.orderComment = option.comment
			this.loadAddress()
		},
		methods: {
			editAddress(id) {
				uni.navigateTo({
					url: '/pages/addAddress/addAddress?id=' + id
				})
			},
			deleteAddress(id) {
				this.$request.del('/address/delete/' + id).then(res => {
					uni.showToast({
						title: '操作成功'
					})
					this.loadAddress()
				})
			},
			selectAddress(addressId) {
				this.addressId = addressId
				if (this.businessId) {
					uni.navigateTo({
						url: '/pages/confirm/confirm?businessId='  + this.businessId + '&addressId=' + this.addressId + '&comment=' + this.orderComment
					})
				}
			},
			loadAddress() {
				this.$request.get('/address/selectAll', { userId: this.user.id }).then(res => {
					this.addressList = res.data || []
				})
			},
			addAddress() {
				uni.navigateTo({
					url: '/pages/addAddress/addAddress'
				})
			}
		}
	}
</script>

<style>

</style>
