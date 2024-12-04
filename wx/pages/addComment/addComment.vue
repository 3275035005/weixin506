<template>
	<view style="padding: 20rpx;">
		<view class="box">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="140rpx" label-align="right">
				<uni-forms-item label="内容" name="content">
					<uni-easyinput type="textarea" v-model="form.content" placeholder="请输入" />
				</uni-forms-item>
				<uni-forms-item label="评分" name="star">
					<uni-rate size="18" :max="5" v-model="form.star" style="position: relative; top: 15rpx;" />
				</uni-forms-item>

				<uni-forms-item>
					<button type="primary" size="mini" @click="save">保 存</button>
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
				orderId: 0,
				user: uni.getStorageSync('xm-user'),
				form: {}
			}
		},
		onLoad(option) {
			this.businessId = option.businessId
			this.orderId = option.orderId
			this.loadComment()
		},
		methods: {
			loadComment() {
				this.$request.get('/comment/selectAll', { userId: this.user.id, orderId: this.orderId }).then(res => {
					this.form = res.data.length ? res.data[0] : {}
				})
			},
			save() {
				this.form.userId = this.user.id
				this.form.businessId = this.businessId
				this.form.orderId = this.orderId
				this.$request.request({
					method: this.form.id ? 'PUT' : 'POST',
					url: this.form.id ? '/comment/update' : '/comment/add',
					data: this.form 
				}).then(res => {
					if (res.code === '200') {
						uni.showToast({
							title: '操作成功'
						})
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/orders/orders'
							})
						}, 500)
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			}
		}
	}
</script>

<style>

</style>