<template>
	<view>
		
		<view style="padding: 20rpx 20rpx 0 20rpx;" v-if="business.id">
			<!-- 商家介绍 -->
			<view class="box" style="display: flex; align-items: center; margin-bottom: 20rpx;">
				<view style="flex: 1">
					<view style="display: flex; align-items: baseline;">
						<text style="font-size: 40rpx;">{{ business.name }}</text>
						<view>
							<uni-icons v-if="business.isCollect" size="18" color="orange" type="star-filled" style="margin-left: 20rpx;"  @click="collect"></uni-icons>
							<uni-icons v-else size="18" type="star" style="margin-left: 20rpx;" @click="collect"></uni-icons>
						</view>
					</view>
					<view style=" font-size: 24rpx; margin: 10rpx 0; color: #666;">
						<text style="margin-right: 10rpx; padding-right: 10rpx; border-right: 2rpx solid #ccc;">平台配送</text>
						<text style="margin-right: 10rpx; padding-right: 10rpx; border-right: 2rpx solid #ccc;">免配送费</text>
						<text>30分钟内送达</text>
					</view>
					<view style="font-size: 24rpx; color: #666; margin: 10rpx 0;">
						地址：{{ business.address }}
					</view>
					<view style="font-size: 24rpx; color: #666;">
						联系电话：{{ business.phone }}
					</view>
				</view>
				<view style="width: 160rpx; height: 160rpx; ">
					<image mode="widthFix" :src="business.avatar" style="width: 100%; border-radius: 50%;"></image>
				</view>
			</view>
			<!-- 商家介绍结束 -->
			
			<view style="margin-bottom: 10rpx; display: flex; background-color: #fff; ">
				<view :class="{'view-active' : currentView === 0 }" style="padding: 15rpx; border-radius: 10rpx 0 0 10rpx;" @click="currentView = 0">全部商品</view>
				<view :class="{'view-active' : currentView === 1 }" style="padding: 15rpx 30rpx; border-radius: 0 10rpx 10rpx 0;" @click="currentView = 1">评价</view>
			</view>
			
			<!--  分类列表 -->
			<view style="background-color: #fff;" v-if="currentView === 0">
				<view style="display: flex;">
					<view style="width: 240rpx; flex-direction: column;">
						<view v-for="item in categoryList" :key="item.id" class="nav-item"
							:class="{ 'active': item.id === currentCategoryId }" @click="loadGoods(item.id)">{{ item.name }}
						</view>
					</view>
					<scroll-view style="height: calc(100vh - 508rpx);" scroll-y="true">
						<view style="flex: 1; padding: 20px;">
							<view v-for="item in goodsList" :key="item.id" style="display: flex; margin-bottom: 20px;">
								<view style="width: 160rpx; height: 160rpx;">
									<image :src="item.img" mode="scaleToFill"
										style="width: 100%; height: 100%; border-radius: 10rpx;"></image>
								</view>
								<view style="flex: 1; display: flex; flex-direction: column; margin-left: 20rpx;">
									<view style="font-size: 28rpx; font-weight: bold; margin-bottom: 10rpx;">{{ item.name }}</view>
									<view style="color: #999; font-size: 24rpx; margin-bottom: 5rpx;">{{ item.descr }}</view>
									<view style=" margin-bottom: 5rpx;">
										<text class="mini-btn" style="color: orangered; border-color: orangered; 
											font-size: 20rpx; margin-right: 10rpx;" v-if="item.discount !== 1.0">{{ item.discount * 10 }}折</text>
										<text style="color: #999; font-size: 24rpx;">已售{{ item.saleCount }}</text>
									</view>
									<view style="margin-bottom: 10rpx; font-size: 24rpx; font-weight: bold;">
										<text style="color: #333; text-decoration: line-through; margin-right: 10rpx;" v-if="item.discount !== 1.0">￥{{ item.price }}</text>
										<text style="color: red; margin-right: 5rpx;">￥{{ item.actualPrice }}</text>
										<text class="mini-btn" style="color: orangered; border-color: orangered; font-size: 20rpx;">到手价</text>
									</view>
									<view>
										<text style="padding: 0 10rpx; background-color: orange; font-size: 24rpx; color: #fff; border-radius: 4rpx;" @click="buy(item)">选购</text>
									</view>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
			</view>
			<!-- 分类列表结束 -->
			
			<!--  评价列表 -->
			<view class="box" v-if="currentView === 1">
				<scroll-view style="height: calc(100vh - 448rpx);" scroll-y="true">
					<view v-for="item in comments" :key="item.id" style="display: flex; border-bottom: 1rpx solid #eee; padding: 20rpx 0;">
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
				</scroll-view>
			</view>
			<!--  评价列表结束 -->
			
		</view>

		
		<view class="goods-carts" v-if="business.id">
			<uni-goods-nav :options="options" :fill="true" :button-group="buttonGroup" @click="onClick"
				@buttonClick="addOrder" />
		</view>
		
		<uni-popup ref="popup" type="bottom" background-color="#fff">
			<scroll-view style="max-height: 70vh;" scroll-y="true">
				<view style="padding: 40rpx 40rpx 280rpx 40rpx;">
					<view style="text-align: right; margin-bottom: 10rpx; color: #999;">
						<uni-icons style="position: relative; top: 4rpx;" type="trash" size="16" color="#999" @click="deleteAll"></uni-icons>清空</view>
					<view v-for="(item, index) in cart" :key="index" style="display: flex; margin-bottom: 20rpx;" v-if="item.goods">
						<view style="width: 100rpx; height: 100rpx;">
							<image style="width: 100%; height: 100%; display: inline-block;" :src="item.goods.img"></image>
						</view>
						<view style="flex: 1; margin-left: 20rpx; display: flex; flex-direction: column; justify-content: space-around;">
							<view style="flex: 1;">{{ item.goods.name }}</view>
							<view style="flex: 1; color: red; display: flex; align-items: flex-end;">
								<view style="flex: 1;">￥{{ item.goods.actualPrice }}</view>
								<view style="flex: 1; display: flex; justify-content: right;">
									<uni-number-box :min="1" v-model="item.num" @change="updateCart(item)"></uni-number-box>
								</view>
							</view>
						</view>
					</view>
					<view style="margin-top: 40rpx; text-align: right;" v-if="amount.actual">总金额：<text style="color: red;">￥{{ amount.actual }}</text></view>
				</view>
			</scroll-view>
			
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				businessId: 0,
				business: {},
				categoryList: [],
				goodsList: [],
				cart: [],
				comments: [],
				currentCategoryId: 0,
				amount: {
					actual: 0
				},
				currentView: 0,
				user: uni.getStorageSync('xm-user'),
				options: [{
					icon: 'cart',
					text: '购物车',
					info: 0
				}],
				buttonGroup: [{
					text: '立即下单',
					backgroundColor: 'linear-gradient(90deg, #FFCD1E, #FF8A18)',
					color: '#fff'
				}],
			}
		},
		onLoad(option) {
			this.businessId = option.businessId
			this.loadBusiness()
			this.loadCategory()
			this.loadCart()
			this.loadComments()
		},
		methods: {
			loadComments() {
				this.$request.get('/comment/selectAll?businessId=' + this.businessId).then(res => {
					this.comments = res.data || []
				})
			},
			collect() {
				if (this.business.isCollect) {  // 取消收藏
					this.$request.del('/collect/delete/' + this.businessId + '/' + this.user.id).then(res => {
						uni.showToast({
							title: '操作成功'
						})
						this.loadBusiness()
					})
				} else {
					this.$request.post('/collect/add', { businessId: this.businessId, userId: this.user.id }).then(res => {
						uni.showToast({
							title: '操作成功'
						})
						this.loadBusiness()
					})
				}
			},
			deleteAll() {
				let ids = this.cart.map(v => v.id)
				this.$request.del('/cart/delete/batch', ids).then(res => {
					this.loadCart()
				})
			},
			updateCart(cart) {
				this.$request.put('/cart/update', cart).then(res => {
					this.changeCartGoodsNum()
				})
			},
			changeCartGoodsNum() {   // 计算总数量和总价格
				let sum = this.cart.map(v => v.num).reduce((sum, item) => { return sum += item }, 0)  // 购物车所有商品数量求和
				this.options[0].info = sum
				this.$request.post('/cart/calc', this.cart).then(res => {
					this.amount = res.data
				})
			},
			buy(goods) {
				let cart = { goodsId: goods.id, num: 1, businessId: this.businessId }
				this.cart.push(cart)
				this.$request.post('/cart/add', cart).then(res => {  // 请求接口
					uni.showToast({
						icon: 'none',
						title: '加入购物车成功'
					})
					this.loadCart()
				})   
			},
			loadBusiness() {
				this.$request.get('/business/selectById/' + this.businessId).then(res => {
					this.business = res.data || {}
				})
			},
			loadCategory() {
				this.$request.get('/category/selectAll', {
					businessId: this.businessId
				}).then(res => {
					this.categoryList = res.data || []
					this.currentCategoryId = this.categoryList.length ? this.categoryList[0].id : 0
					this.loadGoods(this.currentCategoryId)
				})
			},
			loadGoods(categoryId) {
				this.currentCategoryId = categoryId
				this.$request.get('/goods/selectAll', {
					categoryId: categoryId
				}).then(res => {
					this.goodsList = res.data || []
				})
			},
			loadCart() {
				this.$request.get('/cart/selectAll', {
					businessId: this.businessId,
					userId: this.user.id
				}).then(res => {
					this.cart = res.data || []
					this.changeCartGoodsNum()
				})
			},
			onClick(e) {
				this.$refs.popup.open('bottom')
			},
			addOrder(e) {
				if (!this.cart.length) {
					uni.showToast({
						icon: 'none',
						title: '请选择餐品'
					})
					return
				}
				// 下单
				uni.navigateTo({
					url: '/pages/confirm/confirm?businessId=' + this.businessId
				})
			}
		}
	}
</script>

<style>
	.nav-item {
		background-color: #eee;
		padding: 10px 15px;
	}

	.active {
		border-left: 2px solid #ffd100;
		background-color: #fff;
		color: orange;
	}
	.view-active {
		background-color: #ffd100;
	}
</style>