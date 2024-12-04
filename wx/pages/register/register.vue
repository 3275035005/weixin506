<template>
	<view style="padding: 20px;">

		<view style="margin: 30rpx 0; ">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef">
				<uni-forms-item name="username" required>
					<uni-easyinput type="text" prefixIcon="person" v-model="form.username" placeholder="请输入账号" />
				</uni-forms-item>
				<uni-forms-item name="password" required>
					<uni-easyinput type="password" prefixIcon="locked" v-model="form.password"
						placeholder="密码,3-10位字母或数字" />
				</uni-forms-item>
				<uni-forms-item>
					<button size="default" @click="register"
						style="background-color: #7FFFD4; border-color: lightyellow; height: 35px; line-height: 35px;">注 册</button>
				</uni-forms-item>
				<view style="text-align: right;">
					<text style="margin-right: 5px;">前往 </text>
					<text style="color: dodgerblue;" @click="goLogin">登录 </text>
				</view>
			</uni-forms>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: { role: 'USER' },
				rules: {
					username: {
						rules: [{
							required: true,
							errorMessage: '请输入账号',
						}]
					},
					password: {
						rules: [{
							required: true,
							errorMessage: '请输入密码',
						},
						{
							minLength: 3,
							maxLength: 10,
							errorMessage: '密码长度在 {minLength} 到 {maxLength} 个字符',
						}],
					},
				}
			}
		},
		methods: {
			goLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			register() {
				this.$refs.formRef.validate().then(res => {
					this.$request.post('/register', this.form).then(res => {
						if (res.code === '200') {
							uni.showToast({
								title: `注册成功`
							})
						} else {
							uni.showToast({
								title: res.msg
							})
						}
					})
					
				}).catch(err => {
					console.log('err', err);
				})
			},
		}
	}
</script>

<style>

</style>