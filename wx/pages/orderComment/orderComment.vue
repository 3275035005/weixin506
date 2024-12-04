<template>
	<view style="padding: 20rpx;">
		<view class="box">
			<uni-forms :modelValue="form" label-width="120rpx" label-align="right">
				<uni-forms-item label="备注" name="orderComment">
					<uni-easyinput type="textarea" v-model="orderComment" placeholder="请输入备注信息" />
				</uni-forms-item>
				<uni-forms-item>
					<button type="primary" size="mini" @click="save">确 定</button>
				</uni-forms-item>
			</uni-forms>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				businessId: 0,
				addressId: 0,
				orderComment: '',
			}
		},
		onLoad(option) {
			console.log(option)
			this.businessId = option.businessId
			this.addressId = option.addressId
			this.orderComment = option.comment
		},
		methods: {
			save() {
				uni.navigateTo({
					url: '/pages/confirm/confirm?businessId=' + this.businessId + '&addressId=' + this.addressId + '&comment=' + this.orderComment
				})
			}
		}
	}
</script>

<style>

</style>