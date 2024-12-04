<template>
	<view style="padding: 20rpx;">
		<view class="box" style="padding: 40rpx;">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="160rpx" label-align="right">
				<uni-forms-item label="收货地址" name="address" required>
					<uni-easyinput type="text" v-model="form.address" placeholder="请输入收货地址" />
				</uni-forms-item>
				<uni-forms-item label="收货人" name="user" required>
					<uni-easyinput type="text" v-model="form.user" placeholder="请输入收货人" />
				</uni-forms-item>
				<uni-forms-item label="联系电话" name="phone" required>
					<uni-easyinput type="text" v-model="form.phone" placeholder="请输入联系电话" />
				</uni-forms-item>
			</uni-forms>
			<view style="margin: 20rpx; text-align: center;"><button type="primary" size="mini" plain
					@click="addAddress()">确定</button></view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {},
				rules: {
					name: {
						rules: [{
							required: true,
							errorMessage: '请填写收货地址',
						}]
					},
					user: {
						rules: [{
							required: true,
							errorMessage: '请填写收货人',
						}]
					},
					phone: {
						rules: [{
							required: true,
							errorMessage: '请填写联系电话',
						}]
					}
				}
			}
		},
		onLoad(option) {
			let addressId = option.id
			this.loadAddress(addressId)
		},
		methods: {
			loadAddress(id) {
				this.$request.get('/address/selectById/' + id).then(res => {
					this.form = res.data || {}
				})
			},
			addAddress() {
				this.$refs.formRef.validate().then(res => {
					this.$request.request({
						url: this.form.id ? '/address/update' : '/address/add',
						method: this.form.id ? 'PUT' : 'POST',
						data: this.form
					}).then(res => {
						if (res.code === '200') {
							uni.showToast({
								title: '操作成功'
							})
							setTimeout(() => {
								uni.navigateBack({
									url: '/pages/address/address'
								})
							}, 1000)
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
						}
					})
				}).catch(err => {
					console.log('err', err);
				})
			}
		}
	}
</script>

<style>

</style>