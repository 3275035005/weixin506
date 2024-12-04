<template>
	<view style="padding: 20px;">

		<view style="margin: 30px 0; background-color: #fff; padding: 40px 20px; 
			box-shadow: 0 2px 10px rgba(0,0,0,.1);  border-radius: 5px;">
			<view style="font-size: 20px; margin-bottom: 30px;">
				欢迎回来！
			</view>
			<uni-forms :modelValue="form" :rules="rules" ref="formRef">
				<uni-forms-item name="username">
					<uni-easyinput type="text" prefixIcon="person" v-model="form.username" placeholder="请输入账号" />
				</uni-forms-item>
				<uni-forms-item name="password">
					<uni-easyinput type="password" prefixIcon="locked" v-model="form.password" placeholder="请输入密码" />
				</uni-forms-item>
				<uni-forms-item>
					<button style="background-color: #7FFFD4; border-color: lightyellow; 
						height: 35px; line-height: 35px;" @click="login">登 录</button>
				</uni-forms-item>
				<view style="text-align: right; ">
					<text style="margin-right: 5px;">还没有账号？请 </text>
					<text style="color: dodgerblue;" @click="goRegister">注册 </text>
				</view>
			</uni-forms>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					role: 'USER'
				},
				rules: {
					username: {
						rules: [{
							required: true,
							errorMessage: '请填写账号',
						}]
					},
					password: {
						rules: [{
								required: true,
								errorMessage: '请填写密码',
							},
							{
								minLength: 3,
								maxLength: 10,
								errorMessage: '密码长度在 {minLength} 到 {maxLength} 个字符',
							}
						],
					},
				}
			}
		},
		methods: {
			goRegister() {
				uni.navigateTo({
					url: '/pages/register/register'
				})
			},
			login() {
				this.$refs.formRef.validate().then(res => {
					this.$request.post('/login', this.form).then(res => {
						if (res.code === '200') {
							uni.setStorageSync('xm-user', res.data)
							uni.showToast({
								title: `登录成功`
							})
							uni.switchTab({
								url: '/pages/index/index'
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
			}
		}
	}
</script>

<style>
.goods-carts {
		flex-direction: column;
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
	}
</style>